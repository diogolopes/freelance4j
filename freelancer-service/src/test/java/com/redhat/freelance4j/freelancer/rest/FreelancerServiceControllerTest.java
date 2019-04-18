package com.redhat.freelance4j.freelancer.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.redhat.freelance4j.freelancer.model.Freelancer;

/**
 * Test cases to validate freelancer service controller
 */
@ActiveProfiles("dev")
public class FreelancerServiceControllerTest extends AbstractTest{
	@Override
	@Before
	public void setUp() {
		super.setUp();
	}
	
	/**
	 * validate endpoint /freelancers
	 * result: freelancer list contains more than 0 freelancer
	 * @throws Exception
	 */
	@Test
	public void getFreelancerList() throws Exception {
		String uri = "/freelancers";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	      
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Freelancer[] freelancerlist = super.mapFromJson(content, Freelancer[].class);
		assertTrue(freelancerlist.length > 0);
	}
	
	/**
	 * validate entdpoint /freelancers/{freelancerId}
	 * result: freelancer with freelancer id 1000001 exists
	 * @throws Exception
	 */
	@Test
	public void getFreelancer() throws Exception {
		String uri = "/freelancers/1";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	      
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		Freelancer freelancer = super.mapFromJson(content, Freelancer.class);
		assertNotNull(freelancer);
		assertEquals(freelancer.getFreelancerId(), "1");
	}
	
	/**
	 * validate entdpoint /freelancers/{freelancerId}
	 * result: freelancer does not exist
	 * @throws Exception
	 */
	@Test
	public void getFreelancerNotExists() throws Exception {
		String uri = "/freelancers/2000001";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
	      
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
		String content = mvcResult.getResponse().getContentAsString();
		assertEquals(content, "");
	}
}
