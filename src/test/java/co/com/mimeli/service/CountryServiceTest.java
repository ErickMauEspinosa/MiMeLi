package co.com.mimeli.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.client.RestTemplate;

import co.com.mimeli.model.request.CountryInformationRequest;
import co.com.mimeli.model.response.CountryInformationResponse;

@RunWith(MockitoJUnitRunner.class)
public class CountryServiceTest {

	@InjectMocks
	private CountryService service;

	@Mock
	RestTemplate template;

	@Test
	public void getCountryInformation() {
		when(template.getForObject("http://api.ipapi.com/65.49.22.66?access_key=ecbf8e62b479066c40adf804df32d0d5",
				String.class)).thenReturn(
						"{\r\n" + 
						"  \"ip\": \"65.49.22.66\",\r\n" + 
						"  \"type\": \"ipv4\",\r\n" + 
						"  \"continent_code\": \"NA\",\r\n" + 
						"  \"continent_name\": \"North America\",\r\n" + 
						"  \"country_code\": \"US\",\r\n" + 
						"  \"country_name\": \"United States\",\r\n" + 
						"  \"region_code\": \"MN\",\r\n" + 
						"  \"region_name\": \"Minnesota\",\r\n" + 
						"  \"city\": \"Minnetonka\",\r\n" + 
						"  \"zip\": \"55364\",\r\n" + 
						"  \"latitude\": 44.93484115600586,\r\n" + 
						"  \"longitude\": -93.66797637939453,\r\n" + 
						"  \"location\": {\r\n" + 
						"    \"geoname_id\": 5037784,\r\n" + 
						"    \"capital\": \"Washington D.C.\",\r\n" + 
						"    \"languages\": [\r\n" + 
						"      {\r\n" + 
						"        \"code\": \"en\",\r\n" + 
						"        \"name\": \"English\",\r\n" + 
						"        \"native\": \"English\"\r\n" + 
						"      }\r\n" + 
						"    ],\r\n" + 
						"    \"country_flag\": \"https://assets.ipstack.com/flags/us.svg\",\r\n" + 
						"    \"country_flag_emoji\": \"🇺🇸\",\r\n" + 
						"    \"country_flag_emoji_unicode\": \"U+1F1FA U+1F1F8\",\r\n" + 
						"    \"calling_code\": \"1\",\r\n" + 
						"    \"is_eu\": false\r\n" + 
						"  }\r\n" + 
						"}");
		
		when(template.getForObject("https://free.currconv.com/api/v7/countries?apiKey=906aa577080d905adb80",
				String.class)).thenReturn(
						"{\r\n" + 
						"  \"results\": {\r\n" + 
						"    \"US\": {\r\n" + 
						"      \"alpha3\": \"USA\",\r\n" + 
						"      \"currencyId\": \"USD\",\r\n" + 
						"      \"currencyName\": \"United States dollar\"\r\n" + 
						"    }\r\n" + 
						"  }\r\n" + 
						"}");
		
		when(template.getForObject("https://free.currconv.com/api/v7/convert?q=USD_USD&compact=ultra&apiKey=906aa577080d905adb80",
				String.class)).thenReturn(
						"{\r\n" + 
						"  \"USD_USD\": \"1\"\r\n" + 
						"}");

		CountryInformationResponse response = service.getCountryInformation(returnCountryInformationRequest());

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

	private CountryInformationRequest returnCountryInformationRequest() {
		return new CountryInformationRequest("65.49.22.66");
	}
}
