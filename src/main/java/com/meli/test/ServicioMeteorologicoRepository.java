package com.meli.test;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface ServicioMeteorologicoRepository extends MongoRepository<ServicioMeteorologico, String> {
    ServicioMeteorologico findFirstByDia(Integer dia);
    List<ServicioMeteorologico> findByClima(String clima);
}