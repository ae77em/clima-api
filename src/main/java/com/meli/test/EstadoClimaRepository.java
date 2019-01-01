package com.meli.test;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EstadoClimaRepository extends MongoRepository<EstadoClima, String> {
    EstadoClima findFirstByDia(Integer dia);

    List<EstadoClima> findByClima(String clima);
}