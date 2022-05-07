package co.com.mimeli.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MyRegexTest {

	@InjectMocks
	private MyRegex myRegex;

	@Test
	public void validateIpValid() {
		boolean responde = myRegex.validateIp("65.49.22.66");

		assertThat(responde).isNotNull();
		assertThat(responde).isTrue();
	}

	@Test
	public void validateIpNotValid() {
		boolean responde = myRegex.validateIp("");

		assertThat(responde).isNotNull();
		assertThat(responde).isFalse();
	}
}
