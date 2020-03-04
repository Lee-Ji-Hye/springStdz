package com.pjt.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pjt.domain.Task;
import com.pjt.repository.TaskRepository;
import com.pjt.service.TaskService;

@Service
public class TaskServiceImpl implements TaskService {
	
	@Autowired
	TaskRepository taskRepository;
	
	@Override
	public Iterable<Task> findAll() {
		return taskRepository.findAll();
	}
	
	@Override
	public Optional<Task> findById(Long tskId) {
		return taskRepository.findById(tskId);
	}
	
	@Override
	public Task save(Task task) {
		return taskRepository.save(task);
	}
	
	@Override
	public void delete(Task task) {
		taskRepository.delete(task);
	}


}
