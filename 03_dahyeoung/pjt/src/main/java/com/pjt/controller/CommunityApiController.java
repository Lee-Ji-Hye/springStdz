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

import com.pjt.domain.Community;
import com.pjt.service.CommunityService;

@RestController
@RequestMapping(value = "/cmnty")
public class CommunityApiController {
	
	@Autowired
	CommunityService communityService;

	/**
	 * @method Task 리스트
	 * */
	@GetMapping(value = "")
	public ResponseEntity<List<Community>> List() {
		List<Community> cmntys = (List<Community>) communityService.findAll();
		return new ResponseEntity<>(cmntys, HttpStatus.OK);
	}
	
	/**
	 * @method Task 생성
	 * */
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public Community Save(@RequestBody Community cmnty) {
		Community result = communityService.save(cmnty);
		return result;
	}
}
