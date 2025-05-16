package com.nahuel.mongodb.serializador;
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
import com.nahuel.mongodb.Sucursal;
import com.nahuel.mongodb.Venta;
import com.nahuel.mongodb.Producto.TipoProducto;
import com.nahuel.mongodb.Venta.FormaPago;

import java.util.*;

import org.bson.Document;

public class SerializadorVenta {
    public static void main(String[] args) {
    	Random random = new Random();
        ObraSocial obraSocial = new ObraSocial("OSDE", "osde123");
        Cliente cliente = new Cliente("c1", "Juan", "Pérez", "12345678", "Calle falsa 123", "Lanus", "Buenos Aires", obraSocial, "987654");
        Sucursal sucursal = new Sucursal("s1", "Calle falsa", "123", "Microcentro", "Ciudad Autonoma de Buenos Aires");
        Empleado empleado = new Empleado("e1", "Laura", "Gómez", "23456789", "20-23456789-3", "Calle falsa 123", "Lanus", "Buenos Aires", obraSocial, "123456", sucursal, true);
        
        List<DetalleVenta> detalleVenta = new ArrayList<>(Arrays.asList(
        	    new DetalleVenta(new Producto("p1", "MED001", "Ibuprofeno 600mg", TipoProducto.MEDICAMENTO, "Bayer"), 1, Math.round((10 + (990 * random.nextDouble())) * 100.0) / 100.0),
        	    new DetalleVenta(new Producto("p2", "PERF001", "Shampoo Anticaspa", TipoProducto.PERFUMERIA, "Pantene"), 3, Math.round((10 + (990 * random.nextDouble())) * 100.0) / 100.0)
        	));
        
        
        Venta venta = new Venta("v1", "0001-000123", new Date(), FormaPago.TARJETA, cliente, empleado, empleado, detalleVenta, empleado.getSucursal());

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(venta);
        
	     // Conexión a MongoDB
	    String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("mi_base_de_datos");
            MongoCollection<Document> collection = database.getCollection("ventas");

            // Conversión del JSON a Document de MongoDB
            Document documentoVenta = Document.parse(json);

            // Inserción
            collection.insertOne(documentoVenta);

            System.out.println("Venta insertada correctamente en MongoDB.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(json);
    }
}

