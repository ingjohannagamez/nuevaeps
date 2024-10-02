package com.nuevaeps.contrato.api.mapper;

import org.springframework.stereotype.Component;

import com.nuevaeps.contrato.api.dto.ContractDTO;
import com.nuevaeps.contrato.model.Contract;

@Component
public class ContractMapper {

    public ContractDTO toDTO(Contract contract) {
        if (contract == null) {
            return null;  // Manejo de valores nulos
        }

        ContractDTO dto = new ContractDTO();
        dto.setId(contract.getId());
        dto.setModalidad(contract.getModalidad());
        dto.setNumero(contract.getNumero());
        dto.setRegimen(contract.getRegimen());
        dto.setArchivo(contract.getArchivo());

        return dto;
    }

    public Contract toEntity(ContractDTO dto) {
        if (dto == null) {
            return null;  // Manejo de valores nulos
        }

        Contract entity = new Contract();
        entity.setId(dto.getId());
        entity.setModalidad(dto.getModalidad());
        entity.setNumero(dto.getNumero());
        entity.setRegimen(dto.getRegimen());
        entity.setArchivo(dto.getArchivo());

        return entity;
    }
}