package com.bankrest.controller;
import com.bankrest.dto.*; import com.bankrest.service.CardService; import com.bankrest.repo.UserRepository; import jakarta.validation.Valid;
import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import org.springframework.data.domain.PageRequest;
@RestController @RequestMapping("/api/user")
public class UserCardController {
  private final CardService svc; private final UserRepository users;
  public UserCardController(CardService s, UserRepository u){ this.svc=s; this.users=u; }
  private Long uid(Authentication a){ return users.findByUsername(a.getName()).orElseThrow().getId(); }

  @GetMapping("/cards")
  public com.bankrest.service.CardService.PageCards myCards(Authentication a, @RequestParam(required=false) String q,
                                                            @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="10") int size){
    return svc.listOwn(uid(a), q, page, size);
  }
  @PostMapping("/cards/{id}/block-request")
  public CardResponse requestBlock(Authentication a, @PathVariable Long id){ return svc.requestBlock(uid(a), id); }
  @PostMapping("/transfer")
  public void transfer(Authentication a, @RequestBody @Valid TransferRequest r){ svc.transferBetweenOwn(uid(a), r.fromCardId(), r.toCardId(), r.amount()); }
  @GetMapping("/cards/{id}/balance")
  public java.math.BigDecimal balance(Authentication a, @PathVariable Long id){ return svc.balance(uid(a), id); }
}
