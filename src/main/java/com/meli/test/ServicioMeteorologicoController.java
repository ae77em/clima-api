package com.meli.test;

import one.util.streamex.IntStreamEx;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;

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

    @RequestMapping("/periodos-sequia")
    public HashMap<String, Object> periodosSequia() {
        HashMap<String, Object> response = new HashMap<>();
        List<ServicioMeteorologico> periodosSequia = repository.findByClima("sequia");
        IntUnaryOperator next = i -> i + 1;

        IntStream periodos = periodosSequia
                .stream()
                .mapToInt(ServicioMeteorologico::getDia);


        List<List<Integer>> result =
                IntStreamEx.of(periodos)
                        .boxed()
                        .groupRuns((i1, i2) -> next.applyAsInt(i1) == i2).toList();

        response.put("clima", "sequia");
        response.put("cantidad-periodos", result.size());
        response.put("periodos", result);

        return response;
    }
}
