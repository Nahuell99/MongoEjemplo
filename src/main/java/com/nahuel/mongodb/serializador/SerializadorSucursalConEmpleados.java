package com.nahuel.mongodb.serializador;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nahuel.mongodb.Empleado;
import com.nahuel.mongodb.ObraSocial;
import com.nahuel.mongodb.Sucursal;

public class SerializadorSucursalConEmpleados {
    public static void main(String[] args) {
        ObraSocial osde = new ObraSocial("OSDE", "osde123");
        Sucursal sucursal = new Sucursal("s2", "Av. Siempreviva", "742", "Springfield", "Illinois");

        Empleado empleado1 = new Empleado("e2", "Hugo", "Fernández", "22333444", "20-22333444-3", 
            "Calle 1", "Springfield", "Illinois", osde, "998877", sucursal, true);

        Empleado empleado2 = new Empleado("e3", "Marta", "López", "33445566", "27-33445566-5",
            "Calle 2", "Springfield", "Illinois", osde, "112233", sucursal, false);

        List<Empleado> empleados = Arrays.asList(empleado1, empleado2);

        // Estructura contenedora
        Map<String, Object> sucursalConEmpleados = new LinkedHashMap<>();
        sucursalConEmpleados.put("sucursal", sucursal);
        sucursalConEmpleados.put("empleados", empleados);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(sucursalConEmpleados);

        // Conexión a MongoDB
	    String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("mi_base_de_datos");
            MongoCollection<Document> collection = database.getCollection("SucursalConEmpleados");

            // Conversión del JSON a Document de MongoDB
            Document documentoVenta = Document.parse(json);

            // Inserción
            collection.insertOne(documentoVenta);

            System.out.println("SucursalConEmpleados insertada correctamente en MongoDB.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        System.out.println(json);
    }
}