package clients;

import dto.ResourceDto;
import dto.StockUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "resource-service", url = "${clients.resource-service.url:http://resource-service:8082}")
public interface ResourceClient {

    @GetMapping("/api/resources/{id}")
    ResourceDto getResourceById(@PathVariable Long id);

    @PatchMapping("/api/resources/{id}/stock")
    ResourceDto updateStock(@PathVariable Long id, @RequestBody StockUpdateRequest request);
}
