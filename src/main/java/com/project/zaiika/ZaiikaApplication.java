package com.project.zaiika;

import com.project.zaiika.config.RsaKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(RsaKeyProperties.class)
@SpringBootApplication
public class ZaiikaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZaiikaApplication.class, args);
	}

}
