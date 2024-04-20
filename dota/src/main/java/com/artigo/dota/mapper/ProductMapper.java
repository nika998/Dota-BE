package com.artigo.dota.mapper;
import com.artigo.dota.dto.ProductDTO;
import com.artigo.dota.dto.ProductSubmitDTO;
import com.artigo.dota.entity.ProductDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ProductMapper {
    @Mapping(target = "isDeleted", expression = "java(mapIsDeleted())")
    ProductDO dtoToEntity(ProductDTO productDTO);

    ProductDTO entityToDto(ProductDO product);

    default ProductDTO submitDtoToDto(ProductSubmitDTO productSubmitDTO){
        ProductDTO productDTO = new ProductDTO();
        if(productSubmitDTO.getId() != null) {
            productDTO.setId(productSubmitDTO.getId());
        }
        productDTO.setName(productSubmitDTO.getName());
        productDTO.setType(productSubmitDTO.getType());
        productDTO.setPrice(productSubmitDTO.getPrice());
        return productDTO;
    }

    default Boolean mapIsDeleted() {
        return false;
    }

}
