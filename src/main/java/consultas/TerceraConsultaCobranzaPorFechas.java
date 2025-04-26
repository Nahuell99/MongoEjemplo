package consultas;

import com.mongodb.client.*;
import org.bson.Document;
import java.text.SimpleDateFormat;
import java.util.*;

public class TerceraConsultaCobranzaPorFechas {

    public static void consultarCobranzaPorRangoFechas(MongoDatabase db, Date fechaDesde, Date fechaHasta) {
        MongoCollection<Document> ventasCollection = db.getCollection("ventas");

        // Filtro por fechas
        Document filtroFechas = new Document("fecha", new Document("$gte", fechaDesde).append("$lte", fechaHasta));
        FindIterable<Document> ventasFiltradas = ventasCollection.find(filtroFechas);

        Map<String, Double> cobranzaPorSucursal = new HashMap<>();
        double cobranzaTotalGeneral = 0;

        for (Document venta : ventasFiltradas) {
            double total = venta.getDouble("total");

            // Accedemos a sucursalVenta
            Document sucursalVenta = (Document) venta.get("sucursalVenta");
            String idSucursal = sucursalVenta.getString("idSucursal");

            // Sumamos a totales
            cobranzaTotalGeneral += total;
            cobranzaPorSucursal.merge(idSucursal, total, Double::sum);
        }

        // Mostramos los resultados
        System.out.println("Cobranza total de la cadena entre " + fechaDesde + " y " + fechaHasta + ": $" + cobranzaTotalGeneral);

        System.out.println("\n--- Cobranza agrupada por sucursales ---");
        for (Map.Entry<String, Double> entry : cobranzaPorSucursal.entrySet()) {
            System.out.println("Sucursal " + entry.getKey() + ": $" + entry.getValue());
        }
    }

    // Ejemplo de uso:
    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("farmacia");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date desde = sdf.parse("2023-01-01");
        Date hasta = sdf.parse("2025-12-31");

        consultarCobranzaPorRangoFechas(db, desde, hasta);
    }
}
