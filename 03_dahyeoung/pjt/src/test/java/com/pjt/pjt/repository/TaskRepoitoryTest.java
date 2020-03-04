package com.pjt.pjt.repository;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.pjt.domain.Task;
import com.pjt.repository.TaskRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class TaskRepoitoryTest {
	@Autowired
	TaskRepository repository;

	@Test
	public void testRepository() {
		Task tsk = new Task();
		tsk.setTskId(3L);
		tsk.setTskDes("Lokesh");
		tsk.setTskDueDt(new Date());

		repository.save(tsk);

		Assert.assertNotNull(tsk.getTskId());
	}
}
