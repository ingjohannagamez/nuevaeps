package com.ingeneo.logistica.api.mapper;

import com.ingeneo.logistica.api.dto.ClientDTO;
import com.ingeneo.logistica.api.dto.TruckLogisticDTO;
import com.ingeneo.logistica.model.Client;
import com.ingeneo.logistica.model.TruckLogistic;

public class TruckLogisticMapper {

	public static TruckLogisticDTO toDTO(TruckLogistic truckLogistic) {
        TruckLogisticDTO dto = new TruckLogisticDTO();
        
        dto.setId(truckLogistic.getId());
        dto.setProductType(truckLogistic.getProductType());
        dto.setQuantity(truckLogistic.getQuantity());
        dto.setRegistrationDate(truckLogistic.getRegistrationDate());
        dto.setDeliveryDate(truckLogistic.getDeliveryDate());
        dto.setDeliveryWarehouse(truckLogistic.getDeliveryWarehouse());
        dto.setShippingPrice(truckLogistic.getShippingPrice());
        dto.setShippingPriceGranted(truckLogistic.getShippingPriceGranted());
        dto.setVehiclePlate(truckLogistic.getVehiclePlate());
        dto.setGuideNumber(truckLogistic.getGuideNumber());
        if (truckLogistic.getClient() != null) {
        	ClientDTO client = new ClientDTO();
        	client.setId(truckLogistic.getClient().getId());
        	client.setName(truckLogistic.getClient().getName());
            dto.setClient(client);
        }
        return dto;
    }
	
	public static TruckLogistic convertToEntity(TruckLogisticDTO truckLogisticDTO) {
	    TruckLogistic truckLogistic = new TruckLogistic();
	    truckLogistic.setProductType(truckLogisticDTO.getProductType());
	    truckLogistic.setQuantity(truckLogisticDTO.getQuantity());
	    truckLogistic.setRegistrationDate(truckLogisticDTO.getRegistrationDate());
	    truckLogistic.setDeliveryDate(truckLogisticDTO.getDeliveryDate());
	    truckLogistic.setDeliveryWarehouse(truckLogisticDTO.getDeliveryWarehouse());
	    truckLogistic.setVehiclePlate(truckLogisticDTO.getVehiclePlate());
	    truckLogistic.setGuideNumber(truckLogisticDTO.getGuideNumber());
	    truckLogistic.setShippingPrice(truckLogisticDTO.getShippingPrice());
	    if (truckLogisticDTO.getClient() != null) {
        	Client client = new Client();
        	client.setId(truckLogisticDTO.getClient().getId());
        	truckLogistic.setClient(client);
        }
	    return truckLogistic;
	}
}
