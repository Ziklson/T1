package com.kuvarin.taskcrud.model;

import com.kuvarin.taskcrud.enums.TaskStatus;
import jakarta.persistence.*;
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

    @Enumerated(EnumType.STRING)
    private TaskStatus taskStatus;

}
