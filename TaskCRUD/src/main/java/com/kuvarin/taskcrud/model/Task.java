package com.kuvarin.taskcrud.model;

import com.kuvarin.taskcrud.enums.TaskStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Setter;
import lombok.Builder;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;



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
