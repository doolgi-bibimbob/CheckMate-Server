package com.seonlim.mathreview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
//@EntityScan(basePackages = {
//		"com.seonlim.mathreview.user.entity",
//		"com.seonlim.mathreview.problem.entity"
//})
public class MathreviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(MathreviewApplication.class, args);
	}

}
