package hello;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootApplication
public class Application implements CommandLineRunner {

	static String cloudifyManagerUrl = System.getProperty("cloudifyManagerUrl" , "http://10.8.1.145:8100");

	private static final Logger log = LoggerFactory
			.getLogger(Application.class);

	public static void main(String args[]) {
		SpringApplication.run(Application.class);
	}

	public void run(String... strings) throws Exception {
		getBlueprintsList(strings);
		getblueprintById(strings);
		getDeploymentsList(strings);
	}
	
	public void getblueprintById(String... strings) throws Exception {
		System.out.println("-----getblueprintById----");
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(
				cloudifyManagerUrl + "/api/v2/blueprints?id=hello&_include=id,created_at", String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());
		JsonNode items = root.path("items");

		for (final JsonNode objNode : items) {
			System.out.println("blueprint id:" + objNode.get("id").textValue() + " created_at:" + objNode.get("created_at").textValue() );
		}
	}
	
	public void getBlueprintsList(String... strings) throws Exception {
		System.out.println("------getBlueprintLists-----");
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(
				cloudifyManagerUrl + "/api/v2/blueprints?_include=id,created_at", String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());
		JsonNode items = root.path("items");

		for (final JsonNode objNode : items) {
			System.out.println("blueprint id:" + objNode.get("id").textValue() + " created_at:" + objNode.get("created_at").textValue() );
		}
	}

	public void getDeploymentsList(String... strings) throws Exception {
		System.out.println("------getDeploymentsLists-----");
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(
				cloudifyManagerUrl + "/api/v2/deployments?_include=id,created_at,blueprint_id", String.class);
		ObjectMapper mapper = new ObjectMapper();
		JsonNode root = mapper.readTree(response.getBody());
		JsonNode items = root.path("items");

		for (final JsonNode objNode : items) {
			System.out.println("deployment id:" + objNode.get("id").textValue() + 
					" blueprint_id:" + objNode.get("blueprint_id").textValue() +
					" created_at:" + objNode.get("created_at").textValue() );
		}
	}
}
