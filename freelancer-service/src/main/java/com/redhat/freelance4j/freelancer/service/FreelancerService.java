package com.redhat.freelance4j.freelancer.service;

import java.util.List;

import com.redhat.freelance4j.freelancer.model.Freelancer;

public interface FreelancerService {
	   /**
     * gets freelancer by id
     * @param freelancerId id to search
     * @return freelancer with a given id
     */
    public Freelancer getFreelacer(String freelancerId);
    
    /**
     * gets list of the freelancers
     * @return list of found freelancers
     */
    public List<Freelancer> getFreelancers();
}
