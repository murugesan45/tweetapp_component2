package com.tweetapp.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
public class SwaggerConfig {
	

	@Bean
	public Docket swagger(){
		return new Docket(DocumentationType.SWAGGER_2).select().paths(PathSelectors.ant("/api/**")).build().apiInfo(new ApiInfo("TweetApp", "Api for Tweet App", "1.0", "localhost:8081", new Contact("Murugesan", "url", "http://github.com"), "hello", "world", Collections.emptyList()));		
	}

}
