package co.com.mimeli.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import co.com.mimeli.model.request.BlackListRequest;
import co.com.mimeli.model.response.BlackListResponse;
import co.com.mimeli.repository.IBlackListRepository;
import co.com.mimeli.service.BlackListService;
import co.com.mimeli.service.CountryService;
import co.com.mimeli.util.Constants;
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

	@Test
	public void saveBlackListSuccessful() {
		when(blackListService.save(any(BlackList.class))).thenReturn(new BlackList("179.183.250.222"));

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) controller.saveBlackList(returnBlackListRequest());

		assertThat(responseEntity).isNotNull();
		assertThat(((BlackListResponse) responseEntity.getBody())).isNotNull();
		assertThat(((BlackListResponse) responseEntity.getBody()).getIp()).isNotNull();
		assertThat(((BlackListResponse) responseEntity.getBody()).getIp()).isEqualTo("179.183.250.222");

		assertThat(((BlackListResponse) responseEntity.getBody()).getMessage()).isNotNull();
		assertThat(((BlackListResponse) responseEntity.getBody()).getMessage())
				.isEqualTo(Constants.IP_SUCCESSFULLY_REGISTERED);
	}

	@Test
	public void saveBlackListIpExists() {
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
		when(blackListService.save(any(BlackList.class))).thenThrow(new NullPointerException());

		ResponseEntity<?> responseEntity = (ResponseEntity<?>) controller.saveBlackList(returnBlackListRequest());

		assertThat(responseEntity).isNotNull();
		assertThat(((Error) responseEntity.getBody())).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getCode()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getCode()).isEqualTo(Constants.CODE_INTERNAL_SERVER_ERROR);

		assertThat(((Error) responseEntity.getBody()).getMessage()).isNotNull();
		assertThat(((Error) responseEntity.getBody()).getMessage()).isEqualTo(Constants.MESSAGE_ERROR);
	}

	private BlackListRequest returnBlackListRequest() {
		return new BlackListRequest("179.183.250.222");
	}
}
