package com.testBankProj.laytin;

import com.testBankProj.laytin.dto.AuthDTO;
import com.testBankProj.laytin.models.Customer;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling

public class LaytinApplication {

	public static void main(String[] args) {
		SpringApplication.run(LaytinApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		modelMapper.addMappings(new PropertyMap<AuthDTO, Customer>() {
			@Override
			protected void configure() {
				skip(destination.getPhoneList());
				skip(destination.getEmailList());
			}
		});
/*		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.LOOSE)
				.setFieldMatchingEnabled(true)
				.setFieldAccessLevel(Configuration.AccessLevel.PRIVATE)
				.setSourceNamingConvention(NamingConventions.JAVABEANS_MUTATOR);*/
		return modelMapper;
	}
}
