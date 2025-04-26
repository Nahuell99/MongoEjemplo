package consultas;
import com.mongodb.client.*;
import org.bson.Document;
import java.text.SimpleDateFormat;
import java.util.*;

public class PrimeraConsultaCantidadVentas {

    public static void consultarCantidadVentasEntreFechas(MongoDatabase db, Date fechaDesde, Date fechaHasta) {
        MongoCollection<Document> ventasCollection = db.getCollection("ventas");

        // Filtro por rango de fechas
        Document filtroFechas = new Document("fecha", new Document("$gte", fechaDesde).append("$lte", fechaHasta));

        FindIterable<Document> ventasFiltradas = ventasCollection.find(filtroFechas);

        int totalVentas = 0;
        Map<String, Integer> ventasPorSucursal = new HashMap<>();

        for (Document venta : ventasFiltradas) {
            totalVentas++;

            Document sucursalVenta = (Document) venta.get("sucursalVenta");
            String idSucursal = sucursalVenta.getString("idSucursal");

            ventasPorSucursal.merge(idSucursal, 1, Integer::sum);
        }

        System.out.println("Cantidad total de ventas entre " + fechaDesde + " y " + fechaHasta + ": " + totalVentas);

        System.out.println("\n--- Cantidad de ventas por sucursal ---");
        for (Map.Entry<String, Integer> entry : ventasPorSucursal.entrySet()) {
            System.out.println("Sucursal " + entry.getKey() + ": " + entry.getValue() + " ventas");
        }
    }

    // Ejemplo de uso:
    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("farmacia");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date desde = sdf.parse("2023-01-01");
        Date hasta = sdf.parse("2025-12-31");

        consultarCantidadVentasEntreFechas(db, desde, hasta);
    }
}
