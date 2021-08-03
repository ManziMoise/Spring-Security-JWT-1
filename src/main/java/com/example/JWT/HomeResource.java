package com.example.JWT;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import model.AuthenticateRequest;
import model.AuthenticationResponse;

@RestController
public class HomeResource {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Autowired
	private JwtUtil jwtTokenUtil;
	
	
	@RequestMapping( "/hello" )
	public String hello() {
		return "hello world";
	}
	
	@RequestMapping(value="/authenticate", method= RequestMethod.POST)
	public ResponseEntity<?> createAthenticationToken(@RequestBody AuthenticateRequest authenticateRequest) throws Exception{
		try {
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticateRequest.getUsername(), authenticateRequest.getPassword())
				);
		}
		catch (BadCredentialsException e) {
			// TODO: handle exception
			throw new Exception("incorrect username or password", e);
		}
		
		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticateRequest.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		
		return ResponseEntity.ok(new AuthenticationResponse(jwt));
	}
	

}
