package com.redhat.freelance4j.gateway.rest;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.redhat.freelance4j.gateway.model.Freelancer;
import com.redhat.freelance4j.gateway.model.Project;
import com.redhat.freelance4j.gateway.service.FreelancerService;
import com.redhat.freelance4j.gateway.service.ProjectService;

@Path("/gateway")
@RequestScoped
public class GatewayResource {

    @Inject
    private FreelancerService freelancerService;
    
    @Inject
    private ProjectService projectService;

    /**
     * retrieves information for the given projectId
     * end point: gateway/projects/{projectId}
     * @param projectId
     * @return project with the given id
     */
    @GET
    @Path("/projects/{projectId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Project getProject(@PathParam("projectId") String projectId) {
    	Project project = projectService.getProject(projectId);
        if (project == null) {
            throw new NotFoundException();
        } else {
            return project;
        }
    }
    
    /**
     * retrieves a list of all projects
     * end point: gateway/projects
     * @return a list of all projects
     */
    @GET
    @Path("/projects")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Project> getProjects() {
    	List<Project> projectList = projectService.getProjects();
        return projectList;
    }
    
    /**
     * retrieves a list of projects with the given status
     * end point: gateway/projects/status/{theStatus}
     * @param theStatus
     * @return project with the given id
     */
    @GET
    @Path("/projects/status/{theStatus}")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Project> getProjectsByStatus(@PathParam("theStatus") String status) {
    	List<Project> projectList = projectService.getProjectsByStatus(status);
        return projectList;
    }

    /**
     * retrieves information for the given freelancerId
     * end point: gateway/freelancers/{freelancerId}
     * @param freelancerId
     * @return freelancer with the given id
     */
    @GET
    @Path("/freelancers/{freelancerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Freelancer getFreelancer(@PathParam("freelancerId") String freelancerId) {
    	Freelancer freelancer = freelancerService.getFreelancer(freelancerId);
        if (freelancer == null) {
            throw new NotFoundException();
        } else {
            return freelancer;
        }
    }
    
    /**
     * retrieves a list of all freelancer
     * end point: gateway/freelancers
     * @return a list of all freelancers
     */
    @GET
    @Path("/freelancers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Freelancer> getFreelancers() {
    	List<Freelancer> freelancerList = freelancerService.getFreelancers();
        return freelancerList;
    }
}

