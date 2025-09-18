package br.ifba.edu.BibliotecaOnline.mapper;

import br.ifba.edu.BibliotecaOnline.DTO.LivroDTO;
import br.ifba.edu.BibliotecaOnline.entities.LivroEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface LivroMapper {

    // Converte LivroDTO para LivroEntity

    LivroEntity toEntity(LivroDTO dto);

    // Converte LivroEntity para LivroDTO
    LivroDTO toDTO(LivroEntity entity);
}

