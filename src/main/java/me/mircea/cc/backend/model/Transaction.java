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

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table("user_transaction")
public class Transaction {
    @Id
    @ToString.Include
    private UUID id;

    @NotNull
    @Column("sum")
    private BigDecimal sum;

    @NotNull
    @Column("sender")
    private UUID sender;

    @NotNull
    @Column("receiver")
    private UUID receiver;

    @NotNull
    @CreatedDate
    @Column("created_at")
    private Instant createdTimestamp;

    @NotNull
    @LastModifiedDate
    @Column("last_modified_at")
    private Instant lastModifiedTimestamp;

    private String description;
}
