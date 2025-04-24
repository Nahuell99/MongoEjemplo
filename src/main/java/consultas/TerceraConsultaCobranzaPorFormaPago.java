package consultas;
import com.mongodb.client.*;
import org.bson.Document;
import java.text.SimpleDateFormat;
import java.util.*;

public class TerceraConsultaCobranzaPorFormaPago {

    public static void consultarCobranzaPorFormaPago(MongoDatabase db, Date fechaDesde, Date fechaHasta) {
        MongoCollection<Document> ventasCollection = db.getCollection("ventas");

        // Filtro por fechas
        Document filtroFechas = new Document("fecha", new Document("$gte", fechaDesde).append("$lte", fechaHasta));
        FindIterable<Document> ventasFiltradas = ventasCollection.find(filtroFechas);

        // Map<sucursal, Map<formaPago, total>>
        Map<String, Map<String, Double>> totalesPorSucursalYFormaPago = new HashMap<>();
        double totalGeneral = 0;

        System.out.println("Detalle de cobranza entre " + fechaDesde + " y " + fechaHasta + ":\n");

        for (Document venta : ventasFiltradas) {
            String nroTicket = venta.getString("nroTicket");
            Date fecha = venta.getDate("fecha");
            double total = venta.getDouble("total");
            String formaPago = venta.getString("formaPago");

            Document cobrador = (Document) venta.get("cobrador");
            Document sucursal = (Document) cobrador.get("sucursal");
            String idSucursal = sucursal.getString("idSucursal");

            // Sumar totales
            totalGeneral += total;
            totalesPorSucursalYFormaPago
                .computeIfAbsent(idSucursal, k -> new HashMap<>())
                .merge(formaPago, total, Double::sum);

            // Detalle individual
            System.out.println("Ticket: " + nroTicket + ", Fecha: " + fecha + ", Sucursal: " + idSucursal + ", Forma de pago: " + formaPago + ", Total: $" + total);
        }

        // Mostrar totales agrupados
        System.out.println("\n--- Totales por Sucursal y Forma de Pago ---");
        for (Map.Entry<String, Map<String, Double>> entrySucursal : totalesPorSucursalYFormaPago.entrySet()) {
            String sucursal = entrySucursal.getKey();
            System.out.println("Sucursal " + sucursal + ":");
            Map<String, Double> totalesPorForma = entrySucursal.getValue();
            for (Map.Entry<String, Double> entryPago : totalesPorForma.entrySet()) {
                System.out.println("  Forma de pago " + entryPago.getKey() + ": $" + entryPago.getValue());
            }
        }

        System.out.println("\nTotal general de cobranza: $" + totalGeneral);
    }

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("farmacia");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date desde = sdf.parse("2023-01-01");
        Date hasta = sdf.parse("2025-12-31");

        consultarCobranzaPorFormaPago(db, desde, hasta);
    }
}
