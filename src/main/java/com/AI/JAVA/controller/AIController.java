package com.AI.JAVA.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.AI.JAVA.service.AIService;

@RestController
public class AIController {

	@Autowired
	private AIService aiService;
	
	@Value("${server.port}")
	private String port;
	
//	 private static final String OPENAI_API_KEY = "YOUR_OPENAI_API_KEY";
	 
	 	@GetMapping()
	 	public ResponseEntity<?> ping(){
	 		return new ResponseEntity<>("Hi, I am running at port : " + port, HttpStatus.OK);
	 	}

	    @PostMapping("/ask")
	    public ResponseEntity<String> askAI(@RequestBody String question) {
	        String answer = aiService.callOpenAIAPI(question);
	        return ResponseEntity.ok(answer);
	    }

	    
	
}
