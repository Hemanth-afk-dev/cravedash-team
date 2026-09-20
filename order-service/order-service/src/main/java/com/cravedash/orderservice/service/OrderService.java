package com.cravedash.orderservice.service;

import com.cravedash.orderservice.dto.OrderItemDTO;
import com.cravedash.orderservice.dto.OrderRequestDTO;
import com.cravedash.orderservice.dto.OrderResponseDTO;
import com.cravedash.orderservice.dto.PaymentRequestDTO;
import com.cravedash.orderservice.dto.PaymentResponseDTO;
import com.cravedash.orderservice.dto.RestaurantAvailabilityDTO;
import com.cravedash.orderservice.model.Order;
import com.cravedash.orderservice.model.OrderItem;
import com.cravedash.orderservice.repository.OrderRepository;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RestTemplate restTemplate;

    public OrderService(OrderRepository orderRepository,
                        RestTemplate restTemplate) {

        this.orderRepository = orderRepository;
        this.restTemplate = restTemplate;
    }

    public OrderResponseDTO createOrder(OrderRequestDTO request) {

        // 1. Check restaurant availability using Eureka + LoadBalancer
        String restaurantUrl =
                "http://RESTAURANT-SERVICE/restaurants/"
                        + request.getRestaurantId()
                        + "/availability";

        RestaurantAvailabilityDTO availabilityResponse =
                restTemplate.getForObject(
                        restaurantUrl,
                        RestaurantAvailabilityDTO.class
                );

        boolean available =
                availabilityResponse != null
                        && availabilityResponse.isAvailable();

        if (!available) {
            throw new RuntimeException("Restaurant is not available");
        }

        // 2. Create order
        Order order = new Order();

        order.setUserId(request.getUserId());
        order.setRestaurantId(request.getRestaurantId());
        order.setStatus("PLACED");
        order.setCreatedAt(LocalDateTime.now());

        double total = 0;

        for (OrderItemDTO dto : request.getItems()) {

            OrderItem item = new OrderItem();

            item.setMenuItemId(dto.getMenuItemId());
            item.setItemName(dto.getItemName());
            item.setQuantity(dto.getQuantity());
            item.setPrice(dto.getPrice());

            order.getItems().add(item);

            total += dto.getPrice() * dto.getQuantity();
        }

        order.setTotalAmount(total);

        // 3. Save order
        Order savedOrder = orderRepository.save(order);

        // 4. Create payment request
        PaymentRequestDTO paymentRequest = new PaymentRequestDTO();

        paymentRequest.setOrderId(savedOrder.getId());
        paymentRequest.setUserId(savedOrder.getUserId());
        paymentRequest.setAmount(savedOrder.getTotalAmount());
        paymentRequest.setPaymentMethod(request.getPaymentMethod());

        // 5. Call Payment Service using Eureka + LoadBalancer
        String paymentUrl = "http://PAYMENT-SERVICE/payments";

        PaymentResponseDTO paymentResponse =
                restTemplate.postForObject(
                        paymentUrl,
                        paymentRequest,
                        PaymentResponseDTO.class
                );

        // 6. Update order status
        if (paymentResponse != null
                && "SUCCESS".equals(paymentResponse.getStatus())) {

            savedOrder.setStatus("CONFIRMED");

        } else {

            savedOrder.setStatus("PAYMENT_FAILED");
        }

        // 7. Save updated order
        savedOrder = orderRepository.save(savedOrder);

        return convertToResponse(savedOrder);
    }

    public OrderResponseDTO getOrderById(Long id) {

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return convertToResponse(order);
    }

    public List<Order> getOrdersByUser(Long userId) {

        return orderRepository.findByUserId(userId);
    }

    private OrderResponseDTO convertToResponse(Order order) {

        OrderResponseDTO response = new OrderResponseDTO();

        response.setOrderId(order.getId());
        response.setUserId(order.getUserId());
        response.setRestaurantId(order.getRestaurantId());
        response.setTotalAmount(order.getTotalAmount());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());

        return response;
    }
}