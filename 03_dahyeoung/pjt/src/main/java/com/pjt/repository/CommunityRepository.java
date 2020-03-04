package com.pjt.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pjt.domain.Community;
/**
 * @author Danny Yoon
 * */
@Repository
public interface CommunityRepository extends JpaRepository<Community, Long>{

}
