package model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
//ServiceResult.java
import lombok.Data;

@Data
@Entity
public class ServiceResult {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long id;

 private String jobId;
 private String service;
 private String result;
public void setJobId(String string) {
	// TODO Auto-generated method stub
	
}
public String getResult() {
	// TODO Auto-generated method stub
	return null;
}
public void setService(String service2) {
	// TODO Auto-generated method stub
	
}
public void setResult(String result2) {
	// TODO Auto-generated method stub
	
}
public String getJobId() {
	// TODO Auto-generated method stub
	return null;
}
}
