package com.nuevaeps.contrato.service.interfaces;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.nuevaeps.contrato.api.dto.ContractDTO;

public interface IContractService {

	Page<ContractDTO> findAll(Pageable pageable);
	
	Optional<ContractDTO> findById(Long id);
	
	ContractDTO save(ContractDTO clientDTO);
	
	ContractDTO update(Long id, ContractDTO clientDTO);
	
	void delete(Long id);

}
