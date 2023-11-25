package com.ibrahimbayburtlu.inventoryservice.controller;

import com.ibrahimbayburtlu.inventoryservice.dto.InventoryResponse;
import com.ibrahimbayburtlu.inventoryservice.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {


    private final InventoryService inventoryService;

    // http://localhost:8082/api/inventory/iPhone-14
    // http://localhost:8082/api/inventory?sku-code=iPhone-14&sku-code=iPhone14-red
    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }
}
