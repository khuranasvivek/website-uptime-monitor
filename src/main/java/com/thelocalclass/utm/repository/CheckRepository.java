package com.thelocalclass.utm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.thelocalclass.utm.entity.WebCheck;

public interface CheckRepository extends CrudRepository<WebCheck, Integer> {
	
	@Override
    List<WebCheck> findAll();
	List<WebCheck> findByName(String name);
	List<WebCheck> findByNameIgnoreCaseContaining(String name);
	Optional<WebCheck> findByUrlIgnoreCase(String url);
	List<WebCheck> findByFrequencyAndUnitIgnoreCase(int frequency, String unit);
	Optional<WebCheck> findByUrl(String url);
}
