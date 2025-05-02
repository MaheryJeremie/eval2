package org.example.erpspring.controller.payment;

import org.example.erpspring.service.payment.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/payment")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public String payer(@RequestParam String invoiceId,@RequestParam String supplier,@RequestParam String amount,@RequestParam String mode_of_payment){
        paymentService.createPayment(supplier, mode_of_payment, invoiceId, Double.valueOf(amount));
        return "redirect:/purchaseInvoice";
    }
}
