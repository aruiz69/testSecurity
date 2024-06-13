package com.example.demo.modelo;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;



@Data
@Builder
public class DepositDto {
    @Pattern(regexp = "\\d{1,10}", message = "El número de cuenta debe ser numérico y no mayor de 10 caracteres")
    private String accountNumber;

    @Pattern(regexp = "^[a-zA-Z ]{1,100}$", message = "El nombre del cliente debe contener solo letras y tener una longitud máxima de 100")
    private String customerName;

    @DecimalMax(value = "1000000", message = "El monto del depósito debe ser menor a 1000000")
    private double depositAmount;
}