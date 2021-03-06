package me.mircea.cc.backend.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("transaction_history")
public class Transaction {
    @Id
    @ToString.Include
    private UUID id;

    @NotNull
    @Column("sum")
    private BigDecimal sum;

    @NotBlank
    @Column("other_party")
    private String otherParty;

    @NotNull
    @Column("transaction_type")
    private TransactionType type;

    @NotNull
    @CreatedDate
    @Column("created_at")
    private Instant createdTimestamp;

    @NotNull
    @LastModifiedDate
    @Column("last_modified_at")
    private Instant lastModifiedTimestamp;

    @Column("description")
    private String description;
}
