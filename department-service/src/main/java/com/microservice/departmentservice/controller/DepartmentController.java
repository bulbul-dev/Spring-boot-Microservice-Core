package com.microservice.departmentservice.controller;

import com.microservice.departmentservice.entity.Department;
import com.microservice.departmentservice.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/departments")
@Slf4j
public class DepartmentController {

    private final DepartmentService departmentService;

    //create department
    @PostMapping("/")
    public Department createDepartment(@RequestBody Department department){
        log.info("Creating department: {}", department);
        return departmentService.createDepartment(department);
    }

    //get department by department Id
    @GetMapping("/{id}")
    public Department getDepartmentById(@PathVariable Long id){
        log.info("Getting department by department Id: {}", id);
        return departmentService.getDepartmentById(id);
    }
}
