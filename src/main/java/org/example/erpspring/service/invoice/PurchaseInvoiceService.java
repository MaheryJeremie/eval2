package org.example.erpspring.service.invoice;

import org.example.erpspring.service.FrappeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service

public class PurchaseInvoiceService {
    private static final String INVOICE_ENDPOINT = "/resource/Purchase Invoice";
    private final FrappeService apiService;
    public PurchaseInvoiceService(FrappeService apiService) {
        this.apiService = apiService;
    }
    public List<Map<String, Object>> getSupplierInvoices() {
        var response = apiService.get(
                INVOICE_ENDPOINT,
                Map.of(
                        "fields", "[\"name\", \"due_date\", \"outstanding_amount\", \"status\", \"supplier\", \"items.purchase_order\"]"
                )
        );
        return (List<Map<String, Object>>) response.getBody().get("data");
    }
}
