package org.example.erpspring.controller.purchaseorder;

import org.example.erpspring.service.purchaseorder.PurchaseOrderService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/purchaseorders")
public class PurchaseOrderController {
    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }
    @GetMapping("/{supplierId}")
    public String getPurchaseOrders(@PathVariable String supplierId, Model model){
        model.addAttribute("purchases",purchaseOrderService.getSupplierOrders(supplierId));
        model.addAttribute("supplierName", supplierId);
        return "purchaseorder/purchases";
    }
}
