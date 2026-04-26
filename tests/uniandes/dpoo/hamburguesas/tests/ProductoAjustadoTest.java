package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uniandes.dpoo.hamburguesas.mundo.Ingrediente;
import uniandes.dpoo.hamburguesas.mundo.ProductoAjustado;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class ProductoAjustadoTest {

	private ProductoMenu base;
    private ProductoAjustado ajustado;
    private Ingrediente queso;
    private Ingrediente tomate;

    @BeforeEach
    void setUp()
    {
        base = new ProductoMenu( "hamburguesa", 12000 );
        ajustado = new ProductoAjustado( base );
        queso = new Ingrediente( "queso", 2000 );
        tomate = new Ingrediente( "tomate", 1000 );
    }

    @Test
    void testGetNombre()
    {
        assertEquals( "hamburguesa", ajustado.getNombre() );
    }

    @Test
    void testGetPrecioSinAjustes()
    {
        assertEquals( 12000, ajustado.getPrecio() );
    }

    @Test
    void testGetPrecioConAgregados()
    {
        ajustado.agregarIngrediente( queso );
        ajustado.agregarIngrediente( tomate );

        assertEquals( 15000, ajustado.getPrecio() );
    }

    @Test
    void testEliminarIngredienteNoRestaPrecio()
    {
        ajustado.eliminarIngrediente( tomate );

        assertEquals( 12000, ajustado.getPrecio() );
    }

    @Test
    void testGenerarTextoFactura()
    {
        ajustado.agregarIngrediente( queso );
        ajustado.eliminarIngrediente( tomate );

        String texto = ajustado.generarTextoFactura();

        assertTrue( texto.contains( "hamburguesa" ) );
        assertTrue( texto.contains( "+queso" ) );
        assertTrue( texto.contains( "2000" ) );
        assertTrue( texto.contains( "-tomate" ) );
        assertTrue( texto.contains( "14000" ) );
    }
	
}
