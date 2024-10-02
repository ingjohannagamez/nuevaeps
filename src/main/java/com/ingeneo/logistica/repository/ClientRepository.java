package com.ingeneo.logistica.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.ingeneo.logistica.model.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {

	Client findByName(String initialClientName);
}
