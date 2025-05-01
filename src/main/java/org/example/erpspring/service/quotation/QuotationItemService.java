package org.example.erpspring.service.quotation;
import org.example.erpspring.service.FrappeService;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class QuotationItemService {
    private static final String QUOTATION_ENDPOINT = "/resource/Supplier Quotation Item";

    private final FrappeService apiService;

    public QuotationItemService(FrappeService apiService) {
        this.apiService = apiService;
    }
    public void setQuotationItem(String name,String rate ) {
        String endpoint = QUOTATION_ENDPOINT +"/"+ name;

        Map<String, Object> payload = new HashMap<>();
        payload.put("rate", rate);

        // Utilise ta méthode `send` pour faire le PUT
        ResponseEntity<Map> response = apiService.send(endpoint, payload, HttpMethod.PUT);

        System.out.println("Response: " + response.getBody());
    }
}
