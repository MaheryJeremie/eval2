package org.example.erpspring.service.purchaseorder;

import org.example.erpspring.service.FrappeService;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PurchaseOrderService {
    private static final String ORDER_ENDPOINT = "/resource/Purchase Order";

    private final FrappeService apiService;

    public PurchaseOrderService(FrappeService apiService) {
        this.apiService = apiService;
    }

    public List<Map<String, Object>> getSupplierOrders(String supplierId) {
        var response = apiService.get(
                ORDER_ENDPOINT,
                Map.of(
                        "fields", "[\"name\",\"status\", \"transaction_date\", \"grand_total\", \"currency\"]",
                        "filters", "[[\"supplier\", \"=\", \"" + supplierId + "\"]]"
                )
        );
        return (List<Map<String, Object>>) response.getBody().get("data");
    }

    public Map<String, Object> getOrderStatus(String orderId) {
        var response = apiService.get(
                ORDER_ENDPOINT + "/" + orderId,
                Map.of("fields", "[\"status\", \"per_received\", \"per_billed\"]")
        );
        return response.getBody();
    }
}