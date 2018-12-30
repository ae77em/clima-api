import com.meli.test.ServicioMeteorologico;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ServicioMeteorologicoTests {

    @Test
    public void planetasAlineadosConSolTest(){
        ServicioMeteorologico sm = new ServicioMeteorologico();

        sm.calcularClimaDia(0);

        assertEquals(sm.getClima(), "sequia");
    }

    @Test
    public void planetasFormanTrianguloSinSolDentroTest(){
        ServicioMeteorologico sm = new ServicioMeteorologico();

        sm.calcularClimaDia(2);

        assertEquals(sm.getClima(), "normal");
    }
}
