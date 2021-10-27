package com.example.demo.test.Repositories;

import com.example.demo.test.Models.Thread;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface ThreadRepository extends JpaRepository<Thread, Long> {
    List<Thread> findByBusy(boolean Busy);
}
