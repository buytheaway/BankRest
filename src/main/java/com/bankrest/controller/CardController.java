package com.bankrest.controller;
import com.bankrest.dto.*; import com.bankrest.service.CardService; import jakarta.validation.Valid;
import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import org.springframework.data.domain.*;
import java.math.BigDecimal;
@RestController @RequestMapping("/api/cards")
public class CardController {
  private final CardService svc; public CardController(CardService s){ this.svc=s; }
  private Long uid(Authentication a){ return null==a?null:java.util.Objects.requireNonNullElseGet((Long)a.getDetails(),()->null); }
  private Long requireUserId(Authentication a){ // we store username -> fetch id on demand (simpler: lookup in service)
    return null; // not used, see below helper
  }
  private Long userIdFromPrincipal(Authentication a, com.bankrest.repo.UserRepository users){ return users.findByUsername(a.getName()).orElseThrow().getId(); }
}
