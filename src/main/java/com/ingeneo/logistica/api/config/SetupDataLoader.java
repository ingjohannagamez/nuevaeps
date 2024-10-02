package com.ingeneo.logistica.api.config;

import com.ingeneo.logistica.model.Client;
import com.ingeneo.logistica.model.Role;
import com.ingeneo.logistica.model.User;
import com.ingeneo.logistica.repository.ClientRepository;
import com.ingeneo.logistica.repository.RoleRepository;
import com.ingeneo.logistica.repository.UserRepository;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Configuration
public class SetupDataLoader {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private ClientRepository clientRepository;

    @Value("${app.initial.username}")
    private String initialUsername;

    @Value("${app.initial.password}")
    private String initialPassword;

    @Value("${app.initial.role}")
    private String initialRole;
    
    @Value("${app.initial.client.name}")
    private String initialClientName;
    

    @PostConstruct
    public void init() {
    	createRoleAndUser();
        createClient();
    }
    
    private void createRoleAndUser() {
        Role adminRole = roleRepository.findByName(initialRole);
        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName(initialRole);
            roleRepository.save(adminRole);
        }

        Optional<User> adminUserOptional = userRepository.findByUsername(initialUsername);
        if (!adminUserOptional.isPresent()) {
            User adminUser = new User();
            adminUser.setUsername(initialUsername);
            adminUser.setPassword(passwordEncoder.encode(initialPassword));
            adminUser.setRoles(new HashSet<>(Collections.singletonList(adminRole)));
            userRepository.save(adminUser);
        }
    }

    private void createClient() {
        Client client = clientRepository.findByName(initialClientName);
        if (client == null) {
            client = new Client();
            client.setName(initialClientName);
            clientRepository.save(client);
        }
    }
}