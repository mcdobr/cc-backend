package me.mircea.cc.backend.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
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
@Table("transaction")
public class Transaction {
    @Id
    @ToString.Include
    private UUID id;

    @NotNull
    private BigDecimal sum;

    @NotNull
    private UUID sender;

    @NotNull
    private UUID receiver;

    @NotNull
    @CreatedDate
    private Instant createdTimestamp;

    @NotNull
    @LastModifiedDate
    private Instant lastModifiedTimestamp;

    private String description;
}
