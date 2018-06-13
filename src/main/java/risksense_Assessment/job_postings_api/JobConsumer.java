package risksense_Assessment.job_postings_api;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

public class JobConsumer {
    
    static void executeConsumer(Connection connection) throws InterruptedException {
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
                try {
					String query = "INSERT INTO `JobPostings`.`postings` (`id`, `role`, `compensation`, `location`, `description`, `reply_rate`, `skills`, `availability`, `posted_date`)"
							+ " VALUES ('"+ record.value().getId()+"', '"+ record.value().getRole()+"', '"+ record.value().getCompensation()+"', '"+ record.value().getLocation()+"', '"+  record.value().getDescription()+"', '"+  record.value().getReplyRate()+"', '"+ record.value().getSkills()+"', '"+ record.value().getAvailability() +"', '"+  record.value().getPosted_date()+"' )";  
					Statement stmt=connection.createStatement(); 
					stmt.executeUpdate(query);
                } catch (SQLException e) {
					e.printStackTrace();
				} 
            });
             
            consumer.commitAsync();
        }
        consumer.close();
    }
    
    public static void main(String[] args) throws Exception {
    	try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    	} catch (ClassNotFoundException e) {
    		e.printStackTrace();
    		return;
    	}
    	Connection connection = DriverManager.getConnection("jdbc:mysql://santhosh.crzbyoydhnfc.us-east-2.rds.amazonaws.com:3306/JobPostings?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=CST","root", "");		
        executeConsumer(connection);
        connection.close();
    }
}