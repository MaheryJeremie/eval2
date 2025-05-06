package org.example.erpspring.service.quotation;

import org.example.erpspring.service.FrappeService;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QuotationService {
    private static final String REQUEST_QUOTATION_ENDPOINT = "/resource/Request for Quotation";
    private static final String SUPPLIER_QUOTATION_ENDPOINT = "/resource/Supplier Quotation";
    private static final String SUBMIT_ENDPOINT = "/method/frappe.client.submit";

    private final FrappeService apiService;

    public QuotationService(FrappeService apiService) {
        this.apiService = apiService;
    }

    public List<Map<String, Object>> getSupplierQuotations(String supplierId) {
        var response = apiService.get(
                SUPPLIER_QUOTATION_ENDPOINT,
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
                SUPPLIER_QUOTATION_ENDPOINT+"/"+name,null
        );
        Map<String, Object> data =  (Map<String, Object>)  response.getBody().get("data");
        return (List<Map<String, Object>>) data.get("items");
    }

    public List<Map<String, Object>> getAllRequestQuotations() {
        var response = apiService.get(
                REQUEST_QUOTATION_ENDPOINT,
                Map.of(
                        "fields", "[\"name\",\"transaction_date\",\"suppliers.supplier\"]"
                )
        );
        return (List<Map<String, Object>>) response.getBody().get("data");
    }
    public List<Map<String, Object>> getRFQsBySupplier(String supplierName) {
        List<Map<String, Object>> allRfqs = getAllRequestQuotations();
        return allRfqs.stream()
                .filter(rfq -> supplierName.equals(rfq.get("supplier")))
                .collect(Collectors.toList());
    }
    public List<Map<String, Object>> getRfqItems(String name) {
        var response = apiService.get(
                REQUEST_QUOTATION_ENDPOINT+"/"+name,null
        );
        Map<String, Object> data =  (Map<String, Object>)  response.getBody().get("data");
        return (List<Map<String, Object>>) data.get("items");
    }
    public String createSupplierQuotation(String rfq, String supplierName, Map<String, Double> itemRates) {
        List<Map<String, Object>> rfqItems = getRfqItems(rfq);

        List<Map<String, Object>> sqItems = new ArrayList<>();
        for (Map<String, Object> item : rfqItems) {
            String itemCode = (String) item.get("item_code");
            Map<String, Object> sqItem = new HashMap<>();
            sqItem.put("item_code", itemCode);
            sqItem.put("qty", item.get("qty"));
            sqItem.put("rate", itemRates.get(itemCode));
            sqItem.put("schedule_date", LocalDate.now().toString());
            sqItems.add(sqItem);
        }

        Map<String, Object> sqData = new HashMap<>();
        sqData.put("supplier", supplierName);
        sqData.put("items", sqItems);
        sqData.put("transaction_date", LocalDate.now().toString());
        sqData.put("valid_till", LocalDate.now().toString());
        sqData.put("company", "Hehe");
        sqData.put("request_for_quotation", rfq);

        ResponseEntity<Map> response=apiService.send(
                SUPPLIER_QUOTATION_ENDPOINT,
                sqData,
                HttpMethod.POST
        );
        if (response.getBody() == null || !response.getBody().containsKey("data")) {
            throw new RuntimeException("Failed to create payment entry: No response data");
        }
        String sqEntryName = ((Map<String, String>) response.getBody().get("data")).get("name");
//        ResponseEntity<Map> getResponse = apiService.get(
//                SUPPLIER_QUOTATION_ENDPOINT + "/" + sqEntryName,
//                null
//        );
//
//        if (getResponse.getBody() == null || !getResponse.getBody().containsKey("data")) {
//            throw new RuntimeException("Échec de récupération du Payment Entry : " + sqEntryName);
//        }
//
//        Map<String, Object> sqEntry = (Map<String, Object>) getResponse.getBody().get("data");
//        Map<String, Object> submitData = new HashMap<>();
//        submitData.put("doc", sqEntry);
//        ResponseEntity<Map> submitResponse = apiService.send(
//                SUBMIT_ENDPOINT,
//                submitData,
//                HttpMethod.POST
//        );
//
//        if (submitResponse.getStatusCode() != HttpStatus.OK) {
//            throw new RuntimeException("Échec de soumission du Payment Entry : " + submitResponse.getBody());
//        }
        return sqEntryName;
    }
    public void submitSupplierQuotation(String sqEntryName){
        ResponseEntity<Map> getResponse = apiService.get(
                SUPPLIER_QUOTATION_ENDPOINT + "/" + sqEntryName,
                null
        );
        if (getResponse.getBody() == null || !getResponse.getBody().containsKey("data")) {
            throw new RuntimeException("Échec de récupération du Supplier Quotation : " + sqEntryName);
        }
        Map<String, Object> sqEntry = (Map<String, Object>) getResponse.getBody().get("data");
        Map<String, Object> submitData = new HashMap<>();
        submitData.put("doc", sqEntry);
        ResponseEntity<Map> submitResponse = apiService.send(
                SUBMIT_ENDPOINT,
                submitData,
                HttpMethod.POST
        );

        if (submitResponse.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Échec de soumission du Supplier Quotation Entry : " + submitResponse.getBody());
        }
    }

}