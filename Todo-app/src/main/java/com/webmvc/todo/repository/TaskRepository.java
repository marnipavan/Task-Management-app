package com.webmvc.todo.repository;


import com.webmvc.todo.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
    // Add custom methods if needed
}