package com.microservice.departmentservice.service;

import com.microservice.departmentservice.entity.Department;
import com.microservice.departmentservice.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DepartmentService {

    private final DepartmentRepository repository;


    public Department createDepartment(Department department) {
        log.info("Creating department from department service: {}", department);
        return repository.save(department);
    }

    public Department getDepartmentById(Long id) {
        log.info("Getting department by id: {}", id);
        return repository.findById(id).orElse(null);
    }
}
