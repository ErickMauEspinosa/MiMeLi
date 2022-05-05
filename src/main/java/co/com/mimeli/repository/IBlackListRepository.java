package co.com.mimeli.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import co.com.mimeli.model.entity.BlackList;

@Repository
public interface IBlackListRepository extends CrudRepository<BlackList, String> {

}
