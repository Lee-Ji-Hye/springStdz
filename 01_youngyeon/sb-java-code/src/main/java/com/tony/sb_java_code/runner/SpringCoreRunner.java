package com.tony.sb_java_code.runner;

import com.tony.sb_java_code.customQualifier.Shepperd;
import com.tony.sb_java_code.dto.Person;
import com.tony.sb_java_code.service.SellPhone;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Profile;

@Profile(value = "dev")
@Slf4j
@ComponentScan(basePackages = "com.tony.sb_java_code",
   excludeFilters = {
      @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = SellPhone.class),
      @ComponentScan.Filter(Configuration.class)
   })
public class SpringCoreRunner implements ApplicationRunner {
   @Autowired
   SellPhone sellPhone;

   @Autowired
   Person person;

   @Autowired
   Person frontEndDeveloper;

   @Autowired
   Shepperd shepperd;

   @Override
   public void run(ApplicationArguments args) throws Exception {
      sellPhone.sendMessage();
      log.info(person.toString());
      log.info(frontEndDeveloper.toString());
      log.info(shepperd.toString());
   }
}
