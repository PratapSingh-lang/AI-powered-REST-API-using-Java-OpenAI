package com.AI.JAVA.service;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class AIService {

	@Value("${openai.api.key}")
	 private String openaiApiKey;
	
	private final String API_URL = "https://api.openai.com/v1/chat/completions";
	
//	private static final Logger log = LoggerFactory.getLogger(AIService.class);
	
	public String callOpenAIAPI(String question) {
	    RestTemplate restTemplate = new RestTemplate();
	    ObjectMapper mapper = new ObjectMapper();
	    String responseText = "";

	    // Create JSON body using ObjectMapper
	    ObjectNode requestBody = mapper.createObjectNode();
	    requestBody.put("model", "gpt-3.5-turbo");
//	    requestBody.put("model", "text-davinci-003");

	    ArrayNode messages = mapper.createArrayNode();
	    ObjectNode message = mapper.createObjectNode();
	    message.put("role", "user");
	    message.put("content", question);
	    messages.add(message);

	    requestBody.set("messages", messages);

	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.setBearerAuth(openaiApiKey); // Authorization header

	    HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

	    int maxRetries = 5;
	    int attempt = 0;

//	    while (attempt < maxRetries) {
//	        try {
//	            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);
//
//	            if (response.getStatusCode() == HttpStatus.OK) {
//	                JsonNode root = mapper.readTree(response.getBody());
//	                responseText = root.path("choices").get(0).path("message").path("content").asText();
//	                return responseText;
//	            } else if (response.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
//	                // Backoff and retry
//	                int waitTime = (int) Math.pow(2, attempt); // Exponential backoff
//	                System.out.println("Rate limit hit. Retrying in " + waitTime + " seconds...");
//	                Thread.sleep(waitTime * 1000L);
//	                attempt++;
//	            } else {
//	                return "OpenAI API call failed: " + response.getStatusCode();
//	            }
//	        } catch (Exception e) {
//	            e.printStackTrace();
//	            return "Error calling OpenAI API: " + e.getMessage();
//	        }
//	    }
	    
	    while (attempt < maxRetries) {
	        try {
	            ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);

	            // Successful call
	            if (response.getStatusCode() == HttpStatus.OK) {
	                JsonNode root = mapper.readTree(response.getBody());
	                return root.path("choices").get(0).path("message").path("content").asText();
	            }

	        } catch (HttpClientErrorException e) {
	            if (e.getStatusCode() == HttpStatus.TOO_MANY_REQUESTS) {
	                int waitTime = (int) Math.pow(2, attempt);
	                System.out.println("Rate limited. Retrying in " + waitTime + " seconds...");
	                try {
//	                    Thread.sleep(waitTime * 1000L);
	                    Thread.sleep(20000);
	                } catch (InterruptedException ie) {
	                    Thread.currentThread().interrupt();
	                }
	                attempt++;
	            } else {
	                return "Client error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
	            }
	        } catch (HttpServerErrorException e) {
	            return "Server error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString();
	        } catch (RestClientException e) {
	            return "Unexpected error: " + e.getMessage();
	        } catch (Exception e) {
	            e.printStackTrace();
	            return "General error: " + e.getMessage();
	        }
	    }
	    

	    return "Failed after " + maxRetries + " retries due to rate limits.";
	}
	
	
	
//	public String callOpenAIAPI(String question) {
//        
//		try {
//	        RestTemplate restTemplate = new RestTemplate();
//
//	        
//	     // Create JSON body using ObjectMapper
//            ObjectMapper mapper = new ObjectMapper();
//            ObjectNode requestBody = mapper.createObjectNode();
//            requestBody.put("model", "gpt-3.5-turbo");
//
//            ArrayNode messages = mapper.createArrayNode();
//            ObjectNode message = mapper.createObjectNode();
//            message.put("role", "user");
//            message.put("content", question);
//            messages.add(message);
//
//            requestBody.set("messages", messages);
//
//	        HttpHeaders headers = new HttpHeaders();
//	        headers.setContentType(MediaType.APPLICATION_JSON);
//	        headers.setBearerAuth(openaiApiKey); // Add Authorization header
//
//	        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);
//	        ResponseEntity<String> response = restTemplate.postForEntity(API_URL, request, String.class);
////	        log.info("Response: " + response.getBody() + " Status Code: " + response.getStatusCode());
//
//	        if (response.getStatusCode() == HttpStatus.OK) {
//	            // Parse the response
//	            ObjectMapper mapper1 = new ObjectMapper();
//	            JsonNode root = mapper1.readTree(response.getBody());
//	            return root.path("choices").get(0).path("message").path("content").asText();
//	        } else {
//	            return "OpenAI API call failed: " + response.getStatusCode();
//	        }
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        return "Error calling OpenAI API: " + e.getMessage();
//	    }
//		
//		
//		
//    }

	
}
