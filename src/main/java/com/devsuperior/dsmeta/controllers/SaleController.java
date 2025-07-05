package com.devsuperior.dsmeta.controllers;

import com.devsuperior.dsmeta.dto.SaleSummaryDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.devsuperior.dsmeta.dto.SaleMinDTO;
import com.devsuperior.dsmeta.services.SaleService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = "/sales")
public class SaleController {

	@Autowired
	private SaleService service;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<SaleMinDTO> findById(@PathVariable Long id) {
		SaleMinDTO dto = service.findById(id);
		return ResponseEntity.ok(dto);
	}

	@GetMapping(value = "/report")
	public ResponseEntity<Page<SaleMinDTO>> getReport(
			Pageable pageable,
			@RequestParam(value = "minDate", required = false) String minDate,
			@RequestParam(value = "maxDate", required = false) String maxDate,
			@RequestParam(value = "name", defaultValue = "") String name ) {
		Page<SaleMinDTO> saleMinDTOS = service.findRelatorio(pageable, minDate, maxDate, name);
		return ResponseEntity.ok().body(saleMinDTOS);
	}

	@GetMapping(value = "/summary")
	public ResponseEntity<List<SaleSummaryDTO>> getSummary(
			@RequestParam(value = "minDate", required = false) String minDate,
			@RequestParam(value = "maxDate", required = false) String maxDate
	) {
		List<SaleSummaryDTO> saleMinDTOS = service.findSumario(minDate, maxDate);
		return ResponseEntity.ok().body(saleMinDTOS);
	}



}
