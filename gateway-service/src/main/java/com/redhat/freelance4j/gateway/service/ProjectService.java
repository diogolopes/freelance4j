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

import com.redhat.freelance4j.gateway.model.Project;

@ApplicationScoped
public class ProjectService {

	/**
	 * base end point for project service
	 */
    private WebTarget projectBaseService;    
    /**
     * end point for get projects by the status
     */
    private WebTarget projectsGetByStatusService;
    /**
     * end point for get project
     */
    private WebTarget projectGetByIdService;
    
    /**
    * inject environment varible project_service_url
    */
    @Inject
    @ConfigurationValue("project_service_url")
    private String projectUrl;
    /**
     * gets a list of all projects
     * @return a list of all projects
     */
    public List<Project> getProjects() {
        Response response = projectBaseService.request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
            return response.readEntity(new GenericType<List<Project>>() {});
        } else if (response.getStatus() == 404) {
            return null;
        } else {
            throw new ServiceUnavailableException();
        }
    }
    
    /**
     * gets a list of projects by the given status
     * @param status
     * @return a list of projects with the given status
     */
    public List<Project> getProjectsByStatus(String status) {
        Response response = projectsGetByStatusService.resolveTemplate("theStatus", status).request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
            return response.readEntity(new GenericType<List<Project>>() {});
        } else if (response.getStatus() == 404) {
            return null;
        } else {
            throw new ServiceUnavailableException();
        }
    }
    
    /**
     * gets project by the given project id
     * @param projectId
     * @return project with given id
     */
    public Project getProject(String projectId) {
        Response response = projectGetByIdService.resolveTemplate("projectId", projectId).request(MediaType.APPLICATION_JSON).get();
        if (response.getStatus() == 200) {
            return response.readEntity(Project.class);
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
    	projectBaseService = ((ResteasyClientBuilder)ClientBuilder.newBuilder())
                .connectionPoolSize(10).build().target(projectUrl).path("projects");
    	
    	projectsGetByStatusService = projectBaseService.path("status").path("{theStatus}");
    	
    	projectGetByIdService = projectBaseService.path("{projectId}");
    }

}

