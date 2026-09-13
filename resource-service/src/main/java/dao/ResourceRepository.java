package dao;

import entities.Resource;
import entities.ResourceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {

    List<Resource> findByCategoryIgnoreCase(String category);

    List<Resource> findByStatus(ResourceStatus status);

    List<Resource> findByNameContainingIgnoreCase(String name);
}