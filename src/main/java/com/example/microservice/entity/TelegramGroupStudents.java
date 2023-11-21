package com.example.microservice.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "telegram_group_student")
public class TelegramGroupStudents {

    @Id
    @Column(name = "telegram_group_id")
    private Long telegramGroupId;

    @Column(name = "students_id")
    private Long studentsId;
}
