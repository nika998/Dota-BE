package com.artigo.dota.mapper;

import com.artigo.dota.dto.NewsletterDTO;
import com.artigo.dota.dto.enums.NewsletterStatus;
import com.artigo.dota.entity.NewsletterDO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface NewsletterMapper {

    @Mapping(target = "isDeleted", expression = "java(mapIsDeleted())")
    NewsletterDO dtoToEntity(NewsletterDTO newsletterDTO);

    @Mapping(target = "status", expression = "java(statusSuccess())")
    NewsletterDTO entityToDtoSucces(NewsletterDO newsletterDO);

    @Mapping(target = "status", expression = "java(statusExists())")
    NewsletterDTO entityToDtoExists(NewsletterDO newsletterDO);

    default Boolean mapIsDeleted() {
        return false;
    }

    default NewsletterStatus statusSuccess() {
        return NewsletterStatus.SUCCESSFULLY_SAVED;
    }

    default NewsletterStatus statusExists() {
        return NewsletterStatus.ALREADY_EXISTS;
    }
}
