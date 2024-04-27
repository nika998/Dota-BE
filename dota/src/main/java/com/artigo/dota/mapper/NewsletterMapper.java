package com.artigo.dota.mapper;

import com.artigo.dota.dto.NewsletterDTO;
import com.artigo.dota.entity.NewsletterDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface NewsletterMapper {

    @Mapping(target = "isDeleted", expression = "java(mapIsDeleted())")
    NewsletterDO dtoToEntity(NewsletterDTO newsletterDTO);

    NewsletterDTO entityToDto(NewsletterDO newsletterDO);

    default Boolean mapIsDeleted() {
        return false;
    }
}
