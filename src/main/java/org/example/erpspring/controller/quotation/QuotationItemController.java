package org.example.erpspring.controller.quotation;
import org.example.erpspring.service.quotation.QuotationItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/quotationItems")
public class QuotationItemController {
    @Autowired
    private QuotationItemService quotationService;

    @PostMapping("/update")
    public String update(@RequestParam String name,
                         @RequestParam String prix, Model model){
        quotationService.setQuotationItem(name,prix);
        return "redirect:/suppliers";
    }

    @GetMapping("/update")
    public String formulaireGet(@RequestParam("name") String name,@RequestParam("rate") String rate, Model model){
        model.addAttribute("name",name);
        model.addAttribute("rate",rate);
        return "quotation/UpdateQuotationItem";
    }

}
