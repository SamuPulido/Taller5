package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoMenuTest {

	private ProductoMenu producto;
	
	@BeforeEach
    void setUp()
    {
        producto = new ProductoMenu( "hamburguesa sencilla", 15000 );
    }

    @Test
    void testGetNombre()
    {
        assertEquals( "hamburguesa sencilla", producto.getNombre() );
    }

    @Test
    void testGetPrecio()
    {
        assertEquals( 15000, producto.getPrecio() );
    }

    @Test
    void testGenerarTextoFactura()
    {
        String texto = producto.generarTextoFactura();

        assertTrue( texto.contains( "hamburguesa sencilla" ) );
        assertTrue( texto.contains( "15000" ) );
    }
	
}
