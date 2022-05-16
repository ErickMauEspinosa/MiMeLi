package co.com.mimeli.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import co.com.mimeli.model.response.CountryInformationResponse;
import co.com.mimeli.repository.ICountryRepository;

@Service
public class CountryService implements ICountryRepository {

	@Autowired
	private RestTemplate template;

	@Value("${endpoint-service.ip-service.url}")
	private String ipInformationApi;

	@Value("${endpoint-service.country-service.url}")
	private String countriesApi;

	@Value("${endpoint-service.currency-conversion-service.url}")
	private String conversionCurrencyApi;

	@Override
	@Cacheable(value = "country-cache", key = "'CountryCache'+#ip")
	public CountryInformationResponse getCountryInformation(String ip) {
		JsonObject result = getJsonObjectByUrl((String.format(ipInformationApi, ip)));

		JsonObject resultCountriesCurrency = getJsonObjectByUrl(countriesApi);
		JsonObject resultCountries = resultCountriesCurrency.getAsJsonObject("results");
		JsonObject resultCountry = resultCountries.getAsJsonObject(result.get("country_code").getAsString());

		JsonElement currencyId = resultCountry.get("currencyId");
		String conversion = String.format("USD_%s", currencyId.getAsString());
		JsonObject resultConvert = getJsonObjectByUrl(String.format(conversionCurrencyApi, conversion));

		CountryInformationResponse countryInfo = new CountryInformationResponse();
		countryInfo.setCode(result.get("country_code").getAsString());
		countryInfo.setCountryName(result.get("country_name").getAsString());
		countryInfo.setLocalCurrency(currencyId.getAsString());
		countryInfo.setCurrentValue(new BigDecimal(resultConvert.get(conversion).getAsString()));

		return countryInfo;
	}

	private JsonObject getJsonObjectByUrl(String url) {
		Gson gson = new Gson();
		return gson.fromJson(template.getForObject(url, String.class), JsonObject.class);
	}

}
