package org.example.erpspring.controller.calendar;

import org.example.erpspring.service.invoice.PurchaseInvoiceService;
import org.example.erpspring.service.purchaseorder.PurchaseOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/calendar")
public class CalendarController {
    private final PurchaseInvoiceService purchaseInvoiceService;

    public CalendarController(PurchaseInvoiceService purchaseInvoiceService) {
        this.purchaseInvoiceService = purchaseInvoiceService;
    }

    @GetMapping
    public String getAllOrderProgram(Model model) {
        List<Map<String, Object>> invoices = purchaseInvoiceService.getAllInvoices();

        List<Map<String, String>> calendarEvents = invoices.stream()
                .map(invoice -> {
                    Map<String, String> event = new HashMap<>();
                    event.put("title", (String) invoice.get("name"));
                    event.put("start", (String) invoice.get("due_date"));
                    event.put("description",
                            "Montant: " + invoice.get("total") + " " + invoice.get("currency") +
                                    "\nStatut: " + invoice.get("status"));
                    return event;
                })
                .collect(Collectors.toList());

        model.addAttribute("events", calendarEvents);
        return "calendar/calendar";
    }
}
