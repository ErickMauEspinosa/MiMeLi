package co.com.mimeli.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import co.com.mimeli.model.entity.BlackList;
import co.com.mimeli.repository.IBlackListRepository;

@RunWith(MockitoJUnitRunner.class)
public class BlackListServiceTest {

	@InjectMocks
	private BlackListService service;

	@Mock
	private IBlackListRepository blackListRepository;

	@Test
	public void saveAll() {
		when(blackListRepository.saveAll(anyListOf(BlackList.class))).thenReturn(returnBlackLists());

		Iterable<BlackList> blackListsResponse = service.saveAll(returnBlackLists());

		assertThat(blackListsResponse).isNotNull();
		assertThat(blackListsResponse).size().isEqualTo(2);
	}

	@Test
	public void findById() {
		when(blackListRepository.findById(anyString())).thenReturn(Optional.of(new BlackList("65.49.22.66")));

		Optional<BlackList> blackListResponse = service.findById("65.49.22.66");

		assertThat(blackListResponse).isPresent();

		assertThat(((BlackList) blackListResponse.get()).getIp()).isNotNull();
		assertThat(((BlackList) blackListResponse.get()).getIp()).isEqualTo("65.49.22.66");
	}

	@Test
	public void existsById() {
		when(blackListRepository.existsById(anyString())).thenReturn(true);

		boolean exists = service.existsById("65.49.22.66");

		assertThat(exists).isNotNull();
		assertThat(exists).isEqualTo(true);
	}

	@Test
	public void findAllById() {
		when(blackListRepository.findAllById(anyListOf(String.class))).thenReturn(returnBlackLists());

		Iterable<BlackList> blackListsResponse = service.findAllById(returnIds());

		assertThat(blackListsResponse).isNotNull();
	}

	@Test
	public void count() {
		when(blackListRepository.count()).thenReturn(1L);

		long count = service.count();

		assertThat(count).isNotNull();
		assertThat(count).isNotNegative();
	}

	@Test
	public void deleteById() {
		service.deleteById("65.49.22.66");

		verify(blackListRepository, times(1)).deleteById("65.49.22.66");
	}

	@Test
	public void delete() {
		BlackList blackList = new BlackList("65.49.22.66");

		service.delete(blackList);

		verify(blackListRepository, times(1)).delete(blackList);
	}

	@Test
	public void deleteAllById() {
		service.deleteAllById(returnIds());

		verify(blackListRepository, times(1)).deleteAllById(returnIds());
	}

	@Test
	public void deleteAllByBlackLists() {
		List<BlackList> list = (List<BlackList>) returnBlackLists();

		service.deleteAll(list);

		verify(blackListRepository, times(1)).deleteAll(list);
	}

	@Test
	public void deleteAll() {
		service.deleteAll();

		verify(blackListRepository, times(1)).deleteAll();
	}

	@Test
	public void save() {
		when(blackListRepository.save(any(BlackList.class))).thenReturn(new BlackList("65.49.22.66"));

		BlackList blackListResponse = service.save(returnBlackList());

		assertThat(blackListResponse).isNotNull();

		assertThat(blackListResponse.getIp()).isNotNull();
		assertThat(blackListResponse.getIp()).isEqualTo("65.49.22.66");
	}

	@Test
	public void findAll() {
		when(blackListRepository.findAll()).thenReturn(returnBlackLists());

		Iterable<BlackList> blackListsResponse = service.findAll();

		assertThat(blackListsResponse).isNotNull();
	}

	private BlackList returnBlackList() {
		return new BlackList("65.49.22.66");
	}

	private Iterable<BlackList> returnBlackLists() {
		BlackList blackListOne = new BlackList("65.49.22.66");
		BlackList blackListTwo = new BlackList("65.49.22.67");

		List<BlackList> list = Arrays.asList(blackListOne, blackListTwo);

		return list;
	}

	private Iterable<String> returnIds() {
		List<String> list = Arrays.asList("65.49.22.66", "65.49.22.67");

		return list;
	}

}
