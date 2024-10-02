package com.ingeneo.logistica.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.ingeneo.logistica.api.dto.MaritimeLogisticDTO;

public interface IMaritimeLogisticService {

	List<MaritimeLogisticDTO> findAll();
	
	Optional<MaritimeLogisticDTO> findById(Long id);
	
	MaritimeLogisticDTO save(MaritimeLogisticDTO maritimeLogisticDTO);
	
	MaritimeLogisticDTO update(Long id, MaritimeLogisticDTO maritimeLogisticDTO);
	
	void delete(Long id);
}
