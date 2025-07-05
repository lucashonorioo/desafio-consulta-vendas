package com.devsuperior.dsmeta.services;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Optional;

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
		LocalDate result = today.minusYears(1L);

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
			dataInicial = result;
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

}
