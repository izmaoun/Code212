package services;

import dto.ResourceRequest;
import dto.ResourceResponse;

import java.util.List;

public interface ResourceService {

    ResourceResponse create(ResourceRequest request);

    List<ResourceResponse> findAll();

    List<ResourceResponse> search(String category, String name);

    ResourceResponse findById(Long id);

    ResourceResponse update(Long id, ResourceRequest request);

    void delete(Long id);

    boolean checkAvailability(Long id, int quantity);

    ResourceResponse updateStock(Long id, int delta);
}