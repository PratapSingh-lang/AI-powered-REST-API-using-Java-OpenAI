package com.AI.JAVA.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.AI.JAVA.service.AIService;

@RestController
public class AIController {

	@Autowired
	private AIService aiService;
	
//	 private static final String OPENAI_API_KEY = "YOUR_OPENAI_API_KEY";
	 
	 

	    @PostMapping("/ask")
	    public ResponseEntity<String> askAI(@RequestBody String question) {
	        String answer = aiService.callOpenAIAPI(question);
	        return ResponseEntity.ok(answer);
	    }

	    
	
}
