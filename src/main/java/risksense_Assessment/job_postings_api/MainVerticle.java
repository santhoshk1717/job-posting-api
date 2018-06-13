package risksense_Assessment.job_postings_api;

import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
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