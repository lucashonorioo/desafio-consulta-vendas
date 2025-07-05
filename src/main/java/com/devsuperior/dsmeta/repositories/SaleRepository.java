package com.devsuperior.dsmeta.repositories;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.projection.SaleSumProjection;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.devsuperior.dsmeta.entities.Sale;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    @Query("SELECT obj FROM Sale obj " +
            "WHERE obj.date BETWEEN :dataInicial AND :dataFinal AND UPPER(obj.seller.name) LIKE UPPER(CONCAT('%', :nomeVendedor, '%')) ")
    Page<Sale> findRelatorio(Pageable pageable, LocalDate dataInicial, LocalDate dataFinal, String nomeVendedor);

    @Query("SELECT obj.seller.name AS sellerName, SUM(obj.amount) AS total " +
            "FROM Sale obj " +
            "WHERE obj.date BETWEEN :dataInicial AND :dataFinal " +
            "GROUP BY obj.seller.name")
    List<SaleSumProjection> findSumario(LocalDate dataInicial, LocalDate dataFinal);
}
