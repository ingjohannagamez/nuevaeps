package com.ingeneo.logistica.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Schema(description = "Representa un registro de logística marítima")
public class MaritimeLogistic {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del registro de logística marítima", example = "1")
    private Long id;

    @Schema(description = "Tipo de producto transportado", example = "Materiales de construcción")
    private String productType;

    @Schema(description = "Cantidad del producto", example = "200")
    private Integer quantity;

    @Schema(description = "Fecha de registro del envío", example = "2023-05-01")
    private Date registrationDate;

    @Schema(description = "Fecha estimada de entrega", example = "2023-05-15")
    private Date deliveryDate;

    @Schema(description = "Puerto de entrega", example = "Puerto de Cartagena")
    private String deliveryPort;

    @Schema(description = "Precio de envío", example = "3000.00")
    private Double shippingPrice;
    
    @Schema(description = "Descuento de precio concedido", example = "1500.00")
    private Double shippingPriceGranted;

    @Schema(description = "Número de la flota", example = "ABC1234D")
    private String fleetNumber;

    @Schema(description = "Número único alfanumérico de 10 dígitos para el guía", example = "0987654321")
    private String guideNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;
}