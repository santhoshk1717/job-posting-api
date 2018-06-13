package risksense_Assessment.job_postings_api;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JobEntityDeserializer implements Deserializer {

	public JobEntity deserialize(String arg0, byte[] arg1) {
	    ObjectMapper mapper = new ObjectMapper();
	    JobEntity user = null;
	    try {
	      user = mapper.readValue(arg1, JobEntity.class);
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return user;
	}
	
	public void configure(Map configs, boolean isKey) {
		
	}
	
	public void close() {
	
	}

}