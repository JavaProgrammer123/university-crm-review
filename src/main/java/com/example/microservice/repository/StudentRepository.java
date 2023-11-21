package com.example.microservice.repository;

import com.example.microservice.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Student findStudentByTelegram (Long id);

    Student findStudentByUser_Phone(String phone);

    List<Student> findAllByUniversityName(String name);
}
