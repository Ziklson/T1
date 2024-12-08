package com.kuvarin.taskcrud.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "task")
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
public class Task {
    @Id
    @GeneratedValue
    private Long id;

    private String title;

    private String description;

    private Long userId;

}
