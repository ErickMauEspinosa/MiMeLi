package co.com.mimeli.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonSyntaxException;

import co.com.mimeli.model.entity.BlackList;
import co.com.mimeli.model.request.BlackListRequest;
import co.com.mimeli.model.response.BlackListResponse;
import co.com.mimeli.model.response.CountryInformationResponse;
import co.com.mimeli.model.Error;
import co.com.mimeli.service.BlackListService;
import co.com.mimeli.service.CountryService;
import co.com.mimeli.util.Constants;
import co.com.mimeli.util.MyRegex;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

@RestController
@RequestMapping("/api/v1")
public class GeolocationController {

	@Autowired
	private CountryService countryService;

	@Autowired
	private BlackListService blackListService;

	@Autowired
	private MyRegex myRegex;

	@GetMapping("/blacklist")
	public ResponseEntity<?> list() {
		try {
			Iterable<?> ipList = blackListService.findAll();
			return new ResponseEntity<>(ipList, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Error>(new Error(Constants.CODE_INTERNAL_SERVER_ERROR, Constants.MESSAGE_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/blacklist")
	public ResponseEntity<?> saveBlackList(@Valid @RequestBody BlackListRequest blackListRequest) {
		try {
			if (!myRegex.validateIp(blackListRequest.getIp())) {
				return new ResponseEntity<Error>(new Error(Constants.CODE_BAD_REQUEST, Constants.IP_NOT_FORMAT),
						HttpStatus.BAD_REQUEST);
			}

			if (blackListService.existsById(blackListRequest.getIp())) {
				return new ResponseEntity<Error>(
						new Error(Constants.CODE_BAD_REQUEST, Constants.IP_REGISTERED_IN_BLACK_LIST),
						HttpStatus.BAD_REQUEST);
			}
			BlackList blackList = new BlackList(blackListRequest.getIp());
			BlackList blackListResponse = blackListService.save(blackList);

			BlackListResponse response = new BlackListResponse(blackListResponse.getIp(),
					Constants.IP_SUCCESSFULLY_REGISTERED);
			return new ResponseEntity<BlackListResponse>(response, HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<Error>(new Error(Constants.CODE_NOT_FOUND, Constants.MESSAGE_ERROR),
					HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<Error>(new Error(Constants.CODE_INTERNAL_SERVER_ERROR, Constants.MESSAGE_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/country-info")
	@CircuitBreaker(name = "countryService", fallbackMethod = "countryFallback")
	public ResponseEntity<?> getCountryInformation(@RequestParam(required = true) String ip) {
		try {
			if (!myRegex.validateIp(ip)) {
				return new ResponseEntity<Error>(new Error(Constants.CODE_BAD_REQUEST, Constants.IP_NOT_FORMAT),
						HttpStatus.BAD_REQUEST);
			}

			if (blackListService.existsById(ip)) {
				return new ResponseEntity<Error>(new Error(Constants.CODE_FORBIDDEN, Constants.IP_NOT_AVAILABLE),
						HttpStatus.FORBIDDEN);
			}

			return new ResponseEntity<CountryInformationResponse>(countryService.getCountryInformation(ip),
					HttpStatus.OK);
		} catch (JsonSyntaxException e) {
			return new ResponseEntity<Error>(new Error(Constants.CODE_SERVICE_UNAVAILABLE, Constants.MESSAGE_ERROR),
					HttpStatus.SERVICE_UNAVAILABLE);
		}
	}

	public ResponseEntity<?> countryFallback(Exception e) {
		return new ResponseEntity<Error>(new Error(Constants.CODE_BAD_GATEWAY, Constants.COUNTRY_SERVICE_DOWN),
				HttpStatus.BAD_GATEWAY);
	}
}
