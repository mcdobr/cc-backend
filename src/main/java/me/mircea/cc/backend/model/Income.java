package me.mircea.cc.backend.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Income {
    @Id
    @ToString.Include
    private String id;

    private BigDecimal sum;

    private String description;

    private String email;

    @CreatedDate
    private Instant createdTimestamp;

    @LastModifiedDate
    private Instant lastModifiedTimestamp;
}
