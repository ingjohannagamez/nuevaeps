package com.ingeneo.logistica.api.mapper;

import com.ingeneo.logistica.api.dto.ClientDTO;
import com.ingeneo.logistica.api.dto.MaritimeLogisticDTO;
import com.ingeneo.logistica.model.Client;
import com.ingeneo.logistica.model.MaritimeLogistic;

public class MaritimeLogisticMapper {

	public static MaritimeLogisticDTO toDTO(MaritimeLogistic maritimeLogistic) {
		MaritimeLogisticDTO dto = new MaritimeLogisticDTO();
        
        dto.setId(maritimeLogistic.getId());
        dto.setProductType(maritimeLogistic.getProductType());
        dto.setQuantity(maritimeLogistic.getQuantity());
        dto.setRegistrationDate(maritimeLogistic.getRegistrationDate());
        dto.setDeliveryDate(maritimeLogistic.getDeliveryDate());
        dto.setDeliveryPort(maritimeLogistic.getDeliveryPort());
        dto.setShippingPrice(maritimeLogistic.getShippingPrice());
        dto.setShippingPriceGranted(maritimeLogistic.getShippingPriceGranted());
        dto.setFleetNumber(maritimeLogistic.getFleetNumber());
        dto.setGuideNumber(maritimeLogistic.getGuideNumber());
        if (maritimeLogistic.getClient() != null) {
        	ClientDTO client = new ClientDTO();
        	client.setId(maritimeLogistic.getClient().getId());
        	client.setName(maritimeLogistic.getClient().getName());
            dto.setClient(client);
        }
        return dto;
    }
	
	public static MaritimeLogistic convertToEntity(MaritimeLogisticDTO maritimeLogisticDTO) {
		MaritimeLogistic maritimeLogistic = new MaritimeLogistic();
		maritimeLogistic.setProductType(maritimeLogisticDTO.getProductType());
		maritimeLogistic.setQuantity(maritimeLogisticDTO.getQuantity());
		maritimeLogistic.setRegistrationDate(maritimeLogisticDTO.getRegistrationDate());
		maritimeLogistic.setDeliveryDate(maritimeLogisticDTO.getDeliveryDate());
		maritimeLogistic.setDeliveryPort(maritimeLogisticDTO.getDeliveryPort());
		maritimeLogistic.setFleetNumber(maritimeLogisticDTO.getFleetNumber());
		maritimeLogistic.setGuideNumber(maritimeLogisticDTO.getGuideNumber());
		maritimeLogistic.setShippingPrice(maritimeLogisticDTO.getShippingPrice());
	    if (maritimeLogisticDTO.getClient() != null) {
        	Client client = new Client();
        	client.setId(maritimeLogisticDTO.getClient().getId());
        	maritimeLogistic.setClient(client);
        }
	    return maritimeLogistic;
	}
}
