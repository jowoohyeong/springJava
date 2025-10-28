package com.example.demo.service;

public interface EntityMapper<D, E> {
    E toEntity(final D dto);
    D toDto(final E entity);
}
