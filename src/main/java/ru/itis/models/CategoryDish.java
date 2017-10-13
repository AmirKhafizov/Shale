package ru.itis.models;

import lombok.*;

import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class CategoryDish {
    private Long id;
    private String name;
    private List<Dish> dishes;
}
