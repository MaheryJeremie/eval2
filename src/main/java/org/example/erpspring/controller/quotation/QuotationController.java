package org.example.erpspring.controller.quotation;

import org.example.erpspring.service.quotation.QuotationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/quotations")
public class QuotationController {
    private final QuotationService quotationService;

    public QuotationController(QuotationService quotationService) {
        this.quotationService = quotationService;
    }

    @GetMapping("/{supplierId}")
    public String getQuotations(@PathVariable String supplierId, Model model) {
        model.addAttribute("quotations", quotationService.getSupplierQuotations(supplierId));
        model.addAttribute("supplierName", supplierId);
        return "quotation/quotations";
    }
    @GetMapping("/item")
    public String getQuotationitems(@RequestParam("name") String name,@RequestParam Boolean editable, Model model){
        List<Map<String,Object>> fournisseurStrig = quotationService.getQuotationItems(name);
        model.addAttribute("quotations",fournisseurStrig);
        model.addAttribute("editable",editable);
        return "quotation/itemList";
    }
    @GetMapping("/rfqs/{supplierId}")
    public String getRfqsByCustomer(@PathVariable String supplierId,Model model){
        model.addAttribute("quotations", quotationService.getRFQsBySupplier(supplierId));
        model.addAttribute("supplierName", supplierId);
        return "quotation/rfqs";
    }
    @GetMapping("/rfqs/item")
    public String getRfqsItems(@RequestParam String rfq,@RequestParam String supplierName,Model model){
        List<Map<String,Object>> items = quotationService.getRfqItems(rfq);
        model.addAttribute("items",items);
        model.addAttribute("rfq",rfq);
        model.addAttribute("supplierName",supplierName);
        return "quotation/rfqItemList";
    }
    @PostMapping("/create")
    public String createSupplierQuotation(
            @RequestParam String rfq,
            @RequestParam String supplierName,
            @RequestParam Map<String, String> allParams,
            RedirectAttributes redirectAttributes) {

        Map<String, Double> itemRates = new HashMap<>();
        for (String paramName : allParams.keySet()) {
            if (paramName.startsWith("rate_")) {
                String itemCode = paramName.substring(5);
                double rate = Double.valueOf(allParams.get(paramName));
                itemRates.put(itemCode, rate);
            }
        }
        try {
            String sqNumber = quotationService.createSupplierQuotation(rfq, supplierName, itemRates);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Erreur lors de la création: " + e.getMessage());
            return "redirect:/quotations/rfqs/item?rfq="+rfq+"&supplierName="+supplierName;
        }

        return "redirect:/quotations/"+supplierName;
    }

}

