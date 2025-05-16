package com.nahuel.mongodb.serializador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nahuel.mongodb.Cliente;
import com.nahuel.mongodb.DetalleVenta;
import com.nahuel.mongodb.Empleado;
import com.nahuel.mongodb.ObraSocial;
import com.nahuel.mongodb.Producto;
import com.nahuel.mongodb.Producto.TipoProducto;
import com.nahuel.mongodb.Sucursal;
import com.nahuel.mongodb.Venta;
import com.nahuel.mongodb.Venta.FormaPago;

public class SerializadorVentasDeSucursal {
    public static void main(String[] args) {
    	Random random = new Random();
    	ObraSocial swissMedical = new ObraSocial("Swiss Medical", "swiss789");
        Sucursal sucursal = new Sucursal("s3", "Ruta 3", "999", "La Matanza", "Buenos Aires");

        Empleado vendedor1 = new Empleado("e4", "Carlos", "Paz", "22333444", "20-22333444-3", 
            "Calle 3", "La Matanza", "Buenos Aires", swissMedical, "776655", sucursal, true);

        Cliente cliente1 = new Cliente("c2", "María", "Juárez", "44332211", "Calle 4", "Morón", "Buenos Aires", swissMedical, "665544");

        List<DetalleVenta> detalleVenta1 = new ArrayList<>(Arrays.asList(
        	    new DetalleVenta(new Producto("p1", "MED001", "Ibuprofeno 600mg", TipoProducto.MEDICAMENTO, "Bayer"), 1, Math.round((10 + (990 * random.nextDouble())) * 100.0) / 100.0),
        	    new DetalleVenta(new Producto("p2", "PERF001", "Shampoo Anticaspa", TipoProducto.PERFUMERIA, "Pantene"), 3, Math.round((10 + (990 * random.nextDouble())) * 100.0) / 100.0)
        	));
        
        
        Venta venta1 = new Venta("v1", "0001-000123", new Date(), FormaPago.TARJETA, cliente1, vendedor1, vendedor1, detalleVenta1, vendedor1.getSucursal());

        // Simulamos otra venta
        
        List<DetalleVenta> detalleVenta2 = new ArrayList<>(Arrays.asList(
        	    new DetalleVenta(new Producto("p5", "MED003", "Amoxicilina", TipoProducto.MEDICAMENTO, "Roemmers"), 3, Math.round((10 + (990 * random.nextDouble())) * 100.0) / 100.0)
        	));
        
        Venta venta2 = new Venta("v3", "0002-000457", new Date(), FormaPago.DEBITO, cliente1, vendedor1, vendedor1, detalleVenta2, vendedor1.getSucursal());

        List<Venta> ventas = Arrays.asList(venta1, venta2);

        // Estructura contenedora
        Map<String, Object> ventasDeSucursal = new LinkedHashMap<>();
        ventasDeSucursal.put("sucursal", sucursal);
        ventasDeSucursal.put("ventas", ventas);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(ventasDeSucursal);

        // Conexión a MongoDB
	    String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("mi_base_de_datos");
            MongoCollection<Document> collection = database.getCollection("VentasDeSucursal");

            // Conversión del JSON a Document de MongoDB
            Document documentoVenta = Document.parse(json);

            // Inserción
            collection.insertOne(documentoVenta);

            System.out.println("VentasDeSucursal insertada correctamente en MongoDB.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println(json);
    }
}
