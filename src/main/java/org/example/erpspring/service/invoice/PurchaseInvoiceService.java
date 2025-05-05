package org.example.erpspring.service.invoice;

import org.example.erpspring.service.FrappeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service

public class PurchaseInvoiceService {
    private static final String INVOICE_ENDPOINT = "/resource/Purchase Invoice";
    private final FrappeService apiService;
    public PurchaseInvoiceService(FrappeService apiService) {
        this.apiService = apiService;
    }
    public List<Map<String, Object>> getAllInvoices() {
        var response = apiService.get(
                INVOICE_ENDPOINT,
                Map.of(
                        "fields", "[\"name\", \"due_date\",\"total\",\"outstanding_amount\",\"currency\",\"status\", \"supplier\", \"items.purchase_order\"]"
                )
        );
        return (List<Map<String, Object>>) response.getBody().get("data");
    }
    public List<Map<String, Object>> getAllInvoicesByStatus(String status) {
        if (status.equals("all")) return getAllInvoices();
        var response = apiService.get(
                INVOICE_ENDPOINT,
                Map.of(
                        "fields", "[\"name\", \"due_date\",\"total\", \"outstanding_amount\",\"currency\", \"status\", \"supplier\", \"items.purchase_order\"]",
                        "filters", String.format("[[\"status\", \"=\", \"%s\"]]", status)
                )
        );
        return (List<Map<String, Object>>) response.getBody().get("data");
    }
    public Set<String> getDistinctStatuses() {
        List<Map<String, Object>> invoices = getAllInvoices();
        return invoices.stream()
                .map(invoice -> (String) invoice.get("status"))
                .collect(Collectors.toSet());
    }
}
