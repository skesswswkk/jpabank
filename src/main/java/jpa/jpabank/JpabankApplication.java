package jpa.jpabank;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableBatchProcessing
public class JpabankApplication {

	public static void main(String[] args) {
		SpringApplication.run(JpabankApplication.class, args);
	}

	@Bean
	Hibernate5Module hibernate5Module() {

		Hibernate5Module hibernate5Module = new Hibernate5Module();

		//강제 지연 로딩 설정
//		hibernate5Module.configure(Hibernate5Module.Feature.FORCE_LAZY_LOADING, true);

		return hibernate5Module;
	}
}
