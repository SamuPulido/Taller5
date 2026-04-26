package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.Combo;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ComboTest {
	
	private Combo combo;

    @BeforeEach
    void setUp()
    {
        ArrayList<ProductoMenu> items = new ArrayList<ProductoMenu>();
        items.add( new ProductoMenu( "hamburguesa", 12000 ) );
        items.add( new ProductoMenu( "papas", 6000 ) );

        combo = new Combo( "combo basico", 0.10, items );
    }

    @Test
    void testGetNombre()
    {
        assertEquals( "combo basico", combo.getNombre() );
    }

    @Test
    void testGetPrecio()
    {
        assertEquals( 16200, combo.getPrecio() );
    }

    @Test
    void testGenerarTextoFactura()
    {
        String texto = combo.generarTextoFactura();

        assertTrue( texto.contains( "Combo combo basico" ) );
        assertTrue( texto.contains( "Descuento: 0.1" ) );
        assertTrue( texto.contains( "16200" ) );
    }
}
