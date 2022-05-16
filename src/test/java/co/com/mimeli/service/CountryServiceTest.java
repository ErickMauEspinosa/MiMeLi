package co.com.mimeli.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import co.com.mimeli.model.response.CountryInformationResponse;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration
public class CountryServiceTest {

	private final String IP_INFORMATION_API = "IP_INFORMATION_API_URL";
	private final String COUNTRIES_API = "COUNTRIES_API_URL";
	private final String CONVERSION_CURRENCY_API = "CONVERSION_CURRENCY_API_URL";

	@InjectMocks
	private CountryService service;

	@Mock
	RestTemplate template;

	@Test
	public void getCountryInformation() {
		ReflectionTestUtils.setField(service, "ipInformationApi", IP_INFORMATION_API);
		ReflectionTestUtils.setField(service, "countriesApi", COUNTRIES_API);
		ReflectionTestUtils.setField(service, "conversionCurrencyApi", CONVERSION_CURRENCY_API);

		when(template.getForObject(IP_INFORMATION_API, String.class)).thenReturn("{\r\n"
				+ "  \"ip\": \"65.49.22.66\",\r\n" + "  \"type\": \"ipv4\",\r\n" + "  \"continent_code\": \"NA\",\r\n"
				+ "  \"continent_name\": \"North America\",\r\n" + "  \"country_code\": \"US\",\r\n"
				+ "  \"country_name\": \"United States\",\r\n" + "  \"region_code\": \"MN\",\r\n"
				+ "  \"region_name\": \"Minnesota\",\r\n" + "  \"city\": \"Minnetonka\",\r\n"
				+ "  \"zip\": \"55364\",\r\n" + "  \"latitude\": 44.93484115600586,\r\n"
				+ "  \"longitude\": -93.66797637939453,\r\n" + "  \"location\": {\r\n"
				+ "    \"geoname_id\": 5037784,\r\n" + "    \"capital\": \"Washington D.C.\",\r\n"
				+ "    \"languages\": [\r\n" + "      {\r\n" + "        \"code\": \"en\",\r\n"
				+ "        \"name\": \"English\",\r\n" + "        \"native\": \"English\"\r\n" + "      }\r\n"
				+ "    ],\r\n" + "    \"country_flag\": \"https://assets.ipstack.com/flags/us.svg\",\r\n"
				+ "    \"country_flag_emoji\": \"🇺🇸\",\r\n"
				+ "    \"country_flag_emoji_unicode\": \"U+1F1FA U+1F1F8\",\r\n" + "    \"calling_code\": \"1\",\r\n"
				+ "    \"is_eu\": false\r\n" + "  }\r\n" + "}");

		when(template.getForObject(COUNTRIES_API, String.class)).thenReturn("{\r\n" + "  \"results\": {\r\n"
				+ "    \"US\": {\r\n" + "      \"alpha3\": \"USA\",\r\n" + "      \"currencyId\": \"USD\",\r\n"
				+ "      \"currencyName\": \"United States dollar\"\r\n" + "    }\r\n" + "  }\r\n" + "}");

		when(template.getForObject(CONVERSION_CURRENCY_API, String.class))
				.thenReturn("{\r\n" + "  \"USD_USD\": \"1\"\r\n" + "}");

		CountryInformationResponse response = service.getCountryInformation(returnIp());

		assertThat(response).isNotNull();

		assertThat(response.getCode()).isNotNull();
		assertThat(response.getCode()).isEqualTo("US");

		assertThat(response.getCountryName()).isNotNull();
		assertThat(response.getCountryName()).isEqualTo("United States");

		assertThat(response.getLocalCurrency()).isNotNull();
		assertThat(response.getLocalCurrency()).isEqualTo("USD");

		assertThat(response.getCurrentValue()).isNotNull();
		assertThat(response.getCurrentValue()).isEqualTo(BigDecimal.ONE);
	}

	private String returnIp() {
		return "65.49.22.66";
	}
}
