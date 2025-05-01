package org.example.erpspring.controller.quotation;

import org.example.erpspring.service.quotation.QuotationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String getQuotationitems(@RequestParam("name") String name, Model model){
        List<Map<String,Object>> fournisseurStrig = quotationService.getQuotationItems(name);

        model.addAttribute("quotations",fournisseurStrig);
        return "quotation/itemList";
    }


}

