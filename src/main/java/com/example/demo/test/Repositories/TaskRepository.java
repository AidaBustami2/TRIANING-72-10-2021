package com.example.demo.test.Repositories;

import com.example.demo.test.Models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findBystatusLessThanEqual(int status);
}
