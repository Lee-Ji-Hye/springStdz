package com.pjt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.pjt.domain.Task;
import com.pjt.service.TaskService;

@RestController
@RequestMapping(value = "/task")
public class TaskApiController {
	
	@Autowired
	TaskService taskService;

	/**
	 * @method Task 리스트
	 * */
	@GetMapping(value = "")
	public ResponseEntity<List<Task>> List() {
		List<Task> tasks = (List<Task>) taskService.findAll();
		//model.addAttribute("tasks", tasks);
		return new ResponseEntity<>(tasks, HttpStatus.OK);
	}
	
	/**
	 * @method Task 생성
	 * */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public Task Save(@RequestBody Task task) {
		Task result = taskService.save(task);
		return result;
	}
}
