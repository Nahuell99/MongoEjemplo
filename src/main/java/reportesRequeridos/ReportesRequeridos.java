package reportesRequeridos;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;

public class ReportesRequeridos {

	
	
	
	public void reporteVentasEntreFechas(Date desde, Date hasta) {
	    try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
	        MongoDatabase database = mongoClient.getDatabase("mi_base_de_datos");
	        MongoCollection<Document> ventasCollection = database.getCollection("ventas");

	        Bson filtroFechas = Filters.and(
	                Filters.gte("fecha", desde),
	                Filters.lte("fecha", hasta)
	        );

	        // Agrupado total por sucursal
	        List<Bson> pipeline = Arrays.asList(
	            Aggregates.match(filtroFechas),
	            Aggregates.group("$sucursal.id_sucursal", 
	                Accumulators.sum("totalVentas", "$total"),
	                Accumulators.push("ventas", "$$ROOT")
	            )
	        );

	        AggregateIterable<Document> resultado = ventasCollection.aggregate(pipeline);

	        for (Document doc : resultado) {
	            System.out.println("Sucursal: " + doc.get("_id"));
	            System.out.println("Total vendido: $" + doc.get("totalVentas"));
	            System.out.println("Detalle: " + doc.get("ventas"));
	            System.out.println("----------------------------");
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
}
