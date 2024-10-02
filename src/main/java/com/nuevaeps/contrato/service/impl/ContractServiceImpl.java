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
    private final ContractMapper contractMapper; // Inyecta el mapper como una dependencia

    // Constructor para la inyección de dependencias
    public ContractServiceImpl(ContractRepository repository, ContractMapper contractMapper) {
        this.repository = repository;
        this.contractMapper = contractMapper;
    }

    @Override
    public Page<ContractDTO> findAll(Pageable pageable) {
        // Usar la instancia del mapper inyectada en lugar de la referencia estática
        return repository.findAll(pageable)
                         .map(contractMapper::toDTO);
    }

    @Override
    public Optional<ContractDTO> findById(Long id) {
        Optional<Contract> contract = repository.findById(id);
        return contract.map(contractMapper::toDTO);
    }

    @Override
    public ContractDTO save(ContractDTO contractDTO) {
        // Convertir DTO a entidad usando la instancia del mapper
        Contract contract = contractMapper.toEntity(contractDTO);
        Contract savedEntity = repository.save(contract);
        return contractMapper.toDTO(savedEntity); // Convertir la entidad guardada de vuelta a DTO
    }

    @Override
    public ContractDTO update(Long id, ContractDTO contractDTO) {
        return repository.findById(id).map(existingContract -> {
            // Mapear el DTO a la entidad, pero conservando el ID de la entidad existente
            Contract updatedContract = contractMapper.toEntity(contractDTO);
            updatedContract.setId(existingContract.getId()); // Asegurarse de mantener el ID del contrato existente

            // Guardar el contrato actualizado
            Contract savedContract = repository.save(updatedContract);

            // Convertir la entidad guardada de vuelta a DTO y retornarla
            return contractMapper.toDTO(savedContract);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrato no encontrado"));
    }


    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Contrato no encontrado");
        }
        repository.deleteById(id);
    }
}