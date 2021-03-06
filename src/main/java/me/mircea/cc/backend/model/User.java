package me.mircea.cc.backend.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;

import javax.validation.constraints.NotBlank;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @ToString.Include
    private String id;

    private String name;

    @NotBlank(message = "User's email address should not be blank")
    @ToString.Include
    @EqualsAndHashCode.Include
    private String email;

    @CreatedDate
    private Instant createdTimestamp;

    @LastModifiedDate
    private Instant lastModifiedTimestamp;
}
