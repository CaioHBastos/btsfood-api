package br.com.btstech.btsfoodapi;

import br.com.btstech.btsfoodapi.infrastructure.repository.CustomJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = CustomJpaRepositoryImpl.class)
public class BtsfoodApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BtsfoodApiApplication.class, args);
	}

}
