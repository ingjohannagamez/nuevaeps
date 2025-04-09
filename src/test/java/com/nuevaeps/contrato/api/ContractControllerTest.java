package com.nuevaeps.contrato.api;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nuevaeps.contrato.api.controller.ContractController;
import com.nuevaeps.contrato.api.dto.ContractDTO;
import com.nuevaeps.contrato.service.interfaces.IContractService;

@WebMvcTest(ContractController.class)
class ContractControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IContractService contractService;

    private ContractDTO contractDTO;

	@BeforeEach
	void setUp() {
		contractDTO = new ContractDTO();
		contractDTO.setId(1L);
		contractDTO.setModalidad("Modalidad A");
		contractDTO.setNumero(12345);
		contractDTO.setRegimen("Subsidiado");
		contractDTO.setArchivo("archivo.txt");
	}

    @Test
    void testFindAllContracts() throws Exception {
        Page<ContractDTO> contractsPage = new PageImpl<>(Collections.singletonList(contractDTO));
        when(contractService.findAll(PageRequest.of(0, 10))).thenReturn(contractsPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/contracts")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].modalidad").value("Modalidad A"))
                .andExpect(jsonPath("$.content[0].numero").value(12345))
                .andExpect(jsonPath("$.content[0].regimen").value("Subsidiado"));
    }

    @Test
    void testFindContractById() throws Exception {
        when(contractService.findById(1L)).thenReturn(Optional.of(contractDTO));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/contracts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modalidad").value("Modalidad A"))
                .andExpect(jsonPath("$.numero").value(12345))
                .andExpect(jsonPath("$.regimen").value("Subsidiado"));
    }

    @Test
    void testUpdateContract() throws Exception {
        when(contractService.update(Mockito.eq(1L), Mockito.any(ContractDTO.class))).thenReturn(contractDTO);

        String contractJson = """
                {
                    "modalidad": "Modalidad A",
                    "numero": 12345,
                    "regimen": "Subsidiado",
                    "archivo": "archivo.txt"
                }
                """;

        mockMvc.perform(MockMvcRequestBuilders.put("/api/contracts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(contractJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.modalidad").value("Modalidad A"))
                .andExpect(jsonPath("$.numero").value(12345))
                .andExpect(jsonPath("$.regimen").value("Subsidiado"));
    }

    @Test
    void testDeleteContract() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/contracts/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }
}