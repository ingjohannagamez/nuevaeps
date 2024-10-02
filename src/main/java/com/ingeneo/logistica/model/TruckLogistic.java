package com.ingeneo.logistica.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;

import java.util.Date;

@Entity
@Getter
@Setter
@Schema(description = "Representa un registro de logística terrestre")
public class TruckLogistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del registro de logística terrestre", example = "1")
    private Long id;

    @Schema(description = "Tipo de producto transportado", example = "Electrónicos")
    private String productType;

    @Schema(description = "Cantidad del producto", example = "100")
    private Integer quantity;

    @Schema(description = "Fecha de registro del envío", example = "2023-04-05")
    private Date registrationDate;

    @Schema(description = "Fecha estimada de entrega", example = "2023-04-10")
    private Date deliveryDate;

    @Schema(description = "Bodega de entrega", example = "Bodega Central")
    private String deliveryWarehouse;

    @Schema(description = "Precio de envío", example = "1500.00")
    private Double shippingPrice;
    
    @Schema(description = "Descuento de precio concedido", example = "1500.00")
    private Double shippingPriceGranted;

    @Schema(description = "Placa del vehículo", example = "ABC123")
    @Pattern(regexp = "^[A-Z]{3}\\d{3}$", message = "La placa del vehículo debe tener 3 letras seguidas de 3 números")
    private String vehiclePlate;

    @Schema(description = "Número único alfanumérico de 10 dígitos para el guía", example = "1234567890")
    private String guideNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
}
