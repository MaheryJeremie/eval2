package org.example.erpspring.controller.payment;

import org.example.erpspring.service.invoice.PurchaseInvoiceService;
import org.example.erpspring.service.payment.PaymentModeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/purchaseInvoice")
public class PurchaseInvoiceController {
    private final PurchaseInvoiceService purchaseInvoiceServiceService;
    private final PaymentModeService paymentModeService;

    public PurchaseInvoiceController(PurchaseInvoiceService purchaseInvoiceServiceService, PaymentModeService paymentModeService) {
        this.purchaseInvoiceServiceService = purchaseInvoiceServiceService;
        this.paymentModeService = paymentModeService;
    }

    @GetMapping
    public String getInvoiceList(Model model){
        model.addAttribute("invoices",purchaseInvoiceServiceService.getSupplierInvoices());
        return "invoice/list";
    }
    @GetMapping("/form")
    public String showForm(
            @RequestParam String name,
            @RequestParam String supplier,
            @RequestParam String amount,
            @RequestParam String purchaseOrder,
            Model model) {

        model.addAttribute("invoiceId", name);
        model.addAttribute("supplier", supplier);
        model.addAttribute("amount", amount);
        model.addAttribute("purchaseOrder",purchaseOrder);
        model.addAttribute("paiementModes",paymentModeService.getPaymentModes());
        return "payment/form";
    }


}
