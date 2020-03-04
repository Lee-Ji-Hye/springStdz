package com.pjt.service;

import java.util.Optional;

import com.pjt.domain.Task;

public interface TaskService {
	/**
	 * @Method Task 리스트 조회
	 * */
	public Iterable<Task> findAll();

	/**
	 * @Method Task 상세 조회
	 * */
	public Optional<Task> findById(Long tskId);
	
	/**
	 * @method Task 생성, 수정
	 * */
	public Task save(Task task);
	
	/**
	 * @method Task 삭제
	 * */
	public void delete(Task task);
}
