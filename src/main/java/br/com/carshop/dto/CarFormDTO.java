package br.com.carshop.dto;

import lombok.Data;

@Data
public class CarFormDTO {

    private String brandCode;
    private String brandName;
    private String model;
    private Double price;
}