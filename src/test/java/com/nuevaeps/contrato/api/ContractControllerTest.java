package com.nuevaeps.contrato.api;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuevaeps.contrato.api.controller.ContractController;
import com.nuevaeps.contrato.api.dto.ContractDTO;
import com.nuevaeps.contrato.service.interfaces.IContractService;

@ExtendWith(MockitoExtension.class)
public class ContractControllerTest {

    private MockMvc mockMvc;

    // Hacemos final porque no se cambia después de la inicialización
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private IContractService service;

    @InjectMocks
    private ContractController controller;

    @BeforeEach
    void setup() {
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    void getAllContracts_ReturnsList() throws Exception {
        // Simulando una lista de ContractDTO
        List<ContractDTO> contracts = List.of(new ContractDTO());

        // Simulando una página de ContractDTO usando PageImpl
        Page<ContractDTO> page = new PageImpl<>(contracts, PageRequest.of(0, 10), contracts.size());

        // Simular que el servicio devuelve una página en lugar de una lista
        when(service.findAll(any(PageRequest.class))).thenReturn(page);

        mockMvc.perform(get("/api/contracts")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service).findAll(any(PageRequest.class));
    }

    @Test
    void createContract_ReturnsSavedDto() throws Exception {
        ContractDTO dto = new ContractDTO();
        dto.setModalidad("Evento");
        dto.setNumero(12345);
        dto.setRegimen("Contributivo");

        when(service.save(any(ContractDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/contracts/upload")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.modalidad").value("Evento"))
                .andExpect(jsonPath("$.numero").value("12345"))
                .andExpect(jsonPath("$.regimen").value("Contributivo"));

        verify(service).save(any(ContractDTO.class));
    }

    @Test
    void getContractById_ReturnsDto() throws Exception {
        Long id = 1L;
        ContractDTO dto = new ContractDTO();
        dto.setId(id);
        dto.setModalidad("Evento");
        dto.setNumero(12345);
        dto.setRegimen("Contributivo");

        when(service.findById(id)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/contracts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.modalidad").value("Evento"));

        verify(service).findById(id);
    }

    @Test
    void updateContract_ReturnsUpdatedDto() throws Exception {
        Long id = 1L;
        ContractDTO dto = new ContractDTO();
        dto.setModalidad("Capita");
        dto.setNumero(6789);
        dto.setRegimen("Subsidiado");

        when(service.update(eq(id), any(ContractDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/contracts/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modalidad").value("Capita"))
                .andExpect(jsonPath("$.numero").value(67890))
                .andExpect(jsonPath("$.regimen").value("Subsidiado"));

        verify(service).update(eq(id), any(ContractDTO.class));
    }

    @Test
    void deleteContract_ReturnsNoContent() throws Exception {
        Long id = 1L;
        doNothing().when(service).delete(id);

        mockMvc.perform(delete("/api/contracts/{id}", id))
                .andExpect(status().isNoContent());

        verify(service).delete(id);
    }
}