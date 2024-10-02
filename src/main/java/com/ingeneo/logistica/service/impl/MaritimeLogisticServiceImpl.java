package com.ingeneo.logistica.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ingeneo.logistica.api.dto.MaritimeLogisticDTO;
import com.ingeneo.logistica.api.mapper.MaritimeLogisticMapper;
import com.ingeneo.logistica.core.util.CalculateShippingPrice;
import com.ingeneo.logistica.model.MaritimeLogistic;
import com.ingeneo.logistica.repository.MaritimeLogisticRepository;
import com.ingeneo.logistica.service.interfaces.IMaritimeLogisticService;

@Service
public class MaritimeLogisticServiceImpl implements IMaritimeLogisticService {

	private final MaritimeLogisticRepository repository;

    public MaritimeLogisticServiceImpl(MaritimeLogisticRepository repository) {
        this.repository = repository;
    }   
    
    public List<MaritimeLogisticDTO> findAll() {
        return repository.findAll().stream()
                         .map(MaritimeLogisticMapper::toDTO)
                         .collect(Collectors.toList());
    }

    public Optional<MaritimeLogisticDTO> findById(Long id) {
        Optional<MaritimeLogistic> maritimeLogistic = repository.findById(id);
        return maritimeLogistic.map(MaritimeLogisticMapper::toDTO);
    }

    public MaritimeLogisticDTO save(MaritimeLogisticDTO maritimeLogisticDTO) {
        // Convierte DTO a entidad
    	MaritimeLogistic maritimeLogistic = MaritimeLogisticMapper.convertToEntity(maritimeLogisticDTO);

        // Calcula el nuevo precio de envío con el descuento aplicado
        double newShippingPrice = CalculateShippingPrice.calculateMaritimeShippingPrice(maritimeLogistic);
        maritimeLogistic.setShippingPriceGranted(newShippingPrice);

        // Guarda la entidad en la base de datos
        MaritimeLogistic savedEntity = repository.save(maritimeLogistic);

        // Convierte la entidad guardada de vuelta a DTO para devolverlo
        return MaritimeLogisticMapper.toDTO(savedEntity);
    }
    
    public MaritimeLogisticDTO update(Long id, MaritimeLogisticDTO maritimeLogisticDTO) {
        return repository.findById(id).map(existingMaritimeLogistic -> {
            
        	existingMaritimeLogistic.setId(maritimeLogisticDTO.getId());
        	existingMaritimeLogistic.setProductType(maritimeLogisticDTO.getProductType());
        	existingMaritimeLogistic.setQuantity(maritimeLogisticDTO.getQuantity());
        	existingMaritimeLogistic.setRegistrationDate(maritimeLogisticDTO.getRegistrationDate());
        	existingMaritimeLogistic.setDeliveryDate(maritimeLogisticDTO.getDeliveryDate());
        	existingMaritimeLogistic.setDeliveryPort(maritimeLogisticDTO.getDeliveryPort());
        	existingMaritimeLogistic.setShippingPrice(maritimeLogisticDTO.getShippingPrice());
        	existingMaritimeLogistic.setFleetNumber(maritimeLogisticDTO.getFleetNumber());
        	existingMaritimeLogistic.setGuideNumber(maritimeLogisticDTO.getGuideNumber());
        	existingMaritimeLogistic.setShippingPriceGranted(CalculateShippingPrice.calculateMaritimeShippingPrice(existingMaritimeLogistic));

        	MaritimeLogistic updatedMaritimeLogistic = repository.save(existingMaritimeLogistic);
            return MaritimeLogisticMapper.toDTO(updatedMaritimeLogistic);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logística Maritima no encontrada"));
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logística de camión no encontrada");
        }
        repository.deleteById(id);
    }
    
}
