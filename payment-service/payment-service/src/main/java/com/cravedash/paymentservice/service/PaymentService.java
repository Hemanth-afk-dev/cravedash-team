package com.cravedash.paymentservice.service;

import com.cravedash.paymentservice.dto.PaymentRequestDTO;
import com.cravedash.paymentservice.dto.PaymentResponseDTO;
import com.cravedash.paymentservice.model.Payment;
import com.cravedash.paymentservice.repository.PaymentRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public PaymentResponseDTO processPayment(PaymentRequestDTO request) {

        Payment payment = new Payment();

        payment.setOrderId(request.getOrderId());
        payment.setUserId(request.getUserId());
        payment.setAmount(request.getAmount());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());

        Payment savedPayment = paymentRepository.save(payment);

        return convertToResponse(savedPayment);
    }

    public PaymentResponseDTO getPaymentById(Long id) {

        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return convertToResponse(payment);
    }

    public List<PaymentResponseDTO> getPaymentsByOrder(Long orderId) {

        return paymentRepository.findByOrderId(orderId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public List<PaymentResponseDTO> getPaymentsByUser(Long userId) {

        return paymentRepository.findByUserId(userId)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    private PaymentResponseDTO convertToResponse(Payment payment) {

        PaymentResponseDTO response = new PaymentResponseDTO();

        response.setPaymentId(payment.getId());
        response.setOrderId(payment.getOrderId());
        response.setUserId(payment.getUserId());
        response.setAmount(payment.getAmount());
        response.setPaymentMethod(payment.getPaymentMethod());
        response.setStatus(payment.getStatus());
        response.setPaymentDate(payment.getPaymentDate());

        return response;
    }
}