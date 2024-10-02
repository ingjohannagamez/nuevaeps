package com.ingeneo.logistica.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingeneo.logistica.api.controller.MaritimeLogisticController;
import com.ingeneo.logistica.api.dto.MaritimeLogisticDTO;
import com.ingeneo.logistica.service.interfaces.IMaritimeLogisticService;

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
public class MaritimeLogisticControllerTest {

	private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private IMaritimeLogisticService service;

    @InjectMocks
    private MaritimeLogisticController controller;

    @BeforeEach
    void setup() {
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    void getAllMaritimeLogistic_ReturnsList() throws Exception {
        when(service.findAll()).thenReturn(List.of(new MaritimeLogisticDTO()));

        mockMvc.perform(get("/api/maritimeLogistic")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service).findAll();
    }

    @Test
    void createMaritimeLogistic_ReturnsSavedDto() throws Exception {
        MaritimeLogisticDTO dto = new MaritimeLogisticDTO();
        dto.setProductType("Electrónicos");
        dto.setShippingPrice(3000.0);
        dto.setFleetNumber("ABC1234D");;

        when(service.save(any(MaritimeLogisticDTO.class))).thenReturn(dto);

        mockMvc.perform(post("/api/maritimeLogistic/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.productType").value("Electrónicos"))
                .andExpect(jsonPath("$.shippingPrice").value(3000.0));

        verify(service).save(any(MaritimeLogisticDTO.class));
    }

    @Test
    void getMaritimeLogisticById_ReturnsDto() throws Exception {
        Long id = 1L;
        MaritimeLogisticDTO dto = new MaritimeLogisticDTO();
        dto.setId(id);

        when(service.findById(id)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/maritimeLogistic/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id));

        verify(service).findById(id);
    }

    @Test
    void updateMaritimeLogistic_ReturnsUpdatedDto() throws Exception {
        Long id = 1L;
        MaritimeLogisticDTO dto = new MaritimeLogisticDTO();
        dto.setProductType("Alimentos");

        when(service.update(eq(id), any(MaritimeLogisticDTO.class))).thenReturn(dto);

        mockMvc.perform(put("/api/maritimeLogistic/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productType").value("Alimentos"));

        verify(service).update(eq(id), any(MaritimeLogisticDTO.class));
    }

    @Test
    void deleteMaritimeLogistic_ReturnsNoContent() throws Exception {
        Long id = 1L;
        doNothing().when(service).delete(id);

        mockMvc.perform(delete("/api/maritimeLogistic/{id}", id))
                .andExpect(status().isNoContent());

        verify(service).delete(id);
    }
}
