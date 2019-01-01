package com.meli.test;

import java.util.HashMap;

public interface EstadoClimaService {
    HashMap<String, Object> findFirstByDia(Integer dia);

    HashMap<String, Object> getPeriodosSequia();

    HashMap<String, Object> getPeriodosLluvia();

    HashMap<String, Object> getPeriodosCondicionesOptimas();

    void calcularEstados();
}
