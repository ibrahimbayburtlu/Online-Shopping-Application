package com.ibrahimbayburtlu.orderservice.service;

import com.ibrahimbayburtlu.orderservice.dto.InventoryResponse;
import com.ibrahimbayburtlu.orderservice.dto.OrderLineItemsDto;
import com.ibrahimbayburtlu.orderservice.dto.OrderRequest;
import com.ibrahimbayburtlu.orderservice.model.Order;
import com.ibrahimbayburtlu.orderservice.model.OrderLineItems;
import com.ibrahimbayburtlu.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {


    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest){
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemList().stream()
                .map(OrderLineItems::getSkuCode)
                .toList();
        // Call Inventory Service, and place order if product is in
        // stock
        InventoryResponse[] inventoryResponsesArray = webClient.get()
                 .uri("http://localhost:8082/api/inventory",
                         uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                 .retrieve()
                 .bodyToMono(InventoryResponse[].class)
                 .block();

        assert inventoryResponsesArray != null;
        boolean allProductInStock = Arrays.stream(inventoryResponsesArray)
                                    .allMatch(InventoryResponse::isInStock);

        if (allProductInStock){
            orderRepository.save(order);
        }else {
            throw new IllegalArgumentException("Product is not in stock, please try again later.");
        }

    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
