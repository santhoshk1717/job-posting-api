package risksense_Assessment.job_postings_api;

import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class JobConsumer {
    
    static void executeConsumer() throws InterruptedException {
    	final Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "jobConsumer");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JobEntityDeserializer.class.getName());

        final Consumer<Long, JobEntity> consumer = new KafkaConsumer<>(props);

        consumer.subscribe(Collections.singletonList("job_postings_1110am"));

        final int giveUp = 100;  
        int noRecordsCount = 0;
        while (true) {
            final ConsumerRecords<Long, JobEntity> consumerRecords = consumer.poll(1000);
            if (consumerRecords.count()==0) {
                noRecordsCount++;
                if (noRecordsCount > giveUp) break;
                else continue;
            }
            consumerRecords.forEach(record -> {
            		System.out.println(record.value().getRole()+ " "+ record.value().getAvailability());
            });
             
            consumer.commitAsync();
        }
        consumer.close();
    }
    
    public static void main(String[] args) throws Exception {
    	executeConsumer();
    }
}