package org.example.erpspring.model;

import lombok.Data;

import java.util.List;
import java.util.Map;
@Data
public class FrappeResponse {
    private List<Map<String, Object>> data;
}
