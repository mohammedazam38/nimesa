package model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class S3DiscoveryResult {
 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String service;
 private String bucketName;
 private String fileName;
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
public String getBucketName() {
	return bucketName;
}
public void setBucketName(String bucketName) {
	this.bucketName = bucketName;
}
public String getFileName() {
	return fileName;
}
public void setFileName(String fileName) {
	this.fileName = fileName;
}
public S3DiscoveryResult(Long id, String service, String bucketName, String fileName) {
	super();
	this.id = id;
	this.service = service;
	this.bucketName = bucketName;
	this.fileName = fileName;
}
public S3DiscoveryResult() {
	super();
	// TODO Auto-generated constructor stub
}
 
}
