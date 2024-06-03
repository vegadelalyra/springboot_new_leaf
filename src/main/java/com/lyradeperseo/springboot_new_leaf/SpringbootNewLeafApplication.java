package com.lyradeperseo.springboot_new_leaf;

import com.lyradeperseo.springboot_new_leaf.telusko.Alien;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringbootNewLeafApplication {

	public static void main(String[] args) {
	 	ConfigurableApplicationContext context = SpringApplication.run(SpringbootNewLeafApplication.class, args);

		Alien obj = context.getBean(Alien.class);

		obj.code();
	}

}
