package co.com.mimeli.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import co.com.mimeli.model.request.CountryInformationRequest;
import co.com.mimeli.model.response.CountryInformationResponse;
import co.com.mimeli.repository.ICountryRepository;

@Service
public class CountryService implements ICountryRepository {

	@Autowired
	RestTemplate template;

	@Override
	public CountryInformationResponse getCountryInformation(CountryInformationRequest countryInformationRequest) {
		Gson gson = new Gson();
		JsonObject result = gson
				.fromJson(
						template.getForObject("http://api.ipapi.com/".concat(countryInformationRequest.getIp())
								.concat("?access_key=ecbf8e62b479066c40adf804df32d0d5"), String.class),
						JsonObject.class);

		CountryInformationResponse countryInfo = new CountryInformationResponse();
		countryInfo.setCode(result.get("country_code").getAsString());
		countryInfo.setCountryName(result.get("country_name").getAsString());

		Gson gsonCurrencies = new Gson();
		JsonObject resultCountriesCurrency = gsonCurrencies.fromJson(template
				.getForObject("https://free.currconv.com/api/v7/countries?apiKey=906aa577080d905adb80", String.class),
				JsonObject.class);

		JsonObject resultCountries = resultCountriesCurrency.getAsJsonObject("results");

		JsonObject resultCountry = resultCountries.getAsJsonObject(countryInfo.getCode());

		JsonElement currencyId = resultCountry.get("currencyId");
		countryInfo.setLocalCurrency(currencyId.getAsString());

		String conversion = currencyId.getAsString().concat("_USD");

		Gson gsonConversion = new Gson();
		JsonObject resultConvert = gsonConversion
				.fromJson(template.getForObject("https://free.currconv.com/api/v7/convert?q=".concat(conversion)
						.concat("&compact=ultra&apiKey=906aa577080d905adb80"), String.class), JsonObject.class);
		countryInfo.setCurrentValue(new BigDecimal(resultConvert.get(conversion).getAsString()));

		return countryInfo;
	}

}
