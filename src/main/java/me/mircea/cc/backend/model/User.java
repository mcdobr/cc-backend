package me.mircea.cc.backend.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotBlank;

@Document
@Getter
@Setter
@NoArgsConstructor
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @Id
    @ToString.Include
    private String id;

    @NotBlank(message = "User's last name should not be blank")
    private String lastName;

    @NotBlank(message = "User's first name should not be blank")
    private String firstName;

    @NotBlank(message = "User's email address should not be blank")
    @Indexed(unique = true)
    @ToString.Include
    @EqualsAndHashCode.Include
    private String email;
}
