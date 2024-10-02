package com.ingeneo.logistica.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.ingeneo.logistica.api.dto.TruckLogisticDTO;

public interface ITruckLogisticService {

	List<TruckLogisticDTO> findAll();
	
	Optional<TruckLogisticDTO> findById(Long id);
	
	TruckLogisticDTO save(TruckLogisticDTO truckLogisticDTO);
	
	TruckLogisticDTO update(Long id, TruckLogisticDTO truckLogisticDTO);
	
	void delete(Long id);
}
