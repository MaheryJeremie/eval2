package org.example.erpspring.service.payment;

import org.example.erpspring.service.FrappeService;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PaymentService {
    private static final String INVOICE_ENDPOINT = "/resource/Purchase Invoice";
    private static final String PAYMENT_ENDPOINT = "/resource/Payment Entry";

    private final FrappeService apiService;

    public PaymentService(FrappeService apiService) {
        this.apiService = apiService;
    }

    public List<Map<String, Object>> getSupplierInvoices(String supplierId) {
        var response = apiService.get(
                INVOICE_ENDPOINT,
                Map.of(
                        "fields", "[\"name\", \"due_date\", \"outstanding_amount\", \"status\"]",
                        "filters", "[[\"supplier\", \"=\", \"" + supplierId + "\"]]"
                )
        );
        return (List<Map<String, Object>>) response.getBody().get("data");
    }

    public void createPayment(String invoiceId, double amount) {
        apiService.send(
                PAYMENT_ENDPOINT,
                Map.of(
                        "payment_type", "Pay",
                        "party_type", "Supplier",
                        "paid_amount", amount,
                        "references", List.of(
                                Map.of(
                                        "reference_doctype", "Purchase Invoice",
                                        "reference_name", invoiceId
                                )
                        )
                ),
                HttpMethod.POST
        );
    }
}