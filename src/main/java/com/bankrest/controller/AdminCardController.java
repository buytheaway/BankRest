package com.bankrest.controller;
import com.bankrest.domain.CardStatus; import com.bankrest.dto.*; import com.bankrest.service.CardService;
import jakarta.validation.Valid; import org.springframework.security.access.prepost.PreAuthorize; import org.springframework.web.bind.annotation.*; import java.math.BigDecimal;
@RestController @RequestMapping("/api/admin/cards") @PreAuthorize("hasRole('ADMIN')")
public class AdminCardController {
  private final CardService svc; public AdminCardController(CardService s){ this.svc=s; }
  @PostMapping public CardResponse create(@Valid @RequestBody CardCreateRequest r){
    return svc.adminCreate(r.ownerId(), r.cardNumber(), r.expiryMonth(), r.expiryYear(), r.initialBalance());
  }
  @PatchMapping("{id}/block") public void block(@PathVariable Long id){ svc.adminSetStatus(id, CardStatus.BLOCKED); }
  @PatchMapping("{id}/activate") public void activate(@PathVariable Long id){ svc.adminSetStatus(id, CardStatus.ACTIVE); }
  @DeleteMapping("{id}") public void delete(@PathVariable Long id){ svc.adminDelete(id); }
}
