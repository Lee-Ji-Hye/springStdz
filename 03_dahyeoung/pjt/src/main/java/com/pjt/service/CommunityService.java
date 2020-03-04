package com.pjt.service;

import java.util.Optional;

import com.pjt.domain.Community;

public interface CommunityService {
	/**
	 * @Method Community 리스트 조회
	 * */
	public Iterable<Community> findAll();
	
	/**
	 * @Method Community 상세 조회
	 * */
	public Optional<Community> findById(Long cmntyId);
	
	/**
	 * @method Community 생성, 수정
	 * */
	public  Community save(Community cmnty);
	
	/**
	 * @method Community 삭제
	 * */
	public void delete(Community cmnty);
}
