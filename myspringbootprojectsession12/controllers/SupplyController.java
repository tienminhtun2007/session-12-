package com.example.myspringbootprojectsession12.controllers;

import com.example.myspringbootprojectsession12.dto.*;
import com.example.myspringbootprojectsession12.services.SupplyService;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/supplies")
public class SupplyController {
    private final SupplyService service;

    public SupplyController(SupplyService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<SupplyResponse>> create(@Valid @RequestBody SupplyCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(true, "Tạo vật tư thành công", service.create(request)));
    }

    @PutMapping("/{id}")
    public ApiResponse<SupplyResponse> update(@PathVariable Long id,
                                               @Valid @RequestBody SupplyUpdateRequest request) {
        return new ApiResponse<>(true, "Cập nhật vật tư thành công", service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ApiResponse<List<SupplyResponse>> findAll() {
        return new ApiResponse<>(true, "Lấy danh sách vật tư thành công", service.findAll());
    }

    @GetMapping("/search")
    public ApiResponse<List<SupplyResponse>> search(@RequestParam("name") String name) {
        return new ApiResponse<>(true, "Tìm kiếm vật tư thành công", service.search(name));
    }

    @PatchMapping("/{id}/export")
    public ApiResponse<SupplyResponse> export(@PathVariable Long id,
                                               @Valid @RequestBody StockAmountRequest request) {
        return new ApiResponse<>(true, "Xuất kho thành công", service.export(id, request.getAmount()));
    }

    @PatchMapping("/{id}/import")
    public ApiResponse<SupplyResponse> importStock(@PathVariable Long id,
                                                    @Valid @RequestBody StockAmountRequest request) {
        return new ApiResponse<>(true, "Nhập kho thành công", service.importStock(id, request.getAmount()));
    }

    @GetMapping("/statistics/daily-export")
    public ApiResponse<List<DailyExportResponse>> dailyExport() {
        return new ApiResponse<>(true, "Thống kê xuất kho trong ngày", service.dailyExport());
    }

    @GetMapping("/statistics/top-export")
    public ApiResponse<TopExportResponse> topExport() {
        return new ApiResponse<>(true, "Thống kê vật tư xuất nhiều nhất", service.topExport());
    }
}
