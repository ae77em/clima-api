import com.meli.test.EstadoClima;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class EstadoClimaTests {

    @Test
    public void planetasAlineadosConSolTest(){
        EstadoClima sm = new EstadoClima();

        sm.setClimaDia(0);

        assertEquals(sm.getClima(), "sequia");
    }

    @Test
    public void planetasFormanTrianguloSinSolDentroTest(){
        EstadoClima sm = new EstadoClima();

        sm.setClimaDia(2);

        assertEquals(sm.getClima(), "normal");
    }
}
