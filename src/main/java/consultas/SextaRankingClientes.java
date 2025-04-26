package consultas;
import com.mongodb.client.*;
import org.bson.Document;
import java.util.*;

public class SextaRankingClientes {

    public static void rankingClientes(MongoDatabase db) {
        MongoCollection<Document> ventasCollection = db.getCollection("ventas");

        FindIterable<Document> ventas = ventasCollection.find();

        // Map<idCliente, DatosCliente> para sumar los totales
        Map<String, DatosCliente> comprasPorCliente = new HashMap<>();

        for (Document venta : ventas) {
            Document clienteDoc = (Document) venta.get("cliente");
            if (clienteDoc == null) continue; // Por las dudas

            String idCliente = clienteDoc.getString("idCliente");
            String nombre = clienteDoc.getString("nombre");
            String apellido = clienteDoc.getString("apellido");
            double total = venta.getDouble("total");

            // Si el cliente ya existe, sumamos
            DatosCliente datos = comprasPorCliente.getOrDefault(idCliente, new DatosCliente(idCliente, nombre, apellido, 0));
            datos.totalComprado += total;
            comprasPorCliente.put(idCliente, datos);
        }

        // Convertimos a lista para ordenar
        List<DatosCliente> ranking = new ArrayList<>(comprasPorCliente.values());
        ranking.sort((a, b) -> Double.compare(b.totalComprado, a.totalComprado)); // De mayor a menor

        // Mostramos el ranking
        System.out.println("=== Ranking de Clientes por Total de Compras ===");
        for (int i = 0; i < ranking.size(); i++) {
            DatosCliente cliente = ranking.get(i);
            System.out.printf("%d. %s %s (ID: %s) - Total Comprado: $%.2f%n",
                    i + 1, cliente.nombre, cliente.apellido, cliente.idCliente, cliente.totalComprado);
        }
    }

    // Clase auxiliar para guardar datos del cliente
    static class DatosCliente {
        String idCliente;
        String nombre;
        String apellido;
        double totalComprado;

        DatosCliente(String idCliente, String nombre, String apellido, double totalComprado) {
            this.idCliente = idCliente;
            this.nombre = nombre;
            this.apellido = apellido;
            this.totalComprado = totalComprado;
        }
    }

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("farmacia");

        rankingClientes(db);
    }
}
