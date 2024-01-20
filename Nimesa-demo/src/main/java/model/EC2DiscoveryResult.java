package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class EC2DiscoveryResult {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String service;
 private String instanceId;
public EC2DiscoveryResult() {
	super();
	// TODO Auto-generated constructor stub
}
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getService() {
	return service;
}
public void setService(String service) {
	this.service = service;
}
public String getInstanceId() {
	return instanceId;
}
public void setInstanceId(String instanceId) {
	this.instanceId = instanceId;
}
public EC2DiscoveryResult(Long id, String service, String instanceId) {
	super();
	this.id = id;
	this.service = service;
	this.instanceId = instanceId;
}
 
}
