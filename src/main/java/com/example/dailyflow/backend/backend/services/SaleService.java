package com.example.dailyflow.backend.backend.services;

import com.example.dailyflow.backend.backend.models.dto.SaleDTO;
import com.example.dailyflow.backend.backend.models.dto.SaleDetailDTO;
import com.example.dailyflow.backend.backend.models.entities.*;
import com.example.dailyflow.backend.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public Sale registerSale(SaleDTO saleDTO) {
        Sale sale = new Sale();
        sale.setPaymentMethod(saleDTO.getPaymentMethod());
        sale.setDate(LocalDate.now());
        sale.setTime(LocalTime.now());

        List<SaleDetail> detailList = new ArrayList<>();
        double total = 0.0;

        for (SaleDetailDTO detailDTO : saleDTO.getItems()) {
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with ID: " + detailDTO.getProductId()));

            if (product.getStock() < detailDTO.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - detailDTO.getQuantity());
            productRepository.save(product);

            SaleDetail detail = new SaleDetail();
            detail.setProduct(product);
            detail.setQuantity(detailDTO.getQuantity());
            detail.setSale(sale);
            detailList.add(detail);

            total += product.getSalePrice() * detailDTO.getQuantity();

            System.out.println("🧮 Stock actual: " + product.getStock());
            System.out.println("🧾 Restando cantidad: " + detailDTO.getQuantity());
            System.out.println("💾 Nuevo stock: " + (product.getStock() - detailDTO.getQuantity()));

        }

        sale.setAmount(total);
        sale.setDetails(detailList);

        return saleRepository.save(sale);
    }

    public List<Sale> getAllSales() {
        return saleRepository.findAll();
    }

    public Sale getSaleById(Long id) {
        return saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with ID: " + id));
    }

    @Transactional
    public Sale updateSale(Long id, SaleDTO saleDTO) {
        Sale existingSale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with ID: " + id));

        for (SaleDetail detail : existingSale.getDetails()) {
            Product product = detail.getProduct();
            product.setStock(product.getStock() + detail.getQuantity());
            productRepository.save(product);
        }

        existingSale.getDetails().clear();

        List<SaleDetail> updatedDetails = new ArrayList<>();
        double newTotal = 0.0;

        for (SaleDetailDTO detailDTO : saleDTO.getItems()) {
            Product product = productRepository.findById(detailDTO.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < detailDTO.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getName());
            }

            product.setStock(product.getStock() - detailDTO.getQuantity());
            productRepository.save(product);

            SaleDetail newDetail = new SaleDetail();
            newDetail.setProduct(product);
            newDetail.setQuantity(detailDTO.getQuantity());
            newDetail.setSale(existingSale);

            updatedDetails.add(newDetail);
            newTotal += product.getSalePrice() * detailDTO.getQuantity();
        }

        existingSale.getDetails().addAll(updatedDetails);

        existingSale.setAmount(newTotal);
        existingSale.setPaymentMethod(saleDTO.getPaymentMethod());
        existingSale.setDate(LocalDate.now());
        existingSale.setTime(LocalTime.now());

        return saleRepository.save(existingSale);

    }

    @Transactional
    public void deleteSale(Long id) {
        Sale sale = saleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sale not found with ID: " + id));

        for (SaleDetail detail : sale.getDetails()) {
            Product product = detail.getProduct();
            product.setStock(product.getStock() + detail.getQuantity());
            productRepository.save(product);
        }

        saleRepository.delete(sale);
    }
}
