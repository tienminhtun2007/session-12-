package com.example.myspringbootprojectsession12.services;

import com.example.myspringbootprojectsession12.dto.*;
import com.example.myspringbootprojectsession12.entities.*;
import com.example.myspringbootprojectsession12.exceptions.*;
import com.example.myspringbootprojectsession12.repositories.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.List;

@Slf4j
@Service
public class SupplyService {
    private final SupplyRepository supplyRepository;
    private final StockTransactionRepository transactionRepository;

    public SupplyService(SupplyRepository supplyRepository, StockTransactionRepository transactionRepository) {
        this.supplyRepository = supplyRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public SupplyResponse create(SupplyCreateRequest request) {
        Supply saved = supplyRepository.save(new Supply(request.getName().trim(),
                request.getSpecification(), request.getProvider()));
        log.info("Đã tạo mới vật tư: {} với ID: {}", saved.getName(), saved.getId());
        return SupplyResponse.from(saved);
    }

    @Transactional
    public SupplyResponse update(Long id, SupplyUpdateRequest request) {
        if (!request.getUnknownFields().isEmpty()) {
            log.warn("Client gửi dữ liệu cấm khi cập nhật vật tư ID {}: {}", id, request.getUnknownFields().keySet());
            throw new ForbiddenFieldException("Chỉ được phép cập nhật các trường name, specification, provider");
        }
        Supply supply = findActive(id);
        supply.setName(request.getName().trim());
        supply.setSpecification(request.getSpecification());
        supply.setProvider(request.getProvider());
        log.info("Đã cập nhật thông tin vật tư ID: {}", id);
        return SupplyResponse.from(supplyRepository.save(supply));
    }

    @Transactional
    public void softDelete(Long id) {
        Supply supply = findActive(id);
        supply.setDeleted(true);
        supplyRepository.save(supply);
        log.info("Đã xóa mềm vật tư ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<SupplyResponse> findAll() {
        List<SupplyResponse> result = supplyRepository.findAllByIsDeletedFalse().stream()
                .map(SupplyResponse::from).toList();
        log.debug("Đã truy vấn {} vật tư đang hoạt động", result.size());
        return result;
    }

    @Transactional(readOnly = true)
    public List<SupplyResponse> search(String name) {
        List<SupplyResponse> result = supplyRepository.searchActiveByName(name.trim())
                .stream().map(SupplyResponse::from).toList();
        if (result.isEmpty()) {
            log.info("Không tìm thấy vật tư nào khớp với từ khóa: {}", name);
        }
        return result;
    }

    @Transactional
    public SupplyResponse export(Long id, int amount) {
        Supply supply = findActive(id);
        if (supply.getQuantity() < amount) {
            log.error("Thất bại khi xuất kho ID {}: Yêu cầu {}, hiện có {}", id, amount, supply.getQuantity());
            throw new BusinessException("Số lượng tồn kho không đủ để xuất");
        }
        supply.setQuantity(supply.getQuantity() - amount);
        transactionRepository.save(new StockTransaction(supply, TransactionType.EXPORT, amount));
        log.info("Đã xuất kho ID {}, số lượng {}", id, amount);
        return SupplyResponse.from(supplyRepository.save(supply));
    }

    @Transactional
    public SupplyResponse importStock(Long id, int amount) {
        Supply supply = findActive(id);
        int oldQuantity = supply.getQuantity();
        supply.setQuantity(oldQuantity + amount);
        transactionRepository.save(new StockTransaction(supply, TransactionType.IMPORT, amount));
        HistoryLogger.log("Nhập kho ID {}, số lượng [+{}], tồn cũ {}", id, amount, oldQuantity);
        log.info("Đã nhập kho ID {}, số lượng {}", id, amount);
        return SupplyResponse.from(supplyRepository.save(supply));
    }

    @Transactional(readOnly = true)
    public List<DailyExportResponse> dailyExport() {
        LocalDate today = LocalDate.now();
        log.info("Bắt đầu thống kê xuất kho ngày {}", today);
        List<DailyExportResponse> result = transactionRepository.findDailyExports(TransactionType.EXPORT,
                today.atStartOfDay(), today.plusDays(1).atStartOfDay());
        log.info("Hoàn thành thống kê xuất kho ngày {}, thời gian kết thúc {}", today, LocalDateTime.now());
        return result;
    }

    @Transactional(readOnly = true)
    public TopExportResponse topExport() {
        if (!transactionRepository.existsByType(TransactionType.EXPORT)) {
            throw new ResourceNotFoundException("Chưa có dữ liệu giao dịch để thống kê");
        }
        return transactionRepository.findTopExports(TransactionType.EXPORT).get(0);
    }

    private Supply findActive(Long id) {
        return supplyRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vật tư với ID: " + id));
    }
}
