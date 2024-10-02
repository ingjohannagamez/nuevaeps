package com.ingeneo.logistica.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingeneo.logistica.api.controller.ClientController;
import com.ingeneo.logistica.api.dto.ClientDTO;
import com.ingeneo.logistica.service.interfaces.IClientService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ClientControllerTest {

	private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private IClientService service;

    @InjectMocks
    private ClientController controller;

    @BeforeEach
    void setup() {
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    void getAllClient_ReturnsList() throws Exception {
        when(service.findAll()).thenReturn(List.of(new ClientDTO()));

        mockMvc.perform(get("/api/client")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service).findAll();
    }

    @Test
    void createClient_ReturnsSavedDto() throws Exception {
        ClientDTO dto = new ClientDTO();
        dto.setName("Cliente Prueba");

        when(service.save(any(ClientDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/client/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Cliente Prueba"));

        verify(service).save(any(ClientDTO.class));
    }

    @Test
    void getClientById_ReturnsDto() throws Exception {
        Long id = 1L;
        ClientDTO dto = new ClientDTO();
        dto.setId(id);

        when(service.findById(id)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/client/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(service).findById(id);
    }

    @Test
    void updateClient_ReturnsUpdatedDto() throws Exception {
        Long id = 1L;
        ClientDTO dto = new ClientDTO();
        dto.setName("Cliente Actualizado");

        when(service.update(eq(id), any(ClientDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/client/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Cliente Actualizado"));

        verify(service).update(eq(id), any(ClientDTO.class));
    }

    @Test
    void deleteClient_ReturnsNoContent() throws Exception {
        Long id = 1L;
        doNothing().when(service).delete(id);

        mockMvc.perform(delete("/api/client/{id}", id))
                .andExpect(status().isNoContent());

        verify(service).delete(id);
    }
}
