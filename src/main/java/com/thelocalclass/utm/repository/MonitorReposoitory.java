package com.thelocalclass.utm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.thelocalclass.utm.entity.Monitor;

public interface MonitorReposoitory extends CrudRepository<Monitor, Integer> {
	
	public Optional<Monitor> findTop1ByCheckIdOrderByIdDesc(Integer checkId);
	public List<Monitor> findTop100ByCheckIdAndStatusOrderByIdDesc(Integer checkId, String status);
}
