package openstack.summit;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

public class ReadLogLines {
	
	String filePath = "";
	
	public ReadLogLines(String filePath) {
		this.filePath = filePath;
	}
	
	public void getLine(Map<String, Object> kafkaConfigs, final String topic) {
	    try (KafkaProducer<String, String> producer = new KafkaProducer<>(kafkaConfigs)) {
	    	BufferedReader br = null;
			try {
				String sCurrentLine;
				br = new BufferedReader(new FileReader(filePath));
				while ((sCurrentLine = br.readLine()) != null) {
					producer.send(new ProducerRecord<String, String>(topic, sCurrentLine));
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (br != null)br.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			
	    }
	}

	public static void main(String[] args) throws IOException {
	    Map<String, Object> props = new HashMap<>();
	    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
	    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
	    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
	    props.put(ProducerConfig.CLIENT_ID_CONFIG, "my-producer");
	    
	    String path = "sahara-all-small.log";
	    new ReadLogLines(path).getLine(props, "logs");
	}

}
