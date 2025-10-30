package com.bankrest.service;
import com.bankrest.domain.CardStatus; import com.bankrest.entity.CardEntity; import com.bankrest.entity.UserEntity; import com.bankrest.repo.CardRepository; import com.bankrest.repo.UserRepository; import com.bankrest.util.Masking;
import jakarta.transaction.Transactional; import org.springframework.data.domain.*; import org.springframework.stereotype.Service;
import java.math.BigDecimal; import java.util.NoSuchElementException;
@Service
public class CardService {
  private final CardRepository cards; private final UserRepository users;
  public CardService(CardRepository c, UserRepository u){ this.cards=c; this.users=u; }

  public record PageCards(java.util.List<com.bankrest.dto.CardResponse> content, long total){}
  public com.bankrest.dto.CardResponse toDto(CardEntity e){
    var numMasked = Masking.mask16(new com.bankrest.security.AttributeEncryptor().convertToEntityAttribute(e.getNumberEnc()));
    var s = e.isExpiredNow()? CardStatus.EXPIRED : e.getStatus();
    return new com.bankrest.dto.CardResponse(e.getId(), numMasked, e.getExpiryMonth(), e.getExpiryYear(), s, e.isBlockRequested(), e.getBalance());
  }

  @Transactional
  public com.bankrest.dto.CardResponse adminCreate(Long ownerId, String number16, Integer m, Integer y, BigDecimal init){
    UserEntity owner = users.findById(ownerId).orElseThrow(()->new NoSuchElementException("owner not found"));
    CardEntity e = CardEntity.builder()
      .owner(owner)
      .numberEnc(new com.bankrest.security.AttributeEncryptor().convertToDatabaseColumn(number16))
      .expiryMonth(m).expiryYear(y).status(CardStatus.ACTIVE)
      .balance(init==null?BigDecimal.ZERO:init.max(BigDecimal.ZERO)).build();
    return toDto(cards.save(e));
  }
  @Transactional public void adminSetStatus(Long id, CardStatus st){ var e=cards.findById(id).orElseThrow(); e.setStatus(st); e.setBlockRequested(false); }
  @Transactional public void adminDelete(Long id){ cards.deleteById(id); }

  public PageCards listOwn(Long userId, String q, int page, int size){
    var owner = users.findById(userId).orElseThrow();
    Page<CardEntity> pg = (q!=null && !q.isBlank())? cards.findByOwnerAndNumberEncContainingIgnoreCase(owner,q,PageRequest.of(page,size))
                                                   : cards.findByOwner(owner, PageRequest.of(page,size));
    return new PageCards(pg.map(this::toDto).toList(), pg.getTotalElements());
  }

  @Transactional
  public com.bankrest.dto.CardResponse requestBlock(Long userId, Long cardId){
    var e = cards.findByIdAndOwnerId(cardId, userId).orElseThrow();
    e.setBlockRequested(true); return toDto(e);
  }

  @Transactional
  public void transferBetweenOwn(Long userId, Long fromId, Long toId, BigDecimal amount){
    if(amount==null || amount.compareTo(BigDecimal.ZERO)<=0) throw new IllegalArgumentException("amount must be > 0");
    var from = cards.findByIdAndOwnerId(fromId, userId).orElseThrow();
    var to   = cards.findByIdAndOwnerId(toId, userId).orElseThrow();
    if(from.getStatus()!=CardStatus.ACTIVE || to.getStatus()!=CardStatus.ACTIVE) throw new IllegalStateException("card not active");
    if(from.isExpiredNow() || to.isExpiredNow()) throw new IllegalStateException("card expired");
    if(from.getBalance().compareTo(amount)<0) throw new IllegalStateException("insufficient funds");
    from.setBalance(from.getBalance().subtract(amount)); to.setBalance(to.getBalance().add(amount));
  }

  public java.math.BigDecimal balance(Long userId, Long cardId){
    var e = cards.findByIdAndOwnerId(cardId, userId).orElseThrow();
    return e.getBalance();
  }
}
