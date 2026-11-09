package com.example.myspringbootprojectsession12.repositories;

import com.example.myspringbootprojectsession12.entities.Supply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SupplyRepository extends JpaRepository<Supply, Long> {
    List<Supply> findAllByIsDeletedFalse();

    @Query("""
        select s from Supply s
        where s.isDeleted = false
          and lower(s.name) like lower(concat('%', :name, '%'))
        """)
    List<Supply> searchActiveByName(@Param("name") String name);

    Optional<Supply> findByIdAndIsDeletedFalse(Long id);
}
