package com.example.microservice.mapper;

import com.example.microservice.dto.request.StudentRequestDTO;
import com.example.microservice.dto.response.StudentResponseDTO;
import com.example.microservice.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class StudentMapper {

    @Mapping(source = "student.user.name", target = "name")
    @Mapping(source = "student.user.surname", target = "surname")
    @Mapping(source = "student.university.name", target = "university.name")
    public abstract StudentResponseDTO entityToResponse(Student student);

    public abstract Student requestToEntity(StudentRequestDTO studentRequestDTO);

    public abstract List<StudentResponseDTO> listEntityToListResponse(List<Student> students);
}
