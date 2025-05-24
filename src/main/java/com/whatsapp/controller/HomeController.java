package com.whatsapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class HomeController {
	
	@GetMapping("/")
	public ResponseEntity<String> HomeController(){
		return new ResponseEntity<String>("Welcome to Whatsapp clone", HttpStatus.OK);
	}
}
