package com.pjt.pjt.repository;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pjt.domain.Community;
import com.pjt.domain.Task;
import com.pjt.repository.CommunityRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CommunityRepoitoryTest {
	@Autowired
	CommunityRepository repository;

	@Test
	public void testRepository() {
		Community cmnty = new Community();
		cmnty.setCmntyId(1L);
		cmnty.setCmntyNm("Shinsegae");
		cmnty.setCmntyDay("2,3");

		repository.save(cmnty);
		//Assert.assertEquals()
		// TODO findById
		Assert.assertNotNull(cmnty.getCmntyId());
	}
}
