package com.thelocalclass.utm.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Monitor {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @Column(name = "status", nullable = false, length = 15)
    private String status;
    
    @Column(name = "response_time", nullable = false, length = 5)
    private Long responseTime;
    
    @Column(name = "check_id", nullable = false, length = 10)
    private Integer checkId;
    
    @Column(name = "since", nullable = false, length = 10)
    private LocalDateTime since;
    
    @Column(name = "last", nullable = false, length = 10)
    private LocalDateTime last;
    
    @Column(name = "down_tracker", nullable = false, length = 10)
    private Integer tracker;
    
    public Monitor() {
    }
    
    public Monitor(String status, Long responseTime, Integer checkId, LocalDateTime since, LocalDateTime last, Integer tracker) {
        this.status = status;
        this.responseTime = responseTime;
        this.checkId = checkId;
        this.since = since;
        this.last = last;
        this.tracker =  tracker; 
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(Long responseTime) {
		this.responseTime = responseTime;
	}

	public Integer getCheckId() {
		return checkId;
	}

	public void setCheckId(Integer checkId) {
		this.checkId = checkId;
	}

	public LocalDateTime getSince() {
		return since;
	}

	public void setSince(LocalDateTime since) {
		this.since = since;
	}

	public LocalDateTime getLast() {
		return last;
	}

	public void setLast(LocalDateTime last) {
		this.last = last;
	}

	public Integer getTracker() {
		return tracker;
	}

	public void setTracker(Integer tracker) {
		this.tracker = tracker;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((checkId == null) ? 0 : checkId.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((last == null) ? 0 : last.hashCode());
		result = prime * result + ((responseTime == null) ? 0 : responseTime.hashCode());
		result = prime * result + ((since == null) ? 0 : since.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tracker == null) ? 0 : tracker.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Monitor other = (Monitor) obj;
		if (checkId == null) {
			if (other.checkId != null)
				return false;
		} else if (!checkId.equals(other.checkId))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (last == null) {
			if (other.last != null)
				return false;
		} else if (!last.equals(other.last))
			return false;
		if (responseTime == null) {
			if (other.responseTime != null)
				return false;
		} else if (!responseTime.equals(other.responseTime))
			return false;
		if (since == null) {
			if (other.since != null)
				return false;
		} else if (!since.equals(other.since))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (tracker == null) {
			if (other.tracker != null)
				return false;
		} else if (!tracker.equals(other.tracker))
			return false;
		return true;
	}
	
}
