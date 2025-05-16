package reportesRequeridos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;

import org.bson.Document;

import com.nahuel.mongodb.*;
import com.nahuel.mongodb.Producto.TipoProducto;
import com.nahuel.mongodb.Venta.FormaPago;

import java.util.*;
import java.util.stream.Collectors;

public class GeneradorDatosDeEjemplo {
	public static void main(String[] args) {
		GeneradorDatosDeEjemplo.insertarDatosEnMongo();
	}

    public static void insertarDatosEnMongo() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Random random = new Random();

        // Conexión a MongoDB
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            MongoDatabase db = mongoClient.getDatabase("farmacia");

            // 1. Obras Sociales
            ObraSocial osde = new ObraSocial("os1", "OSDE");
            ObraSocial swiss = new ObraSocial("os2", "Swiss Medical");

            List<ObraSocial> obrasSociales = Arrays.asList(osde, swiss);

            // 2. Sucursales
            Sucursal s1 = new Sucursal("01", "Av. Siempre Viva", "123", "Springfield", "Illinois");
            Sucursal s2 = new Sucursal("02", "Calle Falsa", "456", "Shelbyville", "Illinois");
            Sucursal s3 = new Sucursal("03", "Avenida Principal", "789", "Capital", "Buenos Aires");

            List<Sucursal> sucursales = Arrays.asList(s1, s2, s3);

            // 3. Clientes
            List<Cliente> clientes = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                ObraSocial obraSocial = (i % 2 == 0) ? osde : swiss;
                clientes.add(new Cliente("c" + i, "Cliente" + i, "Apellido" + i, "1234567" + i, "Calle " + i, "Ciudad" + i, "Provincia" + i, obraSocial, "nro" + i));
            }

            // 4. Empleados
            List<Empleado> empleados = new ArrayList<>();
            for (Sucursal sucursal : sucursales) {
                for (int i = 1; i <= 3; i++) {
                    boolean esEncargado = (i == 1);
                    empleados.add(new Empleado("e" + sucursal.getIdSucursal() + "_" + i, "Empleado" + i, "Apellido" + i, "2345678" + i,
                            "20-2345678" + i + "-3", "Calle E" + i, "CiudadE" + i, "ProvinciaE" + i, swiss, "afi" + i, sucursal, esEncargado));
                }
            }

            // 5. Productos
            List<Producto> productosCatalogo = new ArrayList<>();
            for (int i = 1; i <= 10; i++) {
                Producto.TipoProducto tipo = (i <= 7) ? Producto.TipoProducto.MEDICAMENTO : Producto.TipoProducto.PERFUMERIA;
                productosCatalogo.add(new Producto("prod" + i, "COD" + i, "Producto" + i, tipo, "Lab" + i));
            }

            // 6. Ventas
            List<Venta> ventas = new ArrayList<>();
            int ventaCounter = 1;
            for (Sucursal sucursal : sucursales) {
                List<Empleado> empleadosSucursal = empleados.stream()
                        .filter(e -> e.getSucursal().getIdSucursal().equals(sucursal.getIdSucursal()))
                        .collect(Collectors.toList());

                int cantidadVentas = 28 + random.nextInt(5);
                for (int i = 0; i < cantidadVentas; i++) {
                    String idVenta = "v" + ventaCounter++;
                    String nroTicket = String.format("%04d", Integer.parseInt(sucursal.getIdSucursal())) + "-" + String.format("%08d", i + 1);
                    long ahora = System.currentTimeMillis();
                    long unAnio = 365L * 24 * 60 * 60 * 1000;
                    Date fecha = new Date(ahora - (Math.abs(random.nextLong()) % unAnio));
                    System.out.println("Ejemplo de fecha generada: " + fecha);
                    Venta.FormaPago formaPago = Venta.FormaPago.values()[random.nextInt(Venta.FormaPago.values().length)];
                    Cliente cliente = clientes.get(random.nextInt(clientes.size()));
                    Empleado vendedor = empleadosSucursal.get(random.nextInt(empleadosSucursal.size()));
                    Sucursal sucursalVenta = vendedor.getSucursal(); // Sucursal asignada al momento de la venta

                    List<DetalleVenta> detalleVenta = new ArrayList<>();
                    int cantidadProductos = 1 + random.nextInt(3);
                    for (int j = 0; j < cantidadProductos; j++) {
                        Producto prod = productosCatalogo.get(random.nextInt(productosCatalogo.size()));
                        int cantidad = 1 + random.nextInt(3);
                        detalleVenta.add(new DetalleVenta(
                            prod,
                            cantidad,
                            Math.round((10 + (990 * random.nextDouble())) * 100.0) / 100.0 //Precio aleatorio del producto
                        ));
                    }

                    double total = detalleVenta.stream().mapToDouble(DetalleVenta::getSubtotal).sum();
                    ventas.add(new Venta(idVenta, nroTicket, fecha, formaPago, cliente, vendedor, vendedor, detalleVenta, sucursalVenta));
                }
            }
            

            // Insertar cada colección en MongoDB (como JSON plano)
            insertarColeccion(db, "obrasSociales", obrasSociales, gson);
            insertarColeccion(db, "sucursales", sucursales, gson);
            insertarColeccion(db, "clientes", clientes, gson);
            insertarColeccion(db, "empleados", empleados, gson);
            insertarColeccion(db, "productos", productosCatalogo, gson);
            insertarVentas(db, ventas);

            System.out.println("✅ Datos insertados correctamente en MongoDB.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static <T> void insertarColeccion(MongoDatabase db, String nombreColeccion, List<T> lista, Gson gson) {
        MongoCollection<Document> coleccion = db.getCollection(nombreColeccion);
        List<Document> docs = lista.stream()
                .map(elem -> Document.parse(gson.toJson(elem)))
                .collect(Collectors.toList());
        coleccion.insertMany(docs);
    }
    
    private static void insertarVentas(MongoDatabase db, List<Venta> ventas) {
        MongoCollection<Document> coleccion = db.getCollection("ventas");
        List<Document> docs = new ArrayList<>();

        for (Venta v : ventas) {
            Document doc = new Document("idVenta", v.getIdVenta())
                .append("nroTicket", v.getNroTicket())
                .append("fecha", v.getFecha()) // 👈 Esto sí lo guarda como Date real
                .append("total", v.getTotal())
                .append("formaPago", v.getFormaPago().toString())
                .append("cliente", Document.parse(new Gson().toJson(v.getCliente())))
                .append("vendedor", Document.parse(new Gson().toJson(v.getEmpleadoAtencion())))
                .append("cobrador", Document.parse(new Gson().toJson(v.getEmpleadoVenta())))
                .append("productos", v.getProductosVendidos().stream()
                        .map(p -> Document.parse(new Gson().toJson(p)))
                        .collect(Collectors.toList()))
                .append("sucursalVenta", Document.parse(new Gson().toJson(v.getSucursalVenta())));
	            

            docs.add(doc);
        }

        coleccion.insertMany(docs);
    }
}
