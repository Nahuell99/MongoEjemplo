package consultas;

import com.mongodb.client.*;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;

public class OctavaConsultaRankingClientes {

    public static void rankingClientesPorCantidad(MongoDatabase db, Date fechaDesde, Date fechaHasta) {
        MongoCollection<Document> ventasCollection = db.getCollection("ventas");

        Document filtroFechas = new Document("fecha", new Document("$gte", fechaDesde).append("$lte", fechaHasta));
        FindIterable<Document> ventasFiltradas = ventasCollection.find(filtroFechas);

        Map<String, Integer> rankingGeneral = new HashMap<>();
        Map<String, Map<String, Integer>> rankingPorSucursal = new HashMap<>();

        for (Document venta : ventasFiltradas) {
            Document cliente = venta.get("cliente", Document.class);
            String idCliente = cliente.getString("idCliente");
            String nombreCliente = cliente.getString("nombre") + " " + cliente.getString("apellido");

            Document vendedor = venta.get("vendedor", Document.class);
            Document sucursal = vendedor.get("sucursal", Document.class);
            String idSucursal = sucursal.getString("idSucursal");

            List<Document> productos = venta.getList("productos", Document.class);
            int totalCantidad = productos.stream()
                    .mapToInt(p -> p.getInteger("cantidad", 0))
                    .sum();

            // Clave cliente para ranking general y por sucursal
            String claveCliente = idCliente + " - " + nombreCliente;

            // Ranking general
            rankingGeneral.merge(claveCliente, totalCantidad, Integer::sum);

            // Ranking por sucursal
            rankingPorSucursal
                .computeIfAbsent(idSucursal, k -> new HashMap<>())
                .merge(claveCliente, totalCantidad, Integer::sum);
        }

        // Mostrar ranking general
        System.out.println("===== RANKING GENERAL DE CLIENTES POR CANTIDAD DE PRODUCTOS COMPRADOS =====");
        rankingGeneral.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue() + " unidades"));

        // Mostrar ranking por sucursal
        System.out.println("\n===== RANKING POR SUCURSAL =====");
        for (Map.Entry<String, Map<String, Integer>> entrySucursal : rankingPorSucursal.entrySet()) {
            String sucursal = entrySucursal.getKey();
            System.out.println("\nSucursal " + sucursal + ":");

            entrySucursal.getValue().entrySet().stream()
                    .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                    .forEach(entry -> System.out.println(entry.getKey() + " -> " + entry.getValue() + " unidades"));
        }
    }

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("farmacia");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date desde = sdf.parse("2023-01-01");
        Date hasta = sdf.parse("2025-12-31");

        rankingClientesPorCantidad(db, desde, hasta);
    }
}
