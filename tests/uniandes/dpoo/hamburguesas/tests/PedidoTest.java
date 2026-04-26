package uniandes.dpoo.hamburguesas.tests;

import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.nio.file.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import uniandes.dpoo.hamburguesas.mundo.Pedido;
import uniandes.dpoo.hamburguesas.mundo.ProductoMenu;

public class PedidoTest {

	private Pedido pedido;

    @BeforeEach
    void setUp()
    {
        pedido = new Pedido( "Samuel", "Calle 123" );
    }

    @Test
    void testGetNombreCliente()
    {
        assertEquals( "Samuel", pedido.getNombreCliente() );
    }

    @Test
    void testIdsDiferentes()
    {
        Pedido otro = new Pedido( "Laura", "Carrera 45" );

        assertEquals( pedido.getIdPedido() + 1, otro.getIdPedido() );
    }

    @Test
    void testAgregarProductoYPrecioTotal()
    {
        pedido.agregarProducto( new ProductoMenu( "hamburguesa", 10000 ) );
        pedido.agregarProducto( new ProductoMenu( "papas", 5000 ) );

        assertEquals( 17850, pedido.getPrecioTotalPedido() );
    }

    @Test
    void testGenerarTextoFactura()
    {
        pedido.agregarProducto( new ProductoMenu( "hamburguesa", 10000 ) );

        String texto = pedido.generarTextoFactura();

        assertTrue( texto.contains( "Cliente: Samuel" ) );
        assertTrue( texto.contains( "Dirección: Calle 123" ) );
        assertTrue( texto.contains( "hamburguesa" ) );
        assertTrue( texto.contains( "Precio Neto:  10000" ) );
        assertTrue( texto.contains( "IVA:          1900" ) );
        assertTrue( texto.contains( "Precio Total: 11900" ) );
    }

    @Test
    void testGuardarFactura() throws Exception
    {
        pedido.agregarProducto( new ProductoMenu( "hamburguesa", 10000 ) );

        File archivo = File.createTempFile( "factura_prueba", ".txt" );
        pedido.guardarFactura( archivo );

        String contenido = Files.readString( archivo.toPath() );

        assertTrue( contenido.contains( "Cliente: Samuel" ) );
        assertTrue( contenido.contains( "hamburguesa" ) );

        archivo.delete();
    }
}
