package com.artigo.dota.mapper;
import com.artigo.dota.dto.ProductDTO;
import com.artigo.dota.dto.ProductDetailsDTO;
import com.artigo.dota.dto.ProductSubmitDTO;
import com.artigo.dota.entity.ProductDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;

@Mapper(componentModel = "spring", uses = {ProductDetailsMapper.class})
public abstract class ProductMapper {

    @Autowired
    private ProductDetailsMapper productDetailsMapper;

    @Mapping(target = "isDeleted", expression = "java(mapIsDeleted())")
    public abstract ProductDO dtoToEntity(ProductDTO productDTO);

    @Mapping(target = "isDeleted", expression = "java(mapIsDeleted())")
    public abstract ProductDO dtoToEntity(ProductSubmitDTO productSubmitDTO);

    public abstract ProductDTO entityToDto(ProductDO product);

    public ProductDTO submitDtoToDto(ProductSubmitDTO productSubmitDTO){
        ProductDTO productDTO = new ProductDTO();
        if(productSubmitDTO.getId() != null) {
            productDTO.setId(productSubmitDTO.getId());
        }
        productDTO.setName(productSubmitDTO.getName());
        productDTO.setType(productSubmitDTO.getType());
        productDTO.setSize(productSubmitDTO.getSize());
        productDTO.setPrice(productSubmitDTO.getPrice());
        var productDetailsList = new ArrayList<ProductDetailsDTO>();
        productSubmitDTO.getProductDetails().stream()
                .forEach(productDetailsSubmitDTO ->
                    productDetailsList.add(productDetailsMapper.submitDtoToDto(productDetailsSubmitDTO))
                );
        productDTO.setProductDetails(productDetailsList);

        return productDTO;
    }

    protected Boolean mapIsDeleted() {
        return false;
    }

}
