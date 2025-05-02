package org.example.erpspring.service.payment;

import org.example.erpspring.service.FrappeService;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class PaymentService {
    private static final String PAYMENT_ENDPOINT = "/resource/Payment Entry";
    private static final String SUBMIT_ENDPOINT = "/method/frappe.client.submit";

    private final FrappeService apiService;

    public PaymentService(FrappeService apiService) {
        this.apiService = apiService;
    }

    public String createPayment(String supplier, String paymentMethod,
                                String invoiceId, double amount) {
        Map<String, Object> paymentData = buildPaymentData(supplier, paymentMethod, invoiceId, amount);

        ResponseEntity<Map> createResponse = apiService.send(
                PAYMENT_ENDPOINT,
                paymentData,
                HttpMethod.POST
        );
        if (createResponse.getBody() == null || !createResponse.getBody().containsKey("data")) {
            throw new RuntimeException("Failed to create payment entry: No response data");
        }

        String paymentEntryName = ((Map<String, String>) createResponse.getBody().get("data")).get("name");

        submitPaymentEntry(paymentEntryName);

        return paymentEntryName;
    }

    private Map<String, Object> buildPaymentData(String supplier, String paymentMethod,
                                                 String invoiceId, double amount) {
        Map<String, Object> paymentData = new HashMap<>();
        paymentData.put("payment_type", "Pay");
        paymentData.put("party_type", "Supplier");
        paymentData.put("party", supplier);
        paymentData.put("paid_amount", amount);
        paymentData.put("received_amount", amount);
        paymentData.put("mode_of_payment", paymentMethod);
        paymentData.put("source_exchange_rate", 1.0);
        paymentData.put("company", "Hehe");
        paymentData.put("party_account", "Creditors - H");
        String paidFromAccount = switch (paymentMethod) {
            case "Cash" -> "Cash - H";
            case "Bank Transfer" -> "Bank Account - H";
            case "Credit Card" -> "Credit Card - H";
            default -> throw new IllegalArgumentException("Mode de paiement non supporté: " + paymentMethod);
        };
        paymentData.put("paid_from", paidFromAccount);
        paymentData.put("paid_to", "Creditors - H");
        if ("Bank Transfer".equals(paymentMethod)) {
            paymentData.put("bank_account", "YOUR_BANK_ACCOUNT");
        } else if ("Cheque".equals(paymentMethod)) {
            paymentData.put("cheque_no", generateChequeNumber());
            paymentData.put("cheque_date", LocalDate.now().toString());
        }

        Map<String, Object> reference = new HashMap<>();
        reference.put("reference_doctype", "Purchase Invoice");
        reference.put("reference_name", invoiceId);
        reference.put("allocated_amount", amount);

        paymentData.put("references", List.of(reference));

        return paymentData;
    }

    private void submitPaymentEntry(String paymentEntryName) {
        ResponseEntity<Map> getResponse = apiService.get(
                PAYMENT_ENDPOINT + "/" + paymentEntryName,
                null
        );

        if (getResponse.getBody() == null || !getResponse.getBody().containsKey("data")) {
            throw new RuntimeException("Échec de récupération du Payment Entry : " + paymentEntryName);
        }

        Map<String, Object> paymentEntry = (Map<String, Object>) getResponse.getBody().get("data");
        Map<String, Object> submitData = new HashMap<>();
        submitData.put("doc", paymentEntry);
        ResponseEntity<Map> submitResponse = apiService.send(
                SUBMIT_ENDPOINT,
                submitData,
                HttpMethod.POST
        );

        if (submitResponse.getStatusCode() != HttpStatus.OK) {
            throw new RuntimeException("Échec de soumission du Payment Entry : " + submitResponse.getBody());
        }
    }

    public String generateChequeNumber() {
        return "CHQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

}