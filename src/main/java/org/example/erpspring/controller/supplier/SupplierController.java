package org.example.erpspring.controller.supplier;

import org.example.erpspring.service.supplier.SupplierService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/suppliers")
public class SupplierController {
    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }
    @GetMapping
    public String getSuppliers(Model model) {
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        return "supplier/suppliers";
    }
}
