package com.cravedash.restaurantservice.service;

import com.cravedash.restaurantservice.dto.MenuItemDTO;
import com.cravedash.restaurantservice.dto.RestaurantAvailabilityDTO;
import com.cravedash.restaurantservice.dto.RestaurantResponseDTO;
import com.cravedash.restaurantservice.model.MenuItem;
import com.cravedash.restaurantservice.model.Restaurant;
import com.cravedash.restaurantservice.repository.MenuItemRepository;
import com.cravedash.restaurantservice.repository.RestaurantRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final MenuItemRepository menuItemRepository;

    public RestaurantService(RestaurantRepository restaurantRepository,
                             MenuItemRepository menuItemRepository) {
        this.restaurantRepository = restaurantRepository;
        this.menuItemRepository = menuItemRepository;
    }

    public List<RestaurantResponseDTO> getAllRestaurants() {

        return restaurantRepository.findAll()
                .stream()
                .map(this::convertToRestaurantDTO)
                .toList();
    }

    public RestaurantResponseDTO getRestaurantById(Long id) {

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        return convertToRestaurantDTO(restaurant);
    }

    public RestaurantResponseDTO createRestaurant(Restaurant restaurant) {

        Restaurant savedRestaurant = restaurantRepository.save(restaurant);

        return convertToRestaurantDTO(savedRestaurant);
    }

    public List<MenuItemDTO> getMenuByRestaurant(Long restaurantId) {

        return menuItemRepository.findByRestaurantId(restaurantId)
                .stream()
                .map(this::convertToMenuItemDTO)
                .toList();
    }

    public MenuItemDTO addMenuItem(Long restaurantId, MenuItem menuItem) {

        menuItem.setRestaurantId(restaurantId);

        MenuItem savedMenuItem = menuItemRepository.save(menuItem);

        return convertToMenuItemDTO(savedMenuItem);
    }

    public RestaurantAvailabilityDTO checkAvailability(Long restaurantId) {

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RuntimeException("Restaurant not found"));

        RestaurantAvailabilityDTO dto = new RestaurantAvailabilityDTO();

        dto.setRestaurantId(restaurant.getId());
        dto.setAvailable(restaurant.isAvailable());

        return dto;
    }

    private RestaurantResponseDTO convertToRestaurantDTO(Restaurant restaurant) {

        RestaurantResponseDTO dto = new RestaurantResponseDTO();

        dto.setId(restaurant.getId());
        dto.setName(restaurant.getName());
        dto.setAddress(restaurant.getAddress());
        dto.setPhone(restaurant.getPhone());
        dto.setAvailable(restaurant.isAvailable());

        return dto;
    }

    private MenuItemDTO convertToMenuItemDTO(MenuItem menuItem) {

        MenuItemDTO dto = new MenuItemDTO();

        dto.setId(menuItem.getId());
        dto.setRestaurantId(menuItem.getRestaurantId());
        dto.setName(menuItem.getName());
        dto.setDescription(menuItem.getDescription());
        dto.setPrice(menuItem.getPrice());
        dto.setAvailable(menuItem.isAvailable());

        return dto;
    }
}