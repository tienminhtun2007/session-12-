package com.example.myspringbootprojectsession12.repositories;

import com.example.myspringbootprojectsession12.dto.DailyExportResponse;
import com.example.myspringbootprojectsession12.dto.TopExportResponse;
import com.example.myspringbootprojectsession12.entities.StockTransaction;
import com.example.myspringbootprojectsession12.entities.TransactionType;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    boolean existsByType(TransactionType type);

    @Query("""
        select new com.example.myspringbootprojectsession12.dto.DailyExportResponse(
          t.supply.id, t.supply.name, sum(t.amount))
        from StockTransaction t
        where t.type = :type and t.createdAt >= :from and t.createdAt < :to
        group by t.supply.id, t.supply.name
        order by sum(t.amount) desc
        """)
    List<DailyExportResponse> findDailyExports(@Param("type") TransactionType type,
                                                @Param("from") LocalDateTime from,
                                                @Param("to") LocalDateTime to);

    @Query("""
        select new com.example.myspringbootprojectsession12.dto.TopExportResponse(
          t.supply.name, sum(t.amount))
        from StockTransaction t
        where t.type = :type
        group by t.supply.id, t.supply.name
        order by sum(t.amount) desc
        """)
    List<TopExportResponse> findTopExports(@Param("type") TransactionType type);
}
