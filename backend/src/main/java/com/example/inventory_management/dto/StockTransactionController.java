package com.example.inventory_management.dto;

import com.example.inventory_management.dto.StockAdjustmentRequest;
import com.example.inventory_management.dto.StockTransactionResponse;
import com.example.inventory_management.service.StockTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stock-transactions")
@RequiredArgsConstructor
public class StockTransactionController {

    private final StockTransactionService stockTransactionService;

    @PostMapping("/adjust")
    public ResponseEntity<StockTransactionResponse> adjustStock(@RequestBody StockAdjustmentRequest request) {
        StockTransactionResponse response = stockTransactionService.adjustStock(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<StockTransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(stockTransactionService.getAllTransactions());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<StockTransactionResponse>> getByProductAndWarehouse(
            @RequestParam Long productId,
            @RequestParam Long warehouseId
    ) {
        return ResponseEntity.ok(stockTransactionService.getTransactionsByProductAndWarehouse(productId, warehouseId));
    }
}