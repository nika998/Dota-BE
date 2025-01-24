package com.artigo.dota.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class ProductDetailQuantitiesRequestDTO {

    private List<UUID> productDetailIds;
}
