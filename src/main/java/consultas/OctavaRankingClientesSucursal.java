package consultas;
import com.mongodb.client.*;
import org.bson.Document;
import java.util.*;

public class OctavaRankingClientesSucursal {

    public static void rankingComprasPorClienteYSucursal(MongoDatabase db) {
        MongoCollection<Document> ventasCollection = db.getCollection("ventas");

        FindIterable<Document> todasLasVentas = ventasCollection.find();

        // Map<sucursal, Map<cliente, InfoCliente>>
        Map<String, Map<String, InfoCliente>> rankingPorSucursal = new HashMap<>();

        for (Document venta : todasLasVentas) {
            Document sucursalVenta = (Document) venta.get("sucursalVenta");
            String idSucursal = sucursalVenta.getString("idSucursal");

            Document clienteDoc = (Document) venta.get("cliente");
            String idCliente = clienteDoc.getString("idCliente");
            String nombreCompleto = clienteDoc.getString("nombre") + " " + clienteDoc.getString("apellido");

            double totalVenta = venta.getDouble("total");

            rankingPorSucursal
                .computeIfAbsent(idSucursal, k -> new HashMap<>())
                .computeIfAbsent(idCliente, k -> new InfoCliente(nombreCompleto))
                .agregarCompra(totalVenta);
        }

        // Mostrar el ranking
        for (Map.Entry<String, Map<String, InfoCliente>> entrySucursal : rankingPorSucursal.entrySet()) {
            String idSucursal = entrySucursal.getKey();
            System.out.println("\n--- Ranking de clientes en Sucursal " + idSucursal + " ---");

            List<InfoCliente> clientes = new ArrayList<>(entrySucursal.getValue().values());
            // Ordenar por total gastado descendente
            clientes.sort((a, b) -> Double.compare(b.totalGastado, a.totalGastado));

            for (InfoCliente cliente : clientes) {
                System.out.println(cliente.nombreCompleto + " - Compras: " + cliente.cantidadCompras + ", Total Gastado: $" + cliente.totalGastado);
            }
        }
    }

    // Clase auxiliar para guardar info del cliente
    private static class InfoCliente {
        String nombreCompleto;
        int cantidadCompras;
        double totalGastado;

        InfoCliente(String nombreCompleto) {
            this.nombreCompleto = nombreCompleto;
            this.cantidadCompras = 0;
            this.totalGastado = 0.0;
        }

        void agregarCompra(double total) {
            this.cantidadCompras++;
            this.totalGastado += total;
        }
    }

    public static void main(String[] args) {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("farmacia");

        rankingComprasPorClienteYSucursal(db);
    }
}
