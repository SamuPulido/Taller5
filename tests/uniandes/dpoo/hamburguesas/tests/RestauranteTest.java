package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.PrintWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.excepciones.IngredienteRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.NoHayPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoFaltanteException;
import uniandes.dpoo.hamburguesas.excepciones.ProductoRepetidoException;
import uniandes.dpoo.hamburguesas.excepciones.YaHayUnPedidoEnCursoException;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;
import uniandes.dpoo.hamburguesas.mundo.Restaurante;

public class RestauranteTest {
	
	private Restaurante restaurante;

    @BeforeEach
    void setUp()
    {
        restaurante = new Restaurante();

        File carpetaFacturas = new File( "./facturas" );
        carpetaFacturas.mkdirs();
    }

    @Test
    void testConstructor()
    {
        assertEquals( 0, restaurante.getIngredientes().size() );
        assertEquals( 0, restaurante.getMenuBase().size() );
        assertEquals( 0, restaurante.getMenuCombos().size() );
        assertEquals( 0, restaurante.getPedidos().size() );
        assertNull( restaurante.getPedidoEnCurso() );
    }

    @Test
    void testIniciarPedido() throws Exception
    {
        restaurante.iniciarPedido( "Samuel", "Calle 123" );

        assertNotNull( restaurante.getPedidoEnCurso() );
        assertEquals( "Samuel", restaurante.getPedidoEnCurso().getNombreCliente() );
    }

    @Test
    void testIniciarPedidoCuandoYaHayUno() throws Exception
    {
        restaurante.iniciarPedido( "Samuel", "Calle 123" );

        assertThrows( YaHayUnPedidoEnCursoException.class, () ->
        {
            restaurante.iniciarPedido( "Laura", "Carrera 45" );
        } );
    }

    @Test
    void testCerrarSinPedido()
    {
        assertThrows( NoHayPedidoEnCursoException.class, () ->
        {
            restaurante.cerrarYGuardarPedido();
        } );
    }

    @Test
    void testCerrarYGuardarPedido() throws Exception
    {
        restaurante.iniciarPedido( "Samuel", "Calle 123" );
        restaurante.getPedidoEnCurso().agregarProducto( new ProductoMenu( "hamburguesa", 10000 ) );

        restaurante.cerrarYGuardarPedido();

        assertNull( restaurante.getPedidoEnCurso() );
        assertEquals( 1, restaurante.getPedidos().size() );
    }

    @Test
    void testCargarInformacionRestaurante() throws Exception
    {
        File ingredientes = crearArchivo( "ingredientes", "queso;2000\ntomate;1000\n" );
        File menu = crearArchivo( "menu", "hamburguesa;12000\npapas;6000\n" );
        File combos = crearArchivo( "combos", "combo basico;10%;hamburguesa;papas\n" );

        restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );

        assertEquals( 2, restaurante.getIngredientes().size() );
        assertEquals( 2, restaurante.getMenuBase().size() );
        assertEquals( 1, restaurante.getMenuCombos().size() );
        assertEquals( "combo basico", restaurante.getMenuCombos().get( 0 ).getNombre() );
    }

    @Test
    void testIngredienteRepetido()
    {
        assertThrows( IngredienteRepetidoException.class, () ->
        {
            File ingredientes = crearArchivo( "ingredientes_repetidos", "queso;2000\nqueso;3000\n" );
            File menu = crearArchivo( "menu_normal", "hamburguesa;12000\n" );
            File combos = crearArchivo( "combos_normal", "combo;10%;hamburguesa\n" );

            restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );
        } );
    }

    @Test
    void testProductoRepetido()
    {
        assertThrows( ProductoRepetidoException.class, () ->
        {
            File ingredientes = crearArchivo( "ingredientes_normal", "queso;2000\n" );
            File menu = crearArchivo( "menu_repetido", "hamburguesa;12000\nhamburguesa;13000\n" );
            File combos = crearArchivo( "combos_normal2", "combo;10%;hamburguesa\n" );

            restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );
        } );
    }

    @Test
    void testProductoFaltanteEnCombo()
    {
        assertThrows( ProductoFaltanteException.class, () ->
        {
            File ingredientes = crearArchivo( "ingredientes_normal2", "queso;2000\n" );
            File menu = crearArchivo( "menu_sin_papas", "hamburguesa;12000\n" );
            File combos = crearArchivo( "combo_con_faltante", "combo;10%;hamburguesa;papas\n" );

            restaurante.cargarInformacionRestaurante( ingredientes, menu, combos );
        } );
    }

    private File crearArchivo( String nombre, String contenido ) throws Exception
    {
        File archivo = File.createTempFile( nombre, ".txt" );

        PrintWriter writer = new PrintWriter( archivo );
        writer.print( contenido );
        writer.close();

        return archivo;
    }

}
