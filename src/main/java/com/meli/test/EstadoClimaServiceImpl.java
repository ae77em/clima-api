package com.meli.test;

import one.util.streamex.IntStreamEx;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.function.IntUnaryOperator;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class EstadoClimaServiceImpl implements EstadoClimaService {

    private static final Logger log = LoggerFactory.getLogger(EstadoClimaServiceImpl.class);

    private static final int CANTIDAD_DIAS_10_ANIOS = 3600;

    @Autowired
    EstadoClimaRepository repository;

    EstadoClimaServiceImpl(EstadoClimaRepository repository) {
        this.repository = repository;
    }

    @Override
    public HashMap<String, Object> findFirstByDia(Integer dia) {
        EstadoClima estadoClima = repository.findFirstByDia(dia);

        HashMap<String, Object> toReturn = new HashMap<>();

        toReturn.put("dia", estadoClima.getDia());
        toReturn.put("clima", estadoClima.getClima());

        return toReturn;
    }

    @Override
    public HashMap<String, Object> getPeriodosSequia() {
        return getPeriodos("sequia");
    }

    @Override
    public HashMap<String, Object> getPeriodosLluvia() {
        HashMap<String, Object> response = new HashMap<>();
        List<EstadoClima> diasSequia = repository.findByClima("lluvia");

        Stream<EstadoClima> streamDias = diasSequia.stream().sorted(Comparator.comparing(EstadoClima::getDia));

        IntStream dias = streamDias.mapToInt(EstadoClima::getDia);

        List<List<Integer>> listadoPeriodos = getListadoPeriodos(dias);
        List<Integer> listadoDiasPicosMaximos = new ArrayList<>();

        for (List<Integer> periodoLluvia : listadoPeriodos) {
            listadoDiasPicosMaximos.add(
                    diasSequia.stream()
                            .sorted(Comparator.comparing(EstadoClima::getDia))
                            .filter(sm -> periodoLluvia.contains(sm.getDia()))
                            .max(Comparator.comparing(EstadoClima::getAreaTrianguloGenerado))
                            .get()
                            .getDia());
        }

        response.put("clima", "lluvia");
        response.put("cantidad_periodos", listadoPeriodos.size());
        response.put("periodos", listadoPeriodos);
        response.put("picos_maximos", listadoDiasPicosMaximos);

        return response;
    }

    @Override
    public HashMap<String, Object> getPeriodosCondicionesOptimas() {
        return getPeriodos("condiciones óptimas de presión y temperatura");
    }

    @Override
    @Scheduled(cron = "0 0 * * * *")
    public void calcularEstados() {
        EstadoClima estadoClima;

        log.info("iniciando carga de datos de clima...");

        repository.deleteAll();

        for (int dia = 0; dia < CANTIDAD_DIAS_10_ANIOS; ++dia) {
            estadoClima = new EstadoClima();
            estadoClima.setClimaDia(dia);
            repository.save(estadoClima);
        }

        log.info("carga de datos de clima finalizada...");
    }

    private HashMap<String, Object> getPeriodos(String clima) {
        HashMap<String, Object> response = new HashMap<>();
        List<EstadoClima> diasSequia = repository.findByClima(clima);

        IntStream dias = diasSequia.stream()
                .mapToInt(EstadoClima::getDia);

        List<List<Integer>> periodos = getListadoPeriodos(dias);

        response.put("clima", clima);
        response.put("cantidad_periodos", periodos.size());
        response.put("periodos", periodos);

        return response;
    }

    private List<List<Integer>> getListadoPeriodos(IntStream dias) {
        IntUnaryOperator next = i -> i + 1;

        return IntStreamEx.of(dias)
                .boxed()
                .groupRuns((i1, i2) -> next.applyAsInt(i1) == i2).toList();
    }
}
