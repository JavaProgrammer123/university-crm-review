package com.example.microservice.repository;

import com.example.microservice.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityRepository extends JpaRepository<University, Long> {

    Optional<University> findByCityAndName(String city, String name);

    List<University> findAllByCity(String city);
}
