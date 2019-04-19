package com.redhat.freelance4j.gateway.service;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.ServiceUnavailableException;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.wildfly.swarm.spi.runtime.annotations.ConfigurationValue;

import com.redhat.freelance4j.gateway.model.Freelancer;

@ApplicationScoped
public class FreelancerService {

	/**
	 * base end point for freelancer service / get all freelancers
	 */
    private WebTarget freelancerBaseService;
    /**
     * end point for get freelancer
     */
    private WebTarget freelancerGetByIdService;
    
    /**
    * inject environment varible freelancer_service_url
    */
    @Inject
    @ConfigurationValue("freelancer_service_url")
    private String freelancerUrl;
    
    /**
     * get a list of all freelancers
     * @return list of all freelancers
     */
    public List<Freelancer> getFreelancers() {
    	// return JsonObject
        Response response = freelancerBaseService.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
        	return response.readEntity(new GenericType<List<Freelancer>>() {});
        } else if (response.getStatus() == 404) {
            return null;
        } else {
            throw new ServiceUnavailableException();
        }
    }

    /**
     * gets freelancer by the given id
     * @param freelancerId freelancer id
     * @return freelancer with the given id
     */
    public Freelancer getFreelancer(String freelancerId) {
        Response response = freelancerGetByIdService.resolveTemplate("freelancerId", freelancerId).request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
            return response.readEntity(Freelancer.class);
        } else if (response.getStatus() == 404) {
            return null;
        } else {
            throw new ServiceUnavailableException();
        }
    }

    /**
     * initial web targets
     */
    @PostConstruct
    public void init() {
    	freelancerBaseService = ((ResteasyClientBuilder)ClientBuilder.newBuilder())
                .connectionPoolSize(10).build().target(freelancerUrl).path("freelancers");
    	
    	freelancerGetByIdService = freelancerBaseService.path("{freelancerId}");
    }

}

