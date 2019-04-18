package com.redhat.freelance4j.freelancer.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint to health check
 */
@RestController
public class HealthCheckEndpoint {

	@Autowired
	private HealthEndpoint health;
	
	@GetMapping(path="/health", produces="application/json")
	public Health getHealth() {
		return health.invoke();
	}
}
