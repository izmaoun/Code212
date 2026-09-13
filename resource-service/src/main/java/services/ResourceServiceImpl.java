package services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dao.ResourceRepository;
import dto.ResourceRequest;
import dto.ResourceResponse;
import entities.Resource;
import exceptions.InsufficientStockException;
import exceptions.ResourceNotFoundException;
import mapper.ResourceMapper;
import services.ResourceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository repository;
    private final ResourceMapper mapper;

    @Override
    public ResourceResponse create(ResourceRequest request) {
        log.debug("Création d'une ressource : {}", request.name());
        Resource resource = mapper.toEntity(request);
        if (resource.getQuantityAvailable() == null) {
            resource.setQuantityAvailable(resource.getQuantityTotal());
        }
        return mapper.toResponse(repository.save(resource));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponse> findAll() {
        return mapper.toResponseList(repository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceResponse> search(String category, String name) {
        if (category != null && !category.isBlank()) {
            return mapper.toResponseList(repository.findByCategoryIgnoreCase(category));
        }
        if (name != null && !name.isBlank()) {
            return mapper.toResponseList(repository.findByNameContainingIgnoreCase(name));
        }
        return findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public ResourceResponse findById(Long id) {
        return mapper.toResponse(getOrThrow(id));
    }

    @Override
    public ResourceResponse update(Long id, ResourceRequest request) {
        Resource resource = getOrThrow(id);
        mapper.updateEntity(request, resource);
        return mapper.toResponse(repository.save(resource));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean checkAvailability(Long id, int quantity) {
        return getOrThrow(id).isAvailable(quantity);
    }

    @Override
    public ResourceResponse updateStock(Long id, int delta) {
        Resource resource = getOrThrow(id);
        try {
            if (delta < 0) resource.decrementStock(-delta);
            else           resource.incrementStock(delta);
        } catch (IllegalStateException ex) {
            throw new InsufficientStockException(ex.getMessage());
        }
        return mapper.toResponse(repository.save(resource));
    }

    private Resource getOrThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}