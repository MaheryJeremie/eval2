package org.example.erpspring.service.payment;

import org.example.erpspring.service.FrappeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
@Service

public class PaymentModeService {
    private static final String PAYMENT_MODE_ENDPOINT = "/resource/Mode of Payment";
    private final FrappeService apiService;

    public PaymentModeService(FrappeService apiService) {
        this.apiService = apiService;
    }
    public List<Map<String, Object>> getPaymentModes() {
        var response = apiService.get(
                PAYMENT_MODE_ENDPOINT,
                Map.of()
        );
        return (List<Map<String, Object>>) response.getBody().get("data");
    }
}
