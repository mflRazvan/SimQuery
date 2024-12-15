package com.example.backend.utils.converter;

import java.util.Collection;
import java.util.List;

public interface Converter <E, D> {
    E createFromDto(D dto);
    D createFromEntity(E entity);

    default List<E> createFromDtos(final Collection<D> dtos){
        return dtos.stream().map(this::createFromDto).toList();
    }
    default List<D> createFromEntities(final Collection<E> entities){
        return entities.stream().map(this::createFromEntity).toList();
    }
}
