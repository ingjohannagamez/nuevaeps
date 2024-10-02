package com.nuevaeps.contrato.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import com.nuevaeps.contrato.model.Contract;

public interface ContractRepository extends JpaRepository<Contract, Long> {

	@Override
    @NonNull
    Page<Contract> findAll(@NonNull Pageable pageable);
	
}
