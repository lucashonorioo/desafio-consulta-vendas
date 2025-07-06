package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import com.devsuperior.dsmeta.projection.SaleSumProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.entities.Sale;
import com.devsuperior.dsmeta.repositories.SaleRepository;

@Service
public class SaleService {

	@Autowired
	private SaleRepository repository;
	
	public SaleMinDTO findById(Long id) {
		Optional<Sale> result = repository.findById(id);
		Sale entity = result.get();
		return new SaleMinDTO(entity);
	}

	public Page<SaleMinDTO> findRelatorio(Pageable pageable, String dataInicialString, String dataFinalString, String nomeVendedor){
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());


		LocalDate dataInicial;
		LocalDate dataFinal;
		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		if (dataFinalString == null || dataFinalString.isEmpty()) {
			dataFinal = today;
		} else {
			try {
				dataFinal = LocalDate.parse(dataFinalString, fmt);
			} catch (DateTimeParseException e) {
				throw new IllegalArgumentException("Formato de dataFinal inválido, use yyyy-MM-dd", e);
			}
		}

		if (dataInicialString == null || dataInicialString.isEmpty()) {
			dataInicial = dataFinal.minusYears(1L);
		} else {
			try {
				dataInicial = LocalDate.parse(dataInicialString, fmt);
			} catch (DateTimeParseException e) {
				throw new IllegalArgumentException("Formato de dataInicial inválido, use yyyy-MM-dd", e);
			}
		}

		Page<Sale> sales = repository.findRelatorio(pageable, dataInicial, dataFinal, nomeVendedor);
		Page<SaleMinDTO> saleMinDTOS = sales.map( s -> new SaleMinDTO(s));

		return saleMinDTOS;
	}

	public List<SaleSummaryDTO> findSumario(String dataInicialString, String dataFinalString){
		LocalDate today = LocalDate.ofInstant(Instant.now(), ZoneId.systemDefault());

		DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		LocalDate dataInicial;
		LocalDate dataFinal;
		if(dataFinalString == null || dataFinalString.isEmpty()){
			dataFinal = today;
		}
		else{
			try{
				dataFinal = LocalDate.parse(dataFinalString, fmt);
			}
			catch (DateTimeParseException e){
				throw new IllegalArgumentException("Formato de dataInicial inválido, use yyyy-MM-dd", e);
			}
		}
		if(dataInicialString == null || dataInicialString.isEmpty()){
			dataInicial = dataFinal.minusYears(1L);
		}
		else {
			try{
				dataInicial = LocalDate.parse(dataInicialString, fmt);
			}
			catch (DateTimeParseException e){
				throw new IllegalArgumentException("Formato de dataInicial inválido, use yyyy-MM-dd", e);
			}
		}

		List<SaleSumProjection> sales = repository.findSumario(dataInicial, dataFinal);
		List<SaleSummaryDTO> saleMinDTOS = sales.stream().map( s -> new SaleSummaryDTO(s.getSellerName(), s.getTotal())).toList();
		return saleMinDTOS;
	}

}
