package com.meli.test;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Component;

import java.awt.geom.Point2D;
import java.math.BigDecimal;

@Component
@Document(collection = "servicio_meteorologico")
public class ServicioMeteorologico {


    @Transient
    private static final int DISTANCIA_VULCANO = 1000;
    @Transient
    private static final int DISTANCIA_BETASOIDE = 2000;
    @Transient
    private static final int DISTANCIA_FERENGI = 500;
    @Transient
    private static final Double RADIAN_UNITARIO = Math.PI / 180.0;
    @Transient
    private Point2D.Double posicionFerengi;
    @Transient
    private Point2D.Double posicionVulcano;
    @Transient
    private Point2D.Double posicionBetasoide;

    @Id
    public String id;

    private Integer dia;
    private String clima;

    public ServicioMeteorologico(Integer dia, String clima) {
        this.dia = dia;
        this.clima = clima;
    }

    public ServicioMeteorologico() {
        this.dia = 0;
        this.clima = "";
    }

    public long getDia() {
        return this.dia;
    }

    public String getClima() {
        return this.clima;
    }

    public void calcularClimaDia(Integer dia){
        this.dia = dia;
        calcularClima();
    }

    private void calcularClima() {
        this.posicionFerengi = getPosicionFerengi();
        this.posicionVulcano = getPosicionVulcano();
        this.posicionBetasoide = getPosicionBetasoide();

        if (planetasEstanEnLineaEntreEllos()) {
            if (planetasEstanEnLineaConSol()) {
                this.clima = "sequia";
            } else {
                this.clima = "condiciones óptimas de presión y temperatura";
            }
        } else if (solEstaEnTrianguloDePlanetas()) {
            this.clima = "lluvia";
        } else {
            this.clima = "normal";
        }
    }

    /*
    Cuando los tres planetas están alineados entre sí y a su vez alineados con respecto al sol, el
    sistema solar experimenta un período de sequía.

    Cuando los tres planetas no están alineados, forman entre sí un triángulo. Es sabido que en el
    momento en el que el sol se encuentra dentro del triángulo, el sistema solar experimenta un
    período de lluvia, teniendo éste, un pico de intensidad cuando el perímetro del triángulo está en
    su máximo.

    Las condiciones óptimas de presión y temperatura se dan cuando los tres planetas están
    alineados entre sí pero no están alineados con el sol.
    */
    private boolean planetasEstanEnLineaEntreEllos() {
        BigDecimal fx = new BigDecimal(getPosicionFerengi().x);
        BigDecimal fy = new BigDecimal(getPosicionFerengi().y);
        BigDecimal bx = new BigDecimal(getPosicionBetasoide().x);
        BigDecimal by = new BigDecimal(getPosicionBetasoide().y);
        BigDecimal vx = new BigDecimal(getPosicionVulcano().x);
        BigDecimal vy = new BigDecimal(getPosicionVulcano().y);

        BigDecimal a = (fx.subtract(vx)).multiply(by.subtract(fy));
        BigDecimal b = (fx.subtract(bx)).multiply(vy.subtract(fy));
        BigDecimal half = new BigDecimal(0.5);

        BigDecimal area = a.subtract(b).abs().multiply(half);

        return area.compareTo(BigDecimal.valueOf(0.0)) == 0;
    }

    private boolean planetasEstanEnLineaConSol() {
        Double Fx = getPosicionFerengi().x;
        Double Fy = getPosicionFerengi().y;
        BigDecimal anguloFerengi = new BigDecimal(Math.atan2(Fx, Fy));
        Double Bx = getPosicionBetasoide().x;
        Double By = getPosicionBetasoide().y;
        BigDecimal anguloBetasoide = new BigDecimal(Math.atan2(Bx, By));
        Double Vx = getPosicionVulcano().x;
        Double Vy = getPosicionVulcano().y;
        BigDecimal anguloVulcano = new BigDecimal(Math.atan2(Vx, Vy));

        return anguloFerengi.compareTo(anguloBetasoide) == 0 && anguloBetasoide.compareTo(anguloVulcano) == 0;
    }

    private Double signo(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
        return (p1.x - p3.x) * (p2.y - p3.y) - (p2.x - p3.x) * (p1.y - p3.y);
    }

    private boolean solEstaEnTrianguloDePlanetas() {
        boolean b1, b2, b3;
        Point2D.Double posicionSol = new Point2D.Double(0.0, 0.0);

        b1 = signo(posicionSol, posicionFerengi, posicionVulcano) < 0.0f;
        b2 = signo(posicionSol, posicionVulcano, posicionBetasoide) < 0.0f;
        b3 = signo(posicionSol, posicionBetasoide, posicionFerengi) < 0.0f;

        return ((b1 == b2) && (b2 == b3));
    }

    /*
    El planeta Ferengi se desplaza con una velocidad angular de 1 grados/día en sentido
    horario. Su distancia con respecto al sol es de 500Km.
    */
    private Point2D.Double getPosicionFerengi() {
        Double radian = -BigDecimal.valueOf(RADIAN_UNITARIO).multiply(BigDecimal.valueOf((dia))).doubleValue();
        Double x = BigDecimal.valueOf(DISTANCIA_FERENGI).multiply(BigDecimal.valueOf(Math.cos(radian))).doubleValue();
        Double y = BigDecimal.valueOf(DISTANCIA_FERENGI).multiply(BigDecimal.valueOf(Math.sin(radian))).doubleValue();

        return new Point2D.Double(x, y);
    }

    /*
    El planeta Betasoide se desplaza con una velocidad angular de 3 grados/día en sentido
    horario. Su distancia con respecto al sol es de 2000Km.
    */
    private Point2D.Double getPosicionBetasoide() {
        Double radian = BigDecimal.valueOf(-3).multiply(BigDecimal.valueOf(RADIAN_UNITARIO)).multiply(BigDecimal.valueOf((dia))).doubleValue();
        Double x = BigDecimal.valueOf(DISTANCIA_BETASOIDE).multiply(BigDecimal.valueOf(Math.cos(radian))).doubleValue();
        Double y = BigDecimal.valueOf(DISTANCIA_BETASOIDE).multiply(BigDecimal.valueOf(Math.sin(radian))).doubleValue();

        return new Point2D.Double(x, y);
    }

    /*
    El planeta Vulcano se desplaza con una velocidad angular de 5 grados/día en sentido
    anti­horario, su distancia con respecto al sol es de 1000Km.
    */
    private Point2D.Double getPosicionVulcano() {
        Double radian = BigDecimal.valueOf(5).multiply(BigDecimal.valueOf(RADIAN_UNITARIO)).multiply(BigDecimal.valueOf((dia))).doubleValue();
        Double x = BigDecimal.valueOf(DISTANCIA_VULCANO).multiply(BigDecimal.valueOf(Math.cos(radian))).doubleValue();
        Double y = BigDecimal.valueOf(DISTANCIA_VULCANO).multiply(BigDecimal.valueOf(Math.sin(radian))).doubleValue();

        return new Point2D.Double(x, y);
    }
}
