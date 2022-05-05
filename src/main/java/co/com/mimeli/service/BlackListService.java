package co.com.mimeli.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.mimeli.model.entity.BlackList;
import co.com.mimeli.repository.IBlackListRepository;

@Service
public class BlackListService implements IBlackListRepository {

	@Autowired
	private IBlackListRepository blackListRepository;

	@Override
	public <S extends BlackList> Iterable<S> saveAll(Iterable<S> entities) {
		return blackListRepository.saveAll(entities);
	}

	@Override
	public Optional<BlackList> findById(String id) {
		return blackListRepository.findById(id);
	}

	@Override
	public boolean existsById(String id) {
		return blackListRepository.existsById(id);
	}

	@Override
	public Iterable<BlackList> findAllById(Iterable<String> ids) {
		return blackListRepository.findAllById(ids);
	}

	@Override
	public long count() {
		return blackListRepository.count();
	}

	@Override
	public void deleteById(String id) {
		blackListRepository.deleteById(id);
	}

	@Override
	public void delete(BlackList entity) {
		blackListRepository.delete(entity);
	}

	@Override
	public void deleteAllById(Iterable<? extends String> ids) {
		blackListRepository.deleteAllById(ids);
	}

	@Override
	public void deleteAll(Iterable<? extends BlackList> entities) {
		blackListRepository.deleteAll(entities);
	}

	@Override
	public void deleteAll() {
		blackListRepository.deleteAll();
	}

	@Override
	public <S extends BlackList> S save(S entity) {
		return blackListRepository.save(entity);
	}

	@Override
	public Iterable<BlackList> findAll() {
		return blackListRepository.findAll();
	}

}
