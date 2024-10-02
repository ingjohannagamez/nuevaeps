package com.ingeneo.logistica.api.mapper;

import com.ingeneo.logistica.api.dto.ClientDTO;
import com.ingeneo.logistica.model.Client;

public class ClientMapper {

	public static ClientDTO toDTO(Client client) {
		ClientDTO dto = new ClientDTO();
        
        dto.setId(client.getId());
        dto.setName(client.getName());
        
        return dto;
    }
	
	public static Client convertToEntity(ClientDTO clientDTO) {
		Client client = new Client();
	    
		client.setId(clientDTO.getId());
		client.setName(clientDTO.getName());
	    
	    return client;
	}
}
