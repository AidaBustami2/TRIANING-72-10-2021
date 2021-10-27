package com.example.demo.test.Repositories;

import com.example.demo.test.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository

public interface UserRepository extends JpaRepository<User, Long> {
}
