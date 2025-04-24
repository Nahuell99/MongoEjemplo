package consultas;

import com.mongodb.client.*;
import org.bson.Document;

import java.text.SimpleDateFormat;
import java.util.*;

public class SeptimaConsultaRankingClientes {

    static class ClienteInfo {
        String nombreCompleto;
        Map<String, Double> totalPorSucursal = new HashMap<>();
        double totalGeneral = 0;

        public ClienteInfo(String nombreCompleto) {
            this.nombreCompleto = nombreCompleto;
        }

        public void agregarCompra(String sucursal, double monto) {
            totalGeneral += monto;
            totalPorSucursal.merge(sucursal, monto, Double::sum);
        }
    }

    public static void rankingClientesPorCompras(MongoDatabase db, Date desde, Date hasta) {
        MongoCollection<Document> ventasCollection = db.getCollection("ventas");

        // Filtro por fechas
        Document filtroFechas = new Document("fecha", new Document("$gte", desde).append("$lte", hasta));
        FindIterable<Document> ventas = ventasCollection.find(filtroFechas);

        Map<String, ClienteInfo> clientesTotales = new HashMap<>();

        for (Document venta : ventas) {
            double total = venta.getDouble("total");

            Document cliente = (Document) venta.get("cliente");
            String idCliente = cliente.getString("idCliente");
            String nombre = cliente.getString("nombre");
            String apellido = cliente.getString("apellido");
            String nombreCompleto = nombre + " " + apellido;

            Document vendedor = (Document) venta.get("vendedor");
            Document sucursal = (Document) vendedor.get("sucursal");
            String idSucursal = sucursal.getString("idSucursal");

            // Guardar o actualizar cliente
            ClienteInfo info = clientesTotales.computeIfAbsent(idCliente, k -> new ClienteInfo(nombreCompleto));
            info.agregarCompra(idSucursal, total);
        }

        // Crear lista ordenada por total general (descendente)
        List<Map.Entry<String, ClienteInfo>> rankingGeneral = new ArrayList<>(clientesTotales.entrySet());
        rankingGeneral.sort((a, b) -> Double.compare(b.getValue().totalGeneral, a.getValue().totalGeneral));

        // Mostrar ranking total
        System.out.println("=== RANKING GENERAL DE CLIENTES ENTRE " + desde + " Y " + hasta + " ===");
        for (int i = 0; i < rankingGeneral.size(); i++) {
            ClienteInfo cliente = rankingGeneral.get(i).getValue();
            System.out.println((i + 1) + ". " + cliente.nombreCompleto + " - Total: $" + cliente.totalGeneral);
        }

        // Mostrar ranking por sucursal
        System.out.println("\n=== RANKING POR SUCURSAL ===");
        Map<String, List<Map.Entry<String, ClienteInfo>>> rankingPorSucursal = new HashMap<>();

        for (String idSucursal : obtenerSucursales(clientesTotales)) {
            List<Map.Entry<String, ClienteInfo>> rankingSucursal = new ArrayList<>();
            for (Map.Entry<String, ClienteInfo> entry : clientesTotales.entrySet()) {
                ClienteInfo info = entry.getValue();
                if (info.totalPorSucursal.containsKey(idSucursal)) {
                    rankingSucursal.add(entry);
                }
            }

            rankingSucursal.sort((a, b) ->
                    Double.compare(
                            b.getValue().totalPorSucursal.get(idSucursal),
                            a.getValue().totalPorSucursal.get(idSucursal))
            );

            rankingPorSucursal.put(idSucursal, rankingSucursal);
        }

        for (String sucursal : rankingPorSucursal.keySet()) {
            System.out.println("\nSucursal " + sucursal + ":");
            List<Map.Entry<String, ClienteInfo>> rankingSucursal = rankingPorSucursal.get(sucursal);
            for (int i = 0; i < rankingSucursal.size(); i++) {
                ClienteInfo cliente = rankingSucursal.get(i).getValue();
                System.out.println((i + 1) + ". " + cliente.nombreCompleto + " - Total: $" + cliente.totalPorSucursal.get(sucursal));
            }
        }
    }

    private static Set<String> obtenerSucursales(Map<String, ClienteInfo> clientesTotales) {
        Set<String> sucursales = new HashSet<>();
        for (ClienteInfo info : clientesTotales.values()) {
            sucursales.addAll(info.totalPorSucursal.keySet());
        }
        return sucursales;
    }

    public static void main(String[] args) throws Exception {
        MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
        MongoDatabase db = mongoClient.getDatabase("farmacia");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date desde = sdf.parse("2023-01-01");
        Date hasta = sdf.parse("2025-12-31");

        rankingClientesPorCompras(db, desde, hasta);
    }
}
