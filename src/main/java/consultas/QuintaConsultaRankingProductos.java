package consultas;

import com.mongodb.client.*;
import org.bson.Document;
import java.text.SimpleDateFormat;
import java.util.*;

public class QuintaConsultaRankingProductos {

    public static void rankingProductosPorMonto(MongoDatabase db, Date fechaDesde, Date fechaHasta) {
        MongoCollection<Document> ventasCollection = db.getCollection("ventas");

        Document filtroFechas = new Document("fecha", new Document("$gte", fechaDesde).append("$lte", fechaHasta));
        FindIterable<Document> ventasFiltradas = ventasCollection.find(filtroFechas);

        Map<String, Double> totalPorProductoGlobal = new HashMap<>();
        Map<String, Map<String, Double>> totalPorProductoPorSucursal = new HashMap<>();

        for (Document venta : ventasFiltradas) {
            Document vendedor = (Document) venta.get("vendedor");
            Document sucursal = (Document) vendedor.get("sucursal");
            String idSucursal = sucursal.getString("idSucursal");

            List<Document> productos = (List<Document>) venta.get("productos");
            for (Document prod : productos) {
                String descripcion = prod.getString("descripcion");
                double precioUnitario = prod.getDouble("precioUnitario");
                int cantidad = prod.getInteger("cantidad");
                double monto = precioUnitario * cantidad;

                // Global
                totalPorProductoGlobal.merge(descripcion, monto, Double::sum);

                // Por sucursal
                totalPorProductoPorSucursal
                    .computeIfAbsent(idSucursal, k -> new HashMap<>())
                    .merge(descripcion, monto, Double::sum);
            }
        }

        // Ranking global
        System.out.println("--- RANKING GLOBAL DE PRODUCTOS POR MONTO ---");
        totalPorProductoGlobal.entrySet().stream()
            .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
            .forEach(entry -> System.out.println("Producto: " + entry.getKey() + " | Monto Total: $" + entry.getValue()));

        // Ranking por sucursal
        System.out.println("\n--- RANKING POR SUCURSAL ---");
        for (Map.Entry<String, Map<String, Double>> entrySucursal : totalPorProductoPorSucursal.entrySet()) {
            String sucursal = entrySucursal.getKey();
            System.out.println("Sucursal " + sucursal + ":");

            entrySucursal.getValue().entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> System.out.println("  Producto: " + entry.getKey() + " | Monto Total: $" + entry.getValue()));
        }
    }

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("farmacia");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date desde = sdf.parse("2023-01-01");
        Date hasta = sdf.parse("2025-12-31");

        rankingProductosPorMonto(db, desde, hasta);
    }
}
