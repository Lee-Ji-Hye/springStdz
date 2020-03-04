package com.pjt.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.pjt.domain.Community;
import com.pjt.domain.Task;
import com.pjt.service.CommunityService;
import com.pjt.service.TaskService;

@Controller
@RequestMapping(value = "/test")
public class TaskController {
	// TODO 필드 주입에 대한 체크
	@Autowired
	TaskService taskService;
	
	@Autowired
	CommunityService communityService;
	
	/**
	 * @method Task 리스트 조회
	 * */
	@GetMapping(value = "")
	public String List(Model model) {
		List<Task> tasks = (List<Task>) taskService.findAll();
		List<Community> cmntys = (List<Community>) communityService.findAll();
		model.addAttribute("tasks", tasks);
		model.addAttribute("cmntys", cmntys);
		return "index";
	}
	
	/**
	 * @method Task 생성,  수정
	 * */
	

	
	/**
	 * @method Task 삭제
	 * */
	
	
	
}
