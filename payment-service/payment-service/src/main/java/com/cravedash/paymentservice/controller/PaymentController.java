package com.cravedash.paymentservice.controller;

import com.cravedash.paymentservice.dto.PaymentRequestDTO;
import com.cravedash.paymentservice.dto.PaymentResponseDTO;
import com.cravedash.paymentservice.service.PaymentService;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentResponseDTO processPayment(
            @RequestBody PaymentRequestDTO request) {

        return paymentService.processPayment(request);
    }

    @GetMapping("/{id}")
    public PaymentResponseDTO getPayment(
            @PathVariable Long id) {

        return paymentService.getPaymentById(id);
    }

    @GetMapping("/order/{orderId}")
    public List<PaymentResponseDTO> getPaymentsByOrder(
            @PathVariable Long orderId) {

        return paymentService.getPaymentsByOrder(orderId);
    }

    @GetMapping("/user/{userId}")
    public List<PaymentResponseDTO> getPaymentsByUser(
            @PathVariable Long userId) {

        return paymentService.getPaymentsByUser(userId);
    }
}