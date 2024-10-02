package com.ingeneo.logistica.api.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TruckLogisticDTO {

	private Long id;

    @NotBlank(message = "El tipo de producto es obligatorio.")
    private String productType;

    @NotNull(message = "La cantidad es obligatoria.")
    @Min(value = 1, message = "La cantidad debe ser al menos 1.")
    private Integer quantity;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de registro es obligatoria.")
    private Date registrationDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @NotNull(message = "La fecha de entrega es obligatoria.")
    private Date deliveryDate;

    @NotBlank(message = "La bodega de entrega es obligatoria.")
    private String deliveryWarehouse;

    @NotNull(message = "El precio de envío es obligatorio.")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio de envío debe ser mayor que cero.")
    private Double shippingPrice;
    
    @DecimalMin(value = "0.0", inclusive = false, message = "El descuento del precio de envío debe ser mayor que cero.")
    private Double shippingPriceGranted;

    @Pattern(regexp = "^[A-Z]{3}\\d{3}$", message = "El formato de la placa del vehículo es inválido.")
    private String vehiclePlate;

    @NotBlank(message = "El número de guía es obligatorio.")
    @Size(min = 10, max = 10, message = "El número de guía debe tener 10 caracteres.")
    private String guideNumber;

    private ClientDTO client;
}
