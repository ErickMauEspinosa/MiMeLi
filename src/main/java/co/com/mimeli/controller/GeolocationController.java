package co.com.mimeli.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.mimeli.model.entity.BlackList;
import co.com.mimeli.model.request.BlackListRequest;
import co.com.mimeli.model.request.CountryInformationRequest;
import co.com.mimeli.model.response.BlackListResponse;
import co.com.mimeli.model.response.CountryInformationResponse;
import co.com.mimeli.model.Error;
import co.com.mimeli.service.BlackListService;
import co.com.mimeli.service.CountryService;
import co.com.mimeli.util.Constants;

@RestController
@RequestMapping("/api/v1")
public class GeolocationController {

	@Autowired
	private CountryService countryService;

	@Autowired
	private BlackListService blackListService;

	@GetMapping("/blacklist")
	public ResponseEntity<?> list() {
		Iterable<?> ipList = null;

		try {
			ipList = blackListService.findAll();
		} catch (Exception e) {
			return new ResponseEntity<Error>(new Error(Constants.CODE_INTERNAL_SERVER_ERROR, Constants.MESSAGE_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(ipList, HttpStatus.OK);
	}

	@PostMapping("/blacklist")
	public ResponseEntity<?> saveBlackList(@RequestBody BlackListRequest blackListRequest) {
		try {
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
	public ResponseEntity<?> getCountryInformation(@RequestBody CountryInformationRequest countryInformationRequest) {
		try {
			if (blackListService.existsById(countryInformationRequest.getIp())) {
				return new ResponseEntity<Error>(new Error(Constants.CODE_FORBIDDEN, Constants.IP_NOT_AVAILABLE),
						HttpStatus.FORBIDDEN);
			}

			return new ResponseEntity<CountryInformationResponse>(
					countryService.getCountryInformation(countryInformationRequest), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Error>(new Error(Constants.CODE_INTERNAL_SERVER_ERROR, Constants.MESSAGE_ERROR),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
