package risksense_Assessment.job_postings_api;

import java.util.Map;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JobEntitySerializer implements Serializer {
	  
	 public void configure(Map configs, boolean isKey) {
			
	 }

      
	public void close() {
	  
	}

	public byte[] serialize(String topic, Object data) {
		byte[] retVal = null;
	    ObjectMapper objectMapper = new ObjectMapper();
	    try {
	      retVal = objectMapper.writeValueAsString(data).getBytes();
	    } catch (Exception e) {
	      e.printStackTrace();
	    }
	    return retVal;
		
	}




	}