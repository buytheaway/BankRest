package com.bankrest.service;
import com.bankrest.domain.CardStatus; import com.bankrest.entity.CardEntity; import com.bankrest.entity.UserEntity; import com.bankrest.repo.CardRepository; import com.bankrest.repo.UserRepository;
import org.junit.jupiter.api.*; import org.mockito.Mockito; import java.math.BigDecimal; import java.util.Optional;
class CardServiceUnitTest {
  @Test void transfer_ok(){
    var cards=Mockito.mock(CardRepository.class); var users=Mockito.mock(UserRepository.class);
    var svc=new CardService(cards, users);
    var user=new UserEntity(); user.setId(1L); Mockito.when(users.findById(1L)).thenReturn(Optional.of(user));
    var from=new CardEntity(); from.setId(10L); from.setOwner(user); from.setStatus(CardStatus.ACTIVE); from.setExpiryMonth(12); from.setExpiryYear(2099); from.setBalance(new BigDecimal("100.00"));
    var to=new CardEntity(); to.setId(11L); to.setOwner(user); to.setStatus(CardStatus.ACTIVE); to.setExpiryMonth(12); to.setExpiryYear(2099); to.setBalance(new BigDecimal("0.00"));
    Mockito.when(cards.findByIdAndOwnerId(10L,1L)).thenReturn(Optional.of(from));
    Mockito.when(cards.findByIdAndOwnerId(11L,1L)).thenReturn(Optional.of(to));
    svc.transferBetweenOwn(1L,10L,11L,new BigDecimal("25.00"));
    Assertions.assertEquals(new BigDecimal("75.00"), from.getBalance());
    Assertions.assertEquals(new BigDecimal("25.00"), to.getBalance());
  }
}
