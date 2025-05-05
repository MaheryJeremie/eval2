package org.example.erpspring.service.supplier;

import org.example.erpspring.service.FrappeService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SupplierService {
    private static final String SUPPLIER_ENDPOINT = "/resource/Supplier";

    private final FrappeService apiService;

    public SupplierService(FrappeService apiService) {
        this.apiService = apiService;
    }

    public List<Map<String, Object>> getAllSuppliers() {
        ResponseEntity<Map> response = apiService.get(
                SUPPLIER_ENDPOINT,
                Map.of(
                        "fields", "[\"name\", \"supplier_name\", \"supplier_type\", \"country\"]"
                )
        );
        return (List<Map<String, Object>>) response.getBody().get("data");
    }
    public List<Map<String, Object>> getAllSuppliersByName(String name) {
        ResponseEntity<Map> response = apiService.get(
                SUPPLIER_ENDPOINT,
                Map.of(
                        "fields", "[\"name\", \"supplier_name\", \"supplier_type\", \"country\"]",
                        "filters", String.format("[[\"name\",\"like\",\"%s\"]]", "%" + name + "%")
                )
        );
        return (List<Map<String, Object>>) response.getBody().get("data");
    }

    public Map<String, Object> getSupplierById(String supplierId) {
        ResponseEntity<Map> response = apiService.get(
                SUPPLIER_ENDPOINT + "/" + supplierId,
                null
        );
        return response.getBody();
    }
}