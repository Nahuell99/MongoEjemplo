package consultas;

import com.mongodb.client.*;
import com.mongodb.client.model.*;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.text.SimpleDateFormat;
import java.util.*;

public class CuartaConsultaVentasPorTipoProducto {

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase database = mongoClient.getDatabase("farmacia");
        MongoCollection<Document> ventas = database.getCollection("ventas");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date desde = sdf.parse("2024-01-01");
        Date hasta = sdf.parse("2024-12-31");

        List<Bson> pipeline = Arrays.asList(
            Aggregates.match(Filters.and(
                Filters.gte("fecha", desde),
                Filters.lte("fecha", hasta)
            )),
            Aggregates.unwind("$productos"),
            Aggregates.group(
                new Document()
                    .append("sucursal", "$vendedor.sucursal.idSucursal")
                    .append("tipoProducto", "$productos.tipo"),
                Accumulators.sum("totalVentas", new Document("$multiply", Arrays.asList("$productos.precioUnitario", "$productos.cantidad"))),
                Accumulators.sum("cantidadVendida", "$productos.cantidad")
            ),
            Aggregates.sort(Sorts.ascending("_id.sucursal", "_id.tipoProducto"))
        );

        AggregateIterable<Document> resultados = ventas.aggregate(pipeline);

        // Map para acumular el total general por tipo de producto
        Map<String, Double> totalVentasPorTipo = new HashMap<>();
        Map<String, Integer> cantidadVendidaPorTipo = new HashMap<>();

        System.out.println("Detalle y Totales de Ventas de Productos:");
        for (Document doc : resultados) {
            Document id = doc.get("_id", Document.class);
            String sucursal = id.getString("sucursal") != null ? id.getString("sucursal") : "CADENA COMPLETA";
            String tipoProducto = id.getString("tipoProducto");
            double totalVentas = doc.getDouble("totalVentas");
            int cantidadVendida = doc.getInteger("cantidadVendida");

            System.out.printf("- Sucursal: %s | Tipo: %s | Total Vendido: $%.2f | Cantidad: %d\n",
                    sucursal, tipoProducto, totalVentas, cantidadVendida);

            // Acumular para el resumen final por tipo
            totalVentasPorTipo.merge(tipoProducto, totalVentas, Double::sum);
            cantidadVendidaPorTipo.merge(tipoProducto, cantidadVendida, Integer::sum);
        }

        // Mostramos los nuevos totalizadores generales por tipo de producto
        System.out.println("\n--- TOTAL CADENA COMPLETA POR TIPO DE PRODUCTO ---");
        for (String tipo : totalVentasPorTipo.keySet()) {
            double total = totalVentasPorTipo.get(tipo);
            int cantidad = cantidadVendidaPorTipo.get(tipo);
            System.out.printf("TOTAL SUCURSAL | Tipo: %s | Total Vendido: $%.2f | Cantidad: %d\n",
                    tipo, total, cantidad);
        }

        mongoClient.close();
    }
}
