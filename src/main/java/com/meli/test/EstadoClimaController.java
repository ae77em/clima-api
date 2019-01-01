package com.meli.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
public class EstadoClimaController {

    @Autowired
    EstadoClimaService service;

    @RequestMapping("/clima")
    public HashMap<String, Object> clima(@RequestParam(value = "dia", defaultValue = "0") Integer dia) {
        return service.findFirstByDia(dia);
    }

    @RequestMapping("/periodos-sequia")
    public HashMap<String, Object> periodosSequia() {
        return service.getPeriodosSequia();
    }

    @RequestMapping("/periodos-lluvia")
    public HashMap<String, Object> periodosLluvia() {
        return service.getPeriodosLluvia();
    }

    @RequestMapping("/periodos-condiciones-optimas")
    public HashMap<String, Object> periodosCondicionesOptimas() {
        return service.getPeriodosCondicionesOptimas();
    }

    @RequestMapping("/calcular-estados")
    public ResponseEntity.BodyBuilder calcularEstados() {
        try {
            service.calcularEstados();
            return ResponseEntity.ok();
        } catch (Exception ex){
            return ResponseEntity.status(500);
        }
    }
}
