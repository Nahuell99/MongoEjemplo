package com.nahuel.mongodb;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class MongoDBExample {
    public static void main(String[] args) {
        // Reemplazá con tu URI si es diferente
        String uri = "mongodb://localhost:27017";
        try (MongoClient mongoClient = MongoClients.create(uri)) {
            MongoDatabase database = mongoClient.getDatabase("mi_base_de_datos");
            System.out.println("Conectado a la base de datos: " + database.getName());
            
            
            // Crear o acceder a una colección llamada "usuarios"
            MongoCollection<Document> collection = database.getCollection("usuarios");

            // Crear un documento (equivale a una "fila")
            Document nuevoUsuario = new Document("nombre", "Lucia")
                    .append("edad", 28)
                    .append("activo", true);

            // Insertar el documento en la colección
            collection.insertOne(nuevoUsuario);

            System.out.println("Documento insertado correctamente.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

