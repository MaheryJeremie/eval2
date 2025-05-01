package org.example.erpspring.controller;


import org.example.erpspring.service.FrappeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/frappe")
public class FrappeController {
    @Autowired
    private FrappeService frappeService;
    @GetMapping("/employees")
    public ResponseEntity<?> getEmployees() {
        return ResponseEntity.ok(frappeService.getAllEmployees());
    }
}
