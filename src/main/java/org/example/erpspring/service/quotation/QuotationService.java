package org.example.erpspring.service.quotation;

import org.example.erpspring.service.FrappeService;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class QuotationService {
    private static final String QUOTATION_ENDPOINT = "/resource/Supplier Quotation";

    private final FrappeService apiService;

    public QuotationService(FrappeService apiService) {
        this.apiService = apiService;
    }

    public List<Map<String, Object>> getSupplierQuotations(String supplierId) {
        var response = apiService.get(
                QUOTATION_ENDPOINT,
                Map.of(
                        "fields", "[\"name\", \"status\", \"valid_till\", \"grand_total\"]",
                        "filters", "[[\"supplier\", \"=\", \"" + supplierId + "\"]]",
                        "order_by", "creation desc"
                )
        );
        return (List<Map<String, Object>>) response.getBody().get("data");
    }

    public List<Map<String, Object>> getQuotationItems(String name) {
        var response = apiService.get(
                QUOTATION_ENDPOINT+"/"+name,null
        );
        System.out.println( response.getBody());
        Map<String, Object> data =  (Map<String, Object>)  response.getBody().get("data");
        return (List<Map<String, Object>>) data.get("items");
    }
}