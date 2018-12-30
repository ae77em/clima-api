package com.meli.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;


@Component
public class ServicioMeteorologicoJobs {

    private static final Logger log = LoggerFactory.getLogger(ServicioMeteorologicoJobs.class);

    @Autowired
    private ServicioMeteorologicoRepository repository;

    private static final int CANTIDAD_DIAS_10_ANIOS = 3600;


    @Scheduled(cron = "0 0 * * * *")
    public void cargarEstadoClimaProximosDiezAnios(){
        ServicioMeteorologico servicioMeteorologico;

        log.info("iniciando carga de datos de clima...");

        repository.deleteAll();

        for (int dia = 0; dia < CANTIDAD_DIAS_10_ANIOS; ++dia) {
            servicioMeteorologico = new ServicioMeteorologico();
            servicioMeteorologico.calcularClimaDia(dia);
            repository.save(servicioMeteorologico);
        }

        log.info("carga de datos de clima finalizada...");
    }
}
