package consultas;
import com.mongodb.client.*;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;

public class SextaConsultaRankingProductos {

    public static void rankingProductosVendidos(MongoDatabase db, Date fechaDesde, Date fechaHasta) {
        MongoCollection<Document> ventasCollection = db.getCollection("ventas");

        // Filtro por fechas
        Document filtroFechas = new Document("fecha", new Document("$gte", fechaDesde).append("$lte", fechaHasta));
        FindIterable<Document> ventasFiltradas = ventasCollection.find(filtroFechas);

        // Mapa para ranking general: Map<descripcionProducto, cantidadTotal>
        Map<String, Integer> rankingGlobal = new HashMap<>();

        // Mapa para ranking por sucursal: Map<sucursal, Map<descripcionProducto, cantidad>>
        Map<String, Map<String, Integer>> rankingPorSucursal = new HashMap<>();

        for (Document venta : ventasFiltradas) {
            List<Document> productos = (List<Document>) venta.get("productos");
            Document vendedor = (Document) venta.get("vendedor");
            Document sucursal = (Document) vendedor.get("sucursal");
            String idSucursal = sucursal.getString("idSucursal");

            for (Document producto : productos) {
                String descripcion = producto.getString("descripcion");
                int cantidad = producto.getInteger("cantidad", 0);

                // Ranking global
                rankingGlobal.merge(descripcion, cantidad, Integer::sum);

                // Ranking por sucursal
                rankingPorSucursal
                    .computeIfAbsent(idSucursal, k -> new HashMap<>())
                    .merge(descripcion, cantidad, Integer::sum);
            }
        }

        // Ordenar y mostrar ranking global
        System.out.println("📦 Ranking global de productos vendidos:");
        rankingGlobal.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
            .forEach(entry -> System.out.println("Producto: " + entry.getKey() + " - Cantidad vendida: " + entry.getValue()));

        // Ranking por sucursal
        System.out.println("\n🏬 Ranking por sucursal:");
        for (String sucursal : rankingPorSucursal.keySet()) {
            System.out.println("\nSucursal " + sucursal + ":");
            Map<String, Integer> productosSucursal = rankingPorSucursal.get(sucursal);
            productosSucursal.entrySet().stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .forEach(entry -> System.out.println("Producto: " + entry.getKey() + " - Cantidad vendida: " + entry.getValue()));
        }
    }

    // Ejemplo de uso
    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("farmacia");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date desde = sdf.parse("2023-01-01");
        Date hasta = sdf.parse("2025-12-31");

        rankingProductosVendidos(db, desde, hasta);
    }
}
