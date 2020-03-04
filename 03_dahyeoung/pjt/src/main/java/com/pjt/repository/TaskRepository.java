package com.pjt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pjt.domain.Task;
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>{

}
