package consultas;
import com.mongodb.client.*;
import org.bson.Document;
import java.text.SimpleDateFormat;
import java.util.*;

public class SegundaConsultaVentasPorObraSocial {

    public static void consultarVentasPorObraSocial(MongoDatabase db, Date fechaDesde, Date fechaHasta) {
        MongoCollection<Document> ventasCollection = db.getCollection("ventas");

        // Filtro por fechas
        Document filtroFechas = new Document("fecha", new Document("$gte", fechaDesde).append("$lte", fechaHasta));
        FindIterable<Document> ventasFiltradas = ventasCollection.find(filtroFechas);

        // Map<sucursal, Map<obraSocial, total>>
        Map<String, Map<String, Double>> totalesPorSucursalYObraSocial = new HashMap<>();
        double totalGeneral = 0;

        System.out.println("Detalle de ventas por obra social entre " + fechaDesde + " y " + fechaHasta + ":\n");

        for (Document venta : ventasFiltradas) {
            String nroTicket = venta.getString("nroTicket");
            Date fecha = venta.getDate("fecha");
            double total = venta.getDouble("total");

            Document vendedor = (Document) venta.get("vendedor");
            Document sucursal = (Document) venta.get("sucursalVenta");
            String idSucursal = sucursal.getString("idSucursal");

            Document cliente = (Document) venta.get("cliente");
            String obraSocial;
            if (cliente.containsKey("obraSocial") && cliente.get("obraSocial") != null) {
                Document os = (Document) cliente.get("obraSocial");
                obraSocial = os.getString("nombre");
            } else {
                obraSocial = "PRIVADO";
            }

            // Sumar totales
            totalGeneral += total;
            totalesPorSucursalYObraSocial
                .computeIfAbsent(idSucursal, k -> new HashMap<>())
                .merge(obraSocial, total, Double::sum);

            // Detalle individual
            System.out.println("Ticket: " + nroTicket + ", Fecha: " + fecha + ", Sucursal: " + idSucursal + ", Obra Social: " + obraSocial + ", Total: $" + total);
        }

        System.out.println("\n--- Totales por Sucursal y Obra Social ---");
        for (Map.Entry<String, Map<String, Double>> entrySucursal : totalesPorSucursalYObraSocial.entrySet()) {
            String sucursal = entrySucursal.getKey();
            System.out.println("Sucursal " + sucursal + ":");
            Map<String, Double> totalesObraSocial = entrySucursal.getValue();
            for (Map.Entry<String, Double> entryOS : totalesObraSocial.entrySet()) {
                System.out.println("  Obra Social " + entryOS.getKey() + ": $" + entryOS.getValue());
            }
        }

        System.out.println("\nTotal general: $" + totalGeneral);
    }

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("farmacia");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date desde = sdf.parse("2023-01-01");
        Date hasta = sdf.parse("2025-12-31");

        consultarVentasPorObraSocial(db, desde, hasta);
    }
}
