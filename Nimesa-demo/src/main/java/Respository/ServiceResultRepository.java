package Respository;


import org.springframework.data.jpa.repository.JpaRepository;

import model.ServiceResult;

import java.util.List;

public interface ServiceResultRepository extends JpaRepository<ServiceResult, Long> {

 List<ServiceResult> findByJobId(String jobId);

 List<ServiceResult> findByService(String service);

 List<ServiceResult> findByServiceAndResultLike(String service, String pattern);
}
