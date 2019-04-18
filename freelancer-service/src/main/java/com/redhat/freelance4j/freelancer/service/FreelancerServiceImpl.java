package com.redhat.freelance4j.freelancer.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.redhat.freelance4j.freelancer.model.Freelancer;

/**
 *  Freelancer CRUD service
 */
@Component
public class FreelancerServiceImpl implements FreelancerService{
	
    private static final Logger LOG = LoggerFactory.getLogger(FreelancerServiceImpl.class);

    @PersistenceContext
    private EntityManager em;

    @Override
    public Freelancer getFreelacer(String freelancerId) {
    	Freelancer freelancer = em.find(Freelancer.class, freelancerId);
    	if (freelancer != null) {
    		LOG.debug("FREELANCER={}", freelancer.getFreelancerId());
    	} else {
    		LOG.debug("FREELANCER=null");
    	}
        return freelancer;
    }
    
    @Override
    public List<Freelancer> getFreelancers() {
    	CriteriaBuilder cb = em.getCriteriaBuilder();
    	CriteriaQuery<Freelancer> cq = cb.createQuery(Freelancer.class);
    	Root<Freelancer> root = cq.from(Freelancer.class);
    	CriteriaQuery<Freelancer> select = cq.select(root);
    	
    	// ordering
    	select.orderBy(cb.asc(root.get("freelancerId")));
    	
    	List<Freelancer> result = em.createQuery(select).getResultList();
    	LOG.debug("RESULT LIST SIZE={]", result.size());
        return result;
    }

}
