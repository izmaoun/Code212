package mapper;

import dto.ResourceRequest;
import dto.ResourceResponse;
import entities.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface ResourceMapper {

    Resource toEntity(ResourceRequest request);

    ResourceResponse toResponse(Resource resource);

    List<ResourceResponse> toResponseList(List<Resource> resources);

    void updateEntity(ResourceRequest request, @MappingTarget Resource resource);
}