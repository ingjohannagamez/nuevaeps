package com.ingeneo.logistica.service.interfaces;

import java.util.List;
import java.util.Optional;

import com.ingeneo.logistica.api.dto.ClientDTO;

public interface IClientService {

	List<ClientDTO> findAll();
	
	Optional<ClientDTO> findById(Long id);
	
	ClientDTO save(ClientDTO clientDTO);
	
	ClientDTO update(Long id, ClientDTO clientDTO);
	
	void delete(Long id);
}
