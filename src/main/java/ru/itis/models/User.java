package ru.itis.models;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class User {
    private Long id;
    protected String login;
    protected String password;
    private UserInformation userInformation;
}
