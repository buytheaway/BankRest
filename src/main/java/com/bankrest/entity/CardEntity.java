package com.bankrest.entity;
import com.bankrest.domain.CardStatus; import com.bankrest.security.AttributeEncryptor;
import jakarta.persistence.*; import lombok.*; import java.math.BigDecimal; import java.time.YearMonth;
@Entity @Table(name="card")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class CardEntity {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @ManyToOne(optional=false) @JoinColumn(name="owner_id") private UserEntity owner;
  @Convert(converter=AttributeEncryptor.class) @Column(name="number_enc", nullable=false, unique=true) private String numberEnc;
  @Column(nullable=false) private Integer expiryMonth; // 1..12
  @Column(nullable=false) private Integer expiryYear;  // YYYY
  @Enumerated(EnumType.STRING) @Column(nullable=false) private CardStatus status=CardStatus.ACTIVE;
  @Column(nullable=false, precision=19, scale=2) private BigDecimal balance = BigDecimal.ZERO;
  @Column(nullable=false) private boolean blockRequested=false;

  public boolean isExpiredNow(){ return YearMonth.now().isAfter(YearMonth.of(expiryYear, expiryMonth)); }
}
