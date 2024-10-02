package com.nuevaeps.contrato.api.mapper;

import com.nuevaeps.contrato.api.dto.ContractDTO;
import com.nuevaeps.contrato.model.Contract;

public class ContractMapper {

	public static ContractDTO toDTO(Contract contract) {
		ContractDTO dto = new ContractDTO();
        
        dto.setId(contract.getId());
        dto.setModalidad(contract.getModalidad());
		dto.setNumero(contract.getNumero());
		dto.setRegimen(contract.getRegimen());
		dto.setArchivo(contract.getArchivo());
        
        return dto;
    }
	
	public static Contract convertToEntity(ContractDTO contract) {
		Contract client = new Contract();

		client.setId(contract.getId());
        client.setModalidad(contract.getModalidad());
		client.setNumero(contract.getNumero());
		client.setRegimen(contract.getRegimen());
		client.setArchivo(contract.getArchivo());
	    
	    return client;
	}
}
