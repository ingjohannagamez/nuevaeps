package com.ingeneo.logistica.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.ingeneo.logistica.api.dto.ClientDTO;
import com.ingeneo.logistica.api.mapper.ClientMapper;
import com.ingeneo.logistica.model.Client;
import com.ingeneo.logistica.repository.ClientRepository;
import com.ingeneo.logistica.service.interfaces.IClientService;

@Service
public class ClientServiceImpl implements IClientService {

	private final ClientRepository repository;

    public ClientServiceImpl(ClientRepository repository) {
        this.repository = repository;
    }   
    
    public List<ClientDTO> findAll() {
        return repository.findAll().stream()
                         .map(ClientMapper::toDTO)
                         .collect(Collectors.toList());
    }

    public Optional<ClientDTO> findById(Long id) {
        Optional<Client> client = repository.findById(id);
        return client.map(ClientMapper::toDTO);
    }

    public ClientDTO save(ClientDTO clientDTO) {
        // Convierte DTO a entidad
    	Client client = ClientMapper.convertToEntity(clientDTO);

        // Guarda la entidad en la base de datos
    	Client savedEntity = repository.save(client);

        // Convierte la entidad guardada de vuelta a DTO para devolverlo
        return ClientMapper.toDTO(savedEntity);
    }
    
    public ClientDTO update(Long id, ClientDTO clientDTO) {
        return repository.findById(id).map(existingClient -> {
            
        	existingClient.setId(clientDTO.getId());
        	existingClient.setName(clientDTO.getName());

            Client updatedClient = repository.save(existingClient);
            return ClientMapper.toDTO(updatedClient);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Logística del cliente no encontrada"));
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Logística del cliente no encontrada");
        }
        repository.deleteById(id);
    }
    
}
