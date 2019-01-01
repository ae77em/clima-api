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
    private static final double DISTANCIA_VULCANO = 1000.0;
    @Transient
    private static final double DISTANCIA_BETASOIDE = 2000.0;
    @Transient
    private static final double DISTANCIA_FERENGI = 500.0;
    @Transient
    private static final int AREA_CON_LA_QUE_LOS_CONSIDERAMOS_ALINEADOS = 30000;

    private Point2D.Double posicionFerengi;

    private Point2D.Double posicionVulcano;

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
        this.clima = "- no definido -";
    }

    public Integer getDia() {
        return this.dia;
    }

    public String getClima() {
        return this.clima;
    }

    public void setClimaDia(Integer dia){
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
            if (planetasEstanEnLineaConSol()) {
                this.clima = "lluvia";
            } else {
                this.clima = "condiciones óptimas de presión y temperatura";
            }

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
        BigDecimal xf = new BigDecimal(posicionFerengi.x);
        BigDecimal yf = new BigDecimal(posicionFerengi.y);
        BigDecimal xb = new BigDecimal(posicionBetasoide.x);
        BigDecimal yb = new BigDecimal(posicionBetasoide.y);
        BigDecimal xv = new BigDecimal(posicionVulcano.x);
        BigDecimal yv = new BigDecimal(posicionVulcano.y);

        BigDecimal a = xf.multiply(yb.subtract(yv));
        BigDecimal b = xb.multiply(yv.subtract(yf));
        BigDecimal c = xv.multiply(yf.subtract(yb));

        BigDecimal area = a.add(b).add(c).multiply(BigDecimal.valueOf(0.5)).abs();

        return area.compareTo(BigDecimal.valueOf(AREA_CON_LA_QUE_LOS_CONSIDERAMOS_ALINEADOS)) < 0;
    }

    private boolean planetasEstanEnLineaConSol() {
        Double fx = posicionFerengi.x;
        Double fy = posicionFerengi.y;
        BigDecimal anguloFerengi = new BigDecimal(Math.toDegrees(Math.atan2(fx, fy)));
        Double bx = posicionBetasoide.x;
        Double by = posicionBetasoide.y;
        BigDecimal anguloBetasoide = new BigDecimal(Math.toDegrees(Math.atan2(bx, by)));
        Double vx = posicionVulcano.x;
        Double vy = posicionVulcano.y;
        BigDecimal anguloVulcano = new BigDecimal(Math.toDegrees(Math.atan2(vx, vy)));

        BigDecimal diferenciaFyB = anguloFerengi.abs().subtract(anguloBetasoide.abs()).abs();
        boolean ferengiYBetasoideAlineados = diferenciaFyB.compareTo(BigDecimal.valueOf(0.0)) == 0 || diferenciaFyB.compareTo(BigDecimal.valueOf(180.0)) == 0;

        BigDecimal diferenciaByV = anguloBetasoide.abs().subtract(anguloVulcano.abs()).abs();
        boolean betasoideYVulcanoAlineados = diferenciaByV.compareTo(BigDecimal.valueOf(0.0)) == 0 || diferenciaByV.compareTo(BigDecimal.valueOf(180.0)) == 0;

        return ferengiYBetasoideAlineados && betasoideYVulcanoAlineados;
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
        Double radian = Math.toRadians(-dia);
        Double x = BigDecimal.valueOf(DISTANCIA_FERENGI).multiply(BigDecimal.valueOf(Math.cos(radian))).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        Double y = BigDecimal.valueOf(DISTANCIA_FERENGI).multiply(BigDecimal.valueOf(Math.sin(radian))).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

        return new Point2D.Double(x, y);
    }

    /*
    El planeta Betasoide se desplaza con una velocidad angular de 3 grados/día en sentido
    horario. Su distancia con respecto al sol es de 2000Km.
    */
    private Point2D.Double getPosicionBetasoide() {
        Double radian = Math.toRadians(-3 * dia);
        Double x = BigDecimal.valueOf(DISTANCIA_BETASOIDE).multiply(BigDecimal.valueOf(Math.cos(radian))).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        Double y = BigDecimal.valueOf(DISTANCIA_BETASOIDE).multiply(BigDecimal.valueOf(Math.sin(radian))).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

        return new Point2D.Double(x, y);
    }

    /*
    El planeta Vulcano se desplaza con una velocidad angular de 5 grados/día en sentido
    anti­horario, su distancia con respecto al sol es de 1000Km.
    */
    private Point2D.Double getPosicionVulcano() {
        Double radian = Math.toRadians(5 * dia);
        Double x = BigDecimal.valueOf(DISTANCIA_VULCANO).multiply(BigDecimal.valueOf(Math.cos(radian))).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();
        Double y = BigDecimal.valueOf(DISTANCIA_VULCANO).multiply(BigDecimal.valueOf(Math.sin(radian))).setScale(2, BigDecimal.ROUND_HALF_EVEN).doubleValue();

        return new Point2D.Double(x, y);
    }
}
