package org.example.erpspring.controller.quotation;
import org.example.erpspring.service.quotation.QuotationItemService;
import org.example.erpspring.service.quotation.QuotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/quotationItems")
public class QuotationItemController {
    @Autowired
    private QuotationItemService quotationItemService;
    @Autowired
    private QuotationService quotationService;

    @PostMapping("/update")
    public String update(
            @RequestParam String quotationName,
            @RequestParam String supplierName,
            @RequestParam Map<String, String> allParams) {

        Map<String, Double> itemRates = new HashMap<>();
        for (String paramName : allParams.keySet()) {
            if (paramName.startsWith("rate_")) {
                String itemName = paramName.substring(5);
                double rate = Double.parseDouble(allParams.get(paramName));
                itemRates.put(itemName, rate);
            }
        }

        for (Map.Entry<String, Double> entry : itemRates.entrySet()) {
            String itemName = entry.getKey();
            String rate = String.valueOf(entry.getValue());
            quotationItemService.setQuotationItem(itemName, rate);
        }

        quotationService.submitSupplierQuotation(quotationName);

        return "redirect:/quotations/" + supplierName;
    }
}
