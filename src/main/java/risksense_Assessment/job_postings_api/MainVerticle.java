package risksense_Assessment.job_postings_api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;

public class MainVerticle extends AbstractVerticle {
		
	public void pushKafka(JobEntity jobEntity){
		Map<String, String> config = new HashMap<>();
		  config.put("bootstrap.servers", "localhost:9092");
		  config.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		  config.put("value.serializer", "io.vertx.blog.my_first_app.JobEntitySerializer");
		  config.put("acks", "1");

		  KafkaProducer<String, JobEntity> producer = KafkaProducer.create(vertx, config);
		  KafkaProducerRecord<String, JobEntity> record = KafkaProducerRecord.create("job_postings_1110am", jobEntity);
		  producer.write(record);
	}
	

	private void addOne(RoutingContext routingContext) {
		  final JobEntity jobEntity = Json.decodeValue(routingContext.getBodyAsString(), JobEntity.class);
		  pushKafka(jobEntity);
	      routingContext.response()
		      .setStatusCode(201)
		      .putHeader("content-type", "application/json; charset=utf-8")
		      .end(Json.encodePrettily(jobEntity));

	      System.out.println("pushed");
		}
	

	private void searchAll(RoutingContext routingContext) throws SQLException {
		 try {
	    		Class.forName("com.mysql.cj.jdbc.Driver");
	    	} catch (ClassNotFoundException e) {
	    		e.printStackTrace();
	    		return;
	    	}
      Connection connection = DriverManager.getConnection("jdbc:mysql://santhosh.crzbyoydhnfc.us-east-2.rds.amazonaws.com:3306/JobPostings?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CST","root", "");		
      
      String keyword = routingContext.request().getParam("keyword");
      String query = "SELECT * FROM postings WHERE MATCH (role,compensation,location,description,reply_rate,skills,availability) AGAINST ('"+keyword+"' IN NATURAL LANGUAGE MODE);";  
	  Statement stmt=connection.createStatement(); 
	  ResultSet rs = stmt.executeQuery(query);
	  Map<Integer, JsonObject> searchedJobs = new LinkedHashMap<>();
	  while(rs.next()){
		  JsonObject tmp = new JsonObject();
		  tmp.put("availability", rs.getString(8));
		  tmp.put("compensation", rs.getString(3));
		  tmp.put("description", rs.getString(5));
		  tmp.put("location", rs.getString(4));
		  tmp.put("posted_date", rs.getString(9));
		  tmp.put("replyRate",rs.getString(6));
		  tmp.put("role",rs.getString(2));
		  tmp.put("skills",rs.getString(7));
		  tmp.put("id",rs.getString(1));
		 searchedJobs.put(rs.getInt(1), tmp);
	  }  
	  connection.close();
      routingContext.response()
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(searchedJobs.values())); 
	}
	
	@Override
	public void start(Future<Void> fut) {
	   


	Router router = Router.router(vertx);
	 
	router.route("/").handler(routingContext -> {
	   HttpServerResponse response = routingContext.response();
	   response
	       .putHeader("content-type", "text/html")
	       .end("<h1>Welcome to Job Postings!</h1>");
	 });
	 
	router.route("/api/job*").handler(BodyHandler.create());
	router.post("/api/job").handler(this::addOne);
	router.get("/api/search/:keyword").handler(event -> {
		try {
			searchAll(event);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	});
	vertx
	     .createHttpServer()
	     .requestHandler(router::accept)
	     .listen(
	         config().getInteger("http.port", 8080),
	         result -> {
	           if (result.succeeded()) {
	             fut.complete();
	           } else {
	             fut.fail(result.cause());
	           }
	         }
	     );

	}
}