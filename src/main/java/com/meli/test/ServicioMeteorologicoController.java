package com.meli.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ServicioMeteorologicoController {

    @Autowired
    ServicioMeteorologicoRepository repository;

    @RequestMapping("/clima")
    public ServicioMeteorologico clima(@RequestParam(value="dia", defaultValue="0") Integer dia) {
        return repository.findFirstByDia(dia);
    }

    @RequestMapping("/dias")
    public List<ServicioMeteorologico> dias(@RequestParam(value="clima", defaultValue="normal") String clima) {
        return repository.findByClima(clima);
    }
}
