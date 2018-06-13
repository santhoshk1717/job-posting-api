package risksense.assessment.job_posting_api;

import java.util.concurrent.atomic.AtomicInteger;

public class JobEntity {
	
	private static final AtomicInteger counter = new AtomicInteger();
	
	private final int id;
	private String role;
	private String compensation;
	private String location;
	private String description;
	private String replyRate;
	private String skills;
	private String availability;
	private String posted_date;
	
	public JobEntity(int id, String role, String compensation, String location, String description, String replyRate,
			String skills, String availability, String posted_date) {
		this.id = id;
		this.role = role;
		this.compensation = compensation;
		this.location = location;
		this.description = description;
		this.replyRate = replyRate;
		this.skills = skills;
		this.availability = availability;
		this.posted_date = posted_date;
	}

	
	public JobEntity() {
		    this.id = counter.getAndIncrement();
	}
	

	public String getAvailability() {
		return availability;
	}


	public void setAvailability(String availability) {
		this.availability = availability;
	}


	public String getPosted_date() {
		return posted_date;
	}

	public void setPosted_date(String posted_date) {
		this.posted_date = posted_date;
	}


	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getCompensation() {
		return compensation;
	}
	public void setCompensation(String compensation) {
		this.compensation = compensation;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getReplyRate() {
		return replyRate;
	}
	public void setReplyRate(String replyRate) {
		this.replyRate = replyRate;
	}
	public String getSkills() {
		return skills;
	}
	public void setSkills(String skills) {
		this.skills = skills;
	}

	public int getId() {
		return id;
	}


}
