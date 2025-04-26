package consultas;
import com.mongodb.client.*;
import org.bson.Document;
import java.text.SimpleDateFormat;
import java.util.*;

public class QuintaRankingProductosPorSucursal {

    public static void rankingProductosPorSucursal(MongoDatabase db) {
        MongoCollection<Document> ventasCollection = db.getCollection("ventas");

        FindIterable<Document> ventas = ventasCollection.find();

        // Map<sucursal, Map<productoDescripcion, montoTotal>>
        Map<String, Map<String, Double>> montoPorSucursalYProducto = new HashMap<>();

        for (Document venta : ventas) {
            Document sucursalVenta = (Document) venta.get("sucursalVenta");
            String idSucursal = sucursalVenta.getString("idSucursal");

            List<Document> productos = (List<Document>) venta.get("productos");
            if (productos != null) {
                for (Document producto : productos) {
                    String descripcionProducto = producto.getString("descripcion");
                    double precioUnitario = producto.getDouble("precioUnitario");
                    int cantidad = producto.getInteger("cantidad");

                    double monto = precioUnitario * cantidad;

                    // Sumar al total correspondiente
                    montoPorSucursalYProducto
                        .computeIfAbsent(idSucursal, k -> new HashMap<>())
                        .merge(descripcionProducto, monto, Double::sum);
                }
            }
        }

        System.out.println("--- Ranking de monto vendido por Producto y Sucursal ---\n");

        for (Map.Entry<String, Map<String, Double>> entrySucursal : montoPorSucursalYProducto.entrySet()) {
            String sucursal = entrySucursal.getKey();
            System.out.println("Sucursal " + sucursal + ":");

            // Obtener productos ordenados por monto de mayor a menor
            List<Map.Entry<String, Double>> productosOrdenados = new ArrayList<>(entrySucursal.getValue().entrySet());
            productosOrdenados.sort((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())); // Orden descendente

            for (Map.Entry<String, Double> entryProducto : productosOrdenados) {
                System.out.println("  Producto: " + entryProducto.getKey() + " - Monto vendido: $" + entryProducto.getValue());
            }
            System.out.println();
        }
    }

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("farmacia");

        rankingProductosPorSucursal(db);
    }
}
