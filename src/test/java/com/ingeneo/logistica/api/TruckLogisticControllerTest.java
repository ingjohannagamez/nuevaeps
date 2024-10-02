package com.ingeneo.logistica.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingeneo.logistica.api.controller.TruckLogisticController;
import com.ingeneo.logistica.api.dto.ClientDTO;
import com.ingeneo.logistica.api.dto.TruckLogisticDTO;
import com.ingeneo.logistica.service.interfaces.ITruckLogisticService;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class TruckLogisticControllerTest {

	private MockMvc mockMvc;

    @Mock
    private ITruckLogisticService service;

    @InjectMocks
    private TruckLogisticController controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        mockMvc = standaloneSetup(controller).build();
    }

    @Test
    void getAllTruckLogistics_ReturnsListOfTruckLogistics() throws Exception {
        when(service.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/truckLogistics")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(service).findAll();
    }

    @Test
    void createTruckLogistic_ReturnsSavedTruckLogistic() throws Exception {
        TruckLogisticDTO requestDTO = new TruckLogisticDTO();
        ClientDTO client = new ClientDTO();
        
        client.setId(1L);
        client.setName("Prueba");
        
        requestDTO.setProductType("Electrónicos");
        requestDTO.setQuantity(100);
        requestDTO.setRegistrationDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-04-05"));
        requestDTO.setDeliveryDate(new SimpleDateFormat("yyyy-MM-dd").parse("2023-04-10"));
        requestDTO.setDeliveryWarehouse("Bodega Central");
        requestDTO.setShippingPrice(2500.0);
        requestDTO.setVehiclePlate("XXX123");
        requestDTO.setGuideNumber("1234567890");
        requestDTO.setClient(client);

        TruckLogisticDTO responseDTO = new TruckLogisticDTO();
        responseDTO.setId(1L);
        responseDTO.setProductType(requestDTO.getProductType());
        responseDTO.setQuantity(requestDTO.getQuantity());
        responseDTO.setRegistrationDate(requestDTO.getRegistrationDate());
        responseDTO.setDeliveryDate(requestDTO.getDeliveryDate());
        responseDTO.setDeliveryWarehouse(requestDTO.getDeliveryWarehouse());
        responseDTO.setShippingPrice(requestDTO.getShippingPrice());
        responseDTO.setShippingPriceGranted(2200.0);
        responseDTO.setVehiclePlate(requestDTO.getVehiclePlate());
        responseDTO.setGuideNumber(requestDTO.getGuideNumber());
        responseDTO.setClient(requestDTO.getClient());

        when(service.save(any(TruckLogisticDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(post("/api/truckLogistics/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(responseDTO.getId()))
                .andExpect(jsonPath("$.productType").value(responseDTO.getProductType()))
                .andExpect(jsonPath("$.quantity").value(responseDTO.getQuantity()))
                .andExpect(jsonPath("$.registrationDate").value("2023-04-05"))
                .andExpect(jsonPath("$.deliveryDate").value("2023-04-10"))
                .andExpect(jsonPath("$.deliveryWarehouse").value(responseDTO.getDeliveryWarehouse()))
                .andExpect(jsonPath("$.shippingPrice").value(responseDTO.getShippingPrice()))
                .andExpect(jsonPath("$.shippingPriceGranted").value(responseDTO.getShippingPriceGranted()))
                .andExpect(jsonPath("$.vehiclePlate").value(responseDTO.getVehiclePlate()))
                .andExpect(jsonPath("$.guideNumber").value(responseDTO.getGuideNumber()))
                .andExpect(jsonPath("$.client.id").value(responseDTO.getClient().getId()))
                .andDo(print());

        verify(service).save(any(TruckLogisticDTO.class));
    }
    
    @Test
    void getTruckLogisticById_ReturnsTruckLogistic() throws Exception {
        Long id = 1L;
        TruckLogisticDTO truckLogisticDTO = new TruckLogisticDTO();
        truckLogisticDTO.setId(id);
        truckLogisticDTO.setProductType("Electrónicos");
        truckLogisticDTO.setQuantity(100);
        truckLogisticDTO.setDeliveryWarehouse("Bodega Central");
        truckLogisticDTO.setShippingPrice(2500.0);
        truckLogisticDTO.setVehiclePlate("XXX123");
        truckLogisticDTO.setGuideNumber("1234567890");

        when(service.findById(id)).thenReturn(Optional.of(truckLogisticDTO));

        mockMvc.perform(get("/api/truckLogistics/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.productType").value("Electrónicos"))
                .andExpect(jsonPath("$.quantity").value(100))
                .andExpect(jsonPath("$.deliveryWarehouse").value("Bodega Central"))
                .andExpect(jsonPath("$.shippingPrice").value(2500.0))
                .andExpect(jsonPath("$.vehiclePlate").value("XXX123"))
                .andExpect(jsonPath("$.guideNumber").value("1234567890"));

        verify(service).findById(id);
    }
    
    @Test
    void updateTruckLogistic_ReturnsUpdatedTruckLogistic() throws Exception {
        Long id = 1L;
        TruckLogisticDTO requestDTO = new TruckLogisticDTO();
        requestDTO.setProductType("Alimentos");
        requestDTO.setQuantity(150);

        TruckLogisticDTO responseDTO = new TruckLogisticDTO();
        responseDTO.setId(id);
        responseDTO.setProductType("Alimentos");
        responseDTO.setQuantity(150);

        when(service.update(eq(id), any(TruckLogisticDTO.class))).thenReturn(responseDTO);

        mockMvc.perform(put("/api/truckLogistics/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.productType").value("Alimentos"))
                .andExpect(jsonPath("$.quantity").value(150));

        verify(service).update(eq(id), any(TruckLogisticDTO.class));
    }

    @Test
    void deleteTruckLogistic_ReturnsNoContent() throws Exception {
        Long id = 1L;

        doNothing().when(service).delete(id);

        mockMvc.perform(delete("/api/truckLogistics/{id}", id))
                .andExpect(status().isNoContent());

        verify(service).delete(id);
    }

    
}
