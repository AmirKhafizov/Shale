package ru.itis.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class Dish {
    private Long id;
    private String name;
    private String description;
    private CategoryDish categoryDish;
}
