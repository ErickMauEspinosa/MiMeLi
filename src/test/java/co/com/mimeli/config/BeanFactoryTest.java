package co.com.mimeli.config;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import co.com.mimeli.util.MyRegex;

@RunWith(MockitoJUnitRunner.class)
public class BeanFactoryTest {

	@InjectMocks
	private BeanFactory beanFactory;

	@Test
	public void template() {
		RestTemplate template = beanFactory.template();

		assertThat(template).isNotNull();
	}

	@Test
	public void regex() {
		MyRegex regex = beanFactory.regex();

		assertThat(regex).isNotNull();
	}

}
