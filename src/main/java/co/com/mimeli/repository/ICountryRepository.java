package co.com.mimeli.repository;

import org.springframework.stereotype.Repository;

import co.com.mimeli.model.response.CountryInformationResponse;

@Repository
public interface ICountryRepository {
	CountryInformationResponse getCountryInformation(String ip);
}
