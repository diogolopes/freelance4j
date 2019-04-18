package com.redhat.freelance4j.gateway.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Freelancer {

	    private String freelancerId;
	    
	    private String firstName;
	    
	    private String lastName;
	    
	    private String emailAddress;
	    
	    private String skills;


	    public String getFreelancerId() {
			return freelancerId;
		}


		public void setFreelancerId(String freelancerId) {
			this.freelancerId = freelancerId;
		}

		public String getFirstName() {
			return firstName;
		}

		public void setFirstName(String firstName) {
			this.firstName = firstName;
		}

		public String getLastName() {
			return lastName;
		}

		public void setLastName(String lastName) {
			this.lastName = lastName;
		}

		public String getEmailAddress() {
			return emailAddress;
		}

		public void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
		}

		public String getSkills() {
			return skills;
		}

		public void setSkills(String skills) {
			this.skills = skills;
		}
}
