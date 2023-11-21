package com.example.microservice.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.util.Set;

@Entity
@Builder
@Setter
@Getter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "telegram_group")
public class TelegramGroup {

    @Id
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "telegram_group_student",
            joinColumns = @JoinColumn(name = "telegram_group_id"),
            inverseJoinColumns = @JoinColumn(name = "students_id"))
    private Set<User> users;

}
