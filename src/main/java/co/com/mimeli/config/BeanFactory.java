package co.com.mimeli.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import co.com.mimeli.util.MyRegex;

@Component
public class BeanFactory {

	@Bean
	public RestTemplate template() {
		return new RestTemplate();
	}

	@Bean
	public MyRegex regex() {
		return new MyRegex();
	}
}
