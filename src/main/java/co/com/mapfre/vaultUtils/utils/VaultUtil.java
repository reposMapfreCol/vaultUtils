package co.com.mapfre.vaultUtils.utils;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VaultUtil {
	
	private final String vaultUrl;
	private final String roleId;
	private final String secretId;
	private final String secretPath;
	private final RestTemplate restTemplate;
	private final ObjectMapper mapper;
	
	
	
	
	 public VaultUtil(String vaultUrl, String roleId, String secretId, String secretPath, RestTemplate restTemplate, ObjectMapper mapper) {
		this.vaultUrl = vaultUrl;
		this.roleId = roleId;
		this.secretId = secretId;
		this.secretPath = secretPath;
		this.restTemplate = restTemplate;
		this.mapper = mapper;
	}

	 private String getVaultToken() {
	        try {
	            String url = vaultUrl + "/v1/auth/approle/login";
	            String payload = String.format("{\"role_id\": \"%s\", \"secret_id\": \"%s\"}", roleId, secretId);
	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            HttpEntity<String> request = new HttpEntity<>(payload, headers);

	            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
	            JsonNode json = mapper.readTree(response.getBody());
	            return json.get("auth").get("client_token").asText();
	        } catch (Exception e) {
	            throw new RuntimeException("Error autenticando con Vault: " + e.getMessage(), e);
	        }
	    }

	    public String getSecretValue(String key) {
	        try {
	            String token = getVaultToken();
	            String url = vaultUrl + "/v1/" + secretPath;

	            HttpHeaders headers = new HttpHeaders();
	            headers.setBearerAuth(token);
	            HttpEntity<Void> request = new HttpEntity<>(headers);

	            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
	            JsonNode json = mapper.readTree(response.getBody());

	            return json.at("/data/data/" + key).asText();
	        } catch (Exception e) {
	            throw new RuntimeException("Error leyendo secreto desde Vault: " + e.getMessage(), e);
	        }
	    }

}
