package consultas;
import com.mongodb.client.*;
import org.bson.Document;
import java.text.SimpleDateFormat;
import java.util.*;

public class PrimeraConsultaVentasPorFechas {

    public static void consultarVentasPorRangoFechas(MongoDatabase db, Date fechaDesde, Date fechaHasta) {
        MongoCollection<Document> ventasCollection = db.getCollection("ventas");

        // Filtro por fechas
        Document filtroFechas = new Document("fecha", new Document("$gte", fechaDesde).append("$lte", fechaHasta));

        FindIterable<Document> ventasFiltradas = ventasCollection.find(filtroFechas);

        Map<String, Double> totalesPorSucursal = new HashMap<>();
        double totalGeneral = 0;

        System.out.println("Detalle de ventas entre " + fechaDesde + " y " + fechaHasta + ":");

        for (Document venta : ventasFiltradas) {
            String nroTicket = venta.getString("nroTicket");
            Date fecha = venta.getDate("fecha");
            double total = venta.getDouble("total");

            Document empleadoVenta = (Document) venta.get("vendedor");
            Document sucursal = (Document) empleadoVenta.get("sucursal");
            String idSucursal = sucursal.getString("idSucursal");

            // Sumar al total general y al total por sucursal
            totalGeneral += total;
            totalesPorSucursal.merge(idSucursal, total, Double::sum);

            System.out.println("Ticket: " + nroTicket + ", Fecha: " + fecha + ", Sucursal: " + idSucursal + ", Total: $" + total);
        }

        System.out.println("\n--- Totales por Sucursal ---");
        for (Map.Entry<String, Double> entry : totalesPorSucursal.entrySet()) {
            System.out.println("Sucursal " + entry.getKey() + ": $" + entry.getValue());
        }

        System.out.println("\nTotal general: $" + totalGeneral);
    }

    // Ejemplo de uso:
    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("farmacia");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date desde = sdf.parse("2023-01-01");
        Date hasta = sdf.parse("2025-12-31");

        consultarVentasPorRangoFechas(db, desde, hasta);
    }
}
