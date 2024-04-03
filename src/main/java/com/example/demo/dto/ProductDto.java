package com.example.demo.dto;


import com.example.demo.entity.Category;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class ProductDto {
    private Long id;
    private String name;
    private long categoryId;
    private Double price;
    private Double weight;
    private String description;
    private String imageName;
}
