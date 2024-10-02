package com.nuevaeps.contrato.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.nuevaeps.contrato.api.dto.ContractDTO;
import com.nuevaeps.contrato.api.mapper.ContractMapper;
import com.nuevaeps.contrato.model.Contract;
import com.nuevaeps.contrato.repository.ContractRepository;
import com.nuevaeps.contrato.service.interfaces.IContractService;

@Service
public class ContractServiceImpl implements IContractService {

	private final ContractRepository repository;

    public ContractServiceImpl(ContractRepository repository) {
        this.repository = repository;
    }   
    
    public Page<ContractDTO> findAll(Pageable pageable) {
        // Usar Page.map() para convertir los elementos a ContractDTO
        return repository.findAll(pageable)
                         .map(ContractMapper::toDTO); // Garantiza que el Page<Contract> se convierte a Page<ContractDTO>
    }

    public Optional<ContractDTO> findById(Long id) {
        Optional<Contract> client = repository.findById(id);
        return client.map(ContractMapper::toDTO);
    }

    public ContractDTO save(ContractDTO clientDTO) {
        // Convierte DTO a entidad
    	Contract client = ContractMapper.convertToEntity(clientDTO);

        // Guarda la entidad en la base de datos
    	Contract savedEntity = repository.save(client);

        // Convierte la entidad guardada de vuelta a DTO para devolverlo
        return ContractMapper.toDTO(savedEntity);
    }
    
    public ContractDTO update(Long id, ContractDTO clientDTO) {
        return repository.findById(id).map(existingClient -> {
            
        	existingClient.setId(clientDTO.getId());
        	existingClient.setModalidad(clientDTO.getModalidad());
            existingClient.setNumero(clientDTO.getNumero());
            existingClient.setRegimen(clientDTO.getRegimen());
            existingClient.setArchivo(clientDTO.getArchivo());

            Contract updatedClient = repository.save(existingClient);
            return ContractMapper.toDTO(updatedClient);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrato no encontrado"));
    }
    
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrato no encontrado");
        }
        repository.deleteById(id);
    }
    
}
