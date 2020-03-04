package com.pjt.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pjt.domain.Community;
import com.pjt.domain.Task;
import com.pjt.repository.CommunityRepository;
import com.pjt.service.CommunityService;

@Service
public class CommunityServiceImpl implements CommunityService {

	@Autowired
	CommunityRepository communityRepository;

	@Override
	public Iterable<Community> findAll() {
		return communityRepository.findAll();
	}

	@Override
	public Optional<Community> findById(Long cmntyId) {
		return communityRepository.findById(cmntyId);
	}

	@Override
	public Community save(Community cmnty) {
		return communityRepository.save(cmnty);
	}

	@Override
	public void delete(Community cmnty) {
		communityRepository.delete(cmnty);

	}

}
