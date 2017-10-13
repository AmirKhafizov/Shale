package ru.itis.models;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class UserInformation {
    private String name;
    private String surname;
    private User user;
}
