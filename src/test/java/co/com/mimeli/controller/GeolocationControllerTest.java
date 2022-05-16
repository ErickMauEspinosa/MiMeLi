package co.com.mimeli.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import com.google.gson.JsonSyntaxException;

import co.com.mimeli.model.request.BlackListRequest;
import co.com.mimeli.model.response.BlackListResponse;
import co.com.mimeli.model.response.CountryInformationResponse;
import co.com.mimeli.repository.IBlackListRepository;
import co.com.mimeli.service.BlackListService;
import co.com.mimeli.service.CountryService;
import co.com.mimeli.util.Constants;
import co.com.mimeli.util.MyRegex;
import co.com.mimeli.model.Error;
import co.com.mimeli.model.entity.BlackList;

@RunWith(MockitoJUnitRunner.class)
public class GeolocationControllerTest {

	@InjectMocks
	private GeolocationController controller;

	@Mock
	private CountryService countryService;

	@Mock
	private IBlackListRepository blackListRepository;

	@Mock
	private BlackListService blackListService;

	@Mock
	private MyRegex regex;

	@Test
	public void listSuccessful() {
		when(blackListService.findAll()).thenReturn(returnBlackLists());

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) controller.list();

		assertThat(responseEntity).isNotNull();
		assertThat(((Iterable<?>) responseEntity.getBody())).isNotNull();
	}

	@Test
	public void listException() {
		when(blackListService.findAll()).thenThrow(new NullPointerException());

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) controller.list();

		assertThat(responseEntity).isNotNull();
		assertThat(((Error) responseEntity.getBody())).isNotNull();

		assertThat(((Error) responseEntity.getBody()).getCode()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getCode()).isEqualTo(Constants.CODE_INTERNAL_SERVER_ERROR);

		assertThat(((Error) responseEntity.getBody()).getMessage()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getMessage()).isEqualTo(Constants.MESSAGE_ERROR);
	}

	@Test
	public void saveBlackListSuccessful() {
		when(regex.validateIp(anyString())).thenReturn(true);
		when(blackListService.save(any(BlackList.class))).thenReturn(new BlackList("65.49.22.66"));

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) controller.saveBlackList(returnBlackListRequest());

		assertThat(responseEntity).isNotNull();
		assertThat(((BlackListResponse) responseEntity.getBody())).isNotNull();

		assertThat(((BlackListResponse) responseEntity.getBody()).getIp()).isNotNull();
		assertThat(((BlackListResponse) responseEntity.getBody()).getIp()).isEqualTo("65.49.22.66");

		assertThat(((BlackListResponse) responseEntity.getBody()).getMessage()).isNotNull();
		assertThat(((BlackListResponse) responseEntity.getBody()).getMessage())
				.isEqualTo(Constants.IP_SUCCESSFULLY_REGISTERED);
	}

	@Test
	public void saveBlackListIpNotFormat() {
		when(regex.validateIp(anyString())).thenReturn(false);

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) controller.saveBlackList(returnBlackListRequest());

		assertThat(responseEntity).isNotNull();
		assertThat(((Error) responseEntity.getBody())).isNotNull();

		assertThat(((Error) responseEntity.getBody()).getCode()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getCode()).isEqualTo(Constants.CODE_BAD_REQUEST);

		assertThat(((Error) responseEntity.getBody()).getMessage()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getMessage()).isEqualTo(Constants.IP_NOT_FORMAT);
	}

	@Test
	public void saveBlackListIpExists() {
		when(regex.validateIp(anyString())).thenReturn(true);
		when(blackListService.existsById(any(String.class))).thenReturn(true);

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) controller.saveBlackList(returnBlackListRequest());

		assertThat(responseEntity).isNotNull();
		assertThat(((Error) responseEntity.getBody())).isNotNull();

		assertThat(((Error) responseEntity.getBody()).getCode()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getCode()).isEqualTo(Constants.CODE_BAD_REQUEST);

		assertThat(((Error) responseEntity.getBody()).getMessage()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getMessage()).isEqualTo(Constants.IP_REGISTERED_IN_BLACK_LIST);
	}

	@Test
	public void saveBlackListIllegalArgumentException() {
		when(regex.validateIp(anyString())).thenReturn(true);
		when(blackListService.save(any(BlackList.class))).thenThrow(new IllegalArgumentException());

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) controller.saveBlackList(returnBlackListRequest());

		assertThat(responseEntity).isNotNull();
		assertThat(((Error) responseEntity.getBody())).isNotNull();

		assertThat(((Error) responseEntity.getBody()).getCode()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getCode()).isEqualTo(Constants.CODE_NOT_FOUND);

		assertThat(((Error) responseEntity.getBody()).getMessage()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getMessage()).isEqualTo(Constants.MESSAGE_ERROR);
	}

	@Test
	public void saveBlackListException() {
		when(regex.validateIp(anyString())).thenReturn(true);
		when(blackListService.save(any(BlackList.class))).thenThrow(new NullPointerException());

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) controller.saveBlackList(returnBlackListRequest());

		assertThat(responseEntity).isNotNull();
		assertThat(((Error) responseEntity.getBody())).isNotNull();

		assertThat(((Error) responseEntity.getBody()).getCode()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getCode()).isEqualTo(Constants.CODE_INTERNAL_SERVER_ERROR);

		assertThat(((Error) responseEntity.getBody()).getMessage()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getMessage()).isEqualTo(Constants.MESSAGE_ERROR);
	}

	@Test
	public void getCountryInformationSuccessful() {
		when(regex.validateIp(anyString())).thenReturn(true);
		when(countryService.getCountryInformation(anyString()))
				.thenReturn(new CountryInformationResponse("US", "United States", "USD", BigDecimal.ONE));

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) controller.getCountryInformation(returnIp());

		assertThat(responseEntity).isNotNull();
		assertThat(((CountryInformationResponse) responseEntity.getBody())).isNotNull();

		assertThat(((CountryInformationResponse) responseEntity.getBody()).getCode()).isNotNull();
		assertThat(((CountryInformationResponse) responseEntity.getBody()).getCode()).isEqualTo("US");

		assertThat(((CountryInformationResponse) responseEntity.getBody()).getCountryName()).isNotNull();
		assertThat(((CountryInformationResponse) responseEntity.getBody()).getCountryName()).isEqualTo("United States");

		assertThat(((CountryInformationResponse) responseEntity.getBody()).getLocalCurrency()).isNotNull();
		assertThat(((CountryInformationResponse) responseEntity.getBody()).getLocalCurrency()).isEqualTo("USD");

		assertThat(((CountryInformationResponse) responseEntity.getBody()).getCurrentValue()).isNotNull();
		assertThat(((CountryInformationResponse) responseEntity.getBody()).getCurrentValue()).isEqualTo(BigDecimal.ONE);
	}

	@Test
	public void getCountryInformationIpNotFormat() {
		when(regex.validateIp(anyString())).thenReturn(false);

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) controller.getCountryInformation(returnIp());

		assertThat(responseEntity).isNotNull();
		assertThat(((Error) responseEntity.getBody())).isNotNull();

		assertThat(((Error) responseEntity.getBody()).getCode()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getCode()).isEqualTo(Constants.CODE_BAD_REQUEST);

		assertThat(((Error) responseEntity.getBody()).getMessage()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getMessage()).isEqualTo(Constants.IP_NOT_FORMAT);
	}

	@Test
	public void getCountryInformationIpExistsInBlackList() {
		when(regex.validateIp(anyString())).thenReturn(true);
		when(blackListService.existsById(any(String.class))).thenReturn(true);

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) controller.getCountryInformation(returnIp());

		assertThat(responseEntity).isNotNull();
		assertThat(((Error) responseEntity.getBody())).isNotNull();

		assertThat(((Error) responseEntity.getBody()).getCode()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getCode()).isEqualTo(Constants.CODE_FORBIDDEN);

		assertThat(((Error) responseEntity.getBody()).getMessage()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getMessage()).isEqualTo(Constants.IP_NOT_AVAILABLE);
	}

	@Test
	public void getCountryInformationJsonSyntaxException() {
		when(regex.validateIp(anyString())).thenReturn(true);
		when(countryService.getCountryInformation(anyString()))
				.thenThrow(new JsonSyntaxException("Could not convert to JSON format"));

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) controller.getCountryInformation(returnIp());

		assertThat(responseEntity).isNotNull();
		assertThat(((Error) responseEntity.getBody())).isNotNull();

		assertThat(((Error) responseEntity.getBody()).getCode()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getCode()).isEqualTo(Constants.CODE_SERVICE_UNAVAILABLE);

		assertThat(((Error) responseEntity.getBody()).getMessage()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getMessage()).isEqualTo(Constants.MESSAGE_ERROR);
	}
	
	@Test
	public void countryFallback() {
		when(regex.validateIp(anyString())).thenReturn(true);
		when(countryService.getCountryInformation(anyString()))
				.thenThrow(new JsonSyntaxException("Could not convert to JSON format"));
		
		Exception e = null;
		ResponseEntity<?> responseEntity = (ResponseEntity<?>) controller.countryFallback(e);

		assertThat(responseEntity).isNotNull();
		assertThat(((Error) responseEntity.getBody())).isNotNull();

		assertThat(((Error) responseEntity.getBody()).getCode()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getCode()).isEqualTo(Constants.CODE_BAD_GATEWAY);

		assertThat(((Error) responseEntity.getBody()).getMessage()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getMessage()).isEqualTo(Constants.COUNTRY_SERVICE_DOWN);
	}

	private BlackListRequest returnBlackListRequest() {
		return new BlackListRequest("65.49.22.66");
	}

	private String returnIp() {
		return "65.49.22.66";
	}

	private Iterable<BlackList> returnBlackLists() {
		BlackList blackListOne = new BlackList("65.49.22.66");
		BlackList blackListTwo = new BlackList("65.49.22.67");

		List<BlackList> intList = Arrays.asList(blackListOne, blackListTwo);

		return intList;
	}
}
