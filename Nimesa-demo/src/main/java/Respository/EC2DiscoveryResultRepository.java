package Respository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import model.EC2DiscoveryResult;

public interface EC2DiscoveryResultRepository extends JpaRepository<EC2DiscoveryResult, Long> {
 List<EC2DiscoveryResult> findByService(String service);
}
