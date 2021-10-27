package com.example.demo.test.Repositories;

import com.example.demo.test.Models.Machine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository


public interface MachineRepository extends JpaRepository<Machine, Long> {


}
