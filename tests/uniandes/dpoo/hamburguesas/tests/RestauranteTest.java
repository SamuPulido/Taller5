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

public class RestauranteTest
{
    private Restaurante restaurante;

    @BeforeEach
    void setUp()
    {
        restaurante = new Restaurante();
        new File("./facturas").mkdir(); //.mkdir es para crear la carpeta facturas, si ya esta creada, no crea nada
    }

    @Test
    void testConstructor()
    {
        assertEquals(0, restaurante.getPedidos().size());
        assertEquals(0, restaurante.getIngredientes().size());
        assertEquals(0, restaurante.getMenuBase().size());
        assertEquals(0, restaurante.getMenuCombos().size());
        assertNull(restaurante.getPedidoEnCurso());
    }

    @Test
    void testIniciarPedido() throws Exception
    {
        restaurante.iniciarPedido("Samuel", "Calle 123");

        assertNotNull(restaurante.getPedidoEnCurso());
        assertEquals("Samuel", restaurante.getPedidoEnCurso().getNombreCliente());
    }

    @Test
    void testIniciarPedidoCuandoYaHayPedido() throws Exception
    {
        restaurante.iniciarPedido("Samuel", "Calle 123");

        assertThrows(YaHayUnPedidoEnCursoException.class, () ->
        {
            restaurante.iniciarPedido("Laura", "Carrera 45");
        });
    }

    @Test
    void testFlujoCompletoPedido() throws Exception
    {
        restaurante.iniciarPedido("Samuel", "Calle 123");
        restaurante.getPedidoEnCurso().agregarProducto(new ProductoMenu("hamburguesa", 10000));
        restaurante.cerrarYGuardarPedido();

        restaurante.iniciarPedido("Laura", "Carrera 45");

        assertEquals(1, restaurante.getPedidos().size());
        assertEquals("Laura", restaurante.getPedidoEnCurso().getNombreCliente());
    }

    @Test
    void testCerrarSinPedido()
    {
        assertThrows(NoHayPedidoEnCursoException.class, () ->
        {
            restaurante.cerrarYGuardarPedido();
        });
    }

    @Test
    void testCerrarYGuardarPedido() throws Exception
    {
        restaurante.iniciarPedido("Samuel", "Calle 123");
        restaurante.getPedidoEnCurso().agregarProducto(new ProductoMenu("hamburguesa", 10000));

        int id = restaurante.getPedidoEnCurso().getIdPedido();

        restaurante.cerrarYGuardarPedido();

        File factura = new File("./facturas/factura_" + id + ".txt");

        assertNull(restaurante.getPedidoEnCurso());
        assertEquals(1, restaurante.getPedidos().size());
        assertTrue(factura.exists());
    }

    @Test
    void testCerrarPedidoConVariosProductos() throws Exception
    {
        restaurante.iniciarPedido("Samuel", "Calle 123");

        restaurante.getPedidoEnCurso().agregarProducto(new ProductoMenu("hamburguesa", 10000));
        restaurante.getPedidoEnCurso().agregarProducto(new ProductoMenu("papas", 5000));

        restaurante.cerrarYGuardarPedido();

        assertEquals(1, restaurante.getPedidos().size());
    }

    @Test
    void testCargaCompleta() throws Exception
    {
        File ingredientes = crearArchivo("ing", "queso;2000\ntomate;1000\n");
        File menu = crearArchivo("men", "hamburguesa;12000\npapas;6000\n");
        File combos = crearArchivo("com", "combo;10%;hamburguesa;papas\n");

        restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);

        assertEquals(2, restaurante.getIngredientes().size());
        assertEquals(2, restaurante.getMenuBase().size());
        assertEquals(1, restaurante.getMenuCombos().size());
    }

    @Test
    void testLineasVacias() throws Exception
    {
        File ingredientes = crearArchivo("ing", "\nqueso;2000\n\n");
        File menu = crearArchivo("men", "\nhamburguesa;12000\n\n");
        File combos = crearArchivo("com", "\ncombo;10%;hamburguesa\n\n");

        restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);

        assertEquals(1, restaurante.getIngredientes().size());
        assertEquals(1, restaurante.getMenuBase().size());
        assertEquals(1, restaurante.getMenuCombos().size());
    }

    @Test
    void testIngredienteRepetido()
    {
        assertThrows(IngredienteRepetidoException.class, () ->
        {
            File ingredientes = crearArchivo("ing", "queso;2000\nqueso;3000\n");
            File menu = crearArchivo("men", "hamburguesa;12000\n");
            File combos = crearArchivo("com", "combo;10%;hamburguesa\n");

            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        });
    }

    @Test
    void testProductoRepetido()
    {
        assertThrows(ProductoRepetidoException.class, () ->
        {
            File ingredientes = crearArchivo("ing", "queso;2000\n");
            File menu = crearArchivo("men", "hamburguesa;12000\nhamburguesa;13000\n");
            File combos = crearArchivo("com", "combo;10%;hamburguesa\n");

            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        });
    }

    @Test
    void testProductoFaltante()
    {
        assertThrows(ProductoFaltanteException.class, () ->
        {
            File ingredientes = crearArchivo("ing", "queso;2000\n");
            File menu = crearArchivo("men", "hamburguesa;12000\n");
            File combos = crearArchivo("com", "combo;10%;papas\n");

            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        });
    }

    @Test
    void testErroresFormato()
    {
        assertThrows(Exception.class, () ->
        {
            File ingredientes = crearArchivo("ing", "queso\n");
            File menu = crearArchivo("men", "hamburguesa\n");
            File combos = crearArchivo("com", "combo\n");

            restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);
        });
    }

    @Test
    void testComboMultiplesProductos() throws Exception
    {
        File ingredientes = crearArchivo("ing", "queso;2000\n");
        File menu = crearArchivo("men", "papas;6000\nhamburguesa;12000\n");
        File combos = crearArchivo("com", "combo;10%;hamburguesa;papas\n");

        restaurante.cargarInformacionRestaurante(ingredientes, menu, combos);

        assertEquals(1, restaurante.getMenuCombos().size());
    }

    private File crearArchivo(String nombre, String contenido) throws Exception // este metodo permite simular archivos de entrada sin necesidad de tener archivos físicos en el proyecto
    {                                                                           // osea la funcion de este es que crea virtualmente los archivos txt para testearlos en vez de meter
        File archivo = File.createTempFile(nombre, ".txt");                     // archivos txt con toda la info para testear
        PrintWriter writer = new PrintWriter(archivo);
        writer.print(contenido);
        writer.close();
        return archivo;
    }
}