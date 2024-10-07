package com.example.orderservice.adapters.rest;

import com.example.orderservice.application.dto.OrderDTO;
import com.example.orderservice.application.mapper.OrderMapper;
import com.example.orderservice.domain.Order;
import com.example.orderservice.domain.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.Optional;

@RestController
@RequestMapping("/api/v2/orders")
@Validated // Enable validation for input
public class OrderControllerV2 {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final OrderService orderService;
    private final OrderMapper orderMapper = OrderMapper.INSTANCE;

    public OrderControllerV2(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Create a new order.
     *
     * @param order the order to be created
     * @return the created order as DTO
     */
    @Operation(summary = "Create a new order", description = "Creates a new order and returns the details of the created order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    //@PreAuthorize("hasRole('USER')")
    public Mono<OrderDTO> createOrder(@Valid @RequestBody Order order) {
        logger.info("Creating a new order: {}", order);
        return Optional.ofNullable(order) // Java 8 Optional to handle potential null input
                .map(o -> Mono.just(o)
                        .flatMap(orderService::createOrder)
                        .map(orderMapper::orderToOrderDTO)
                        .doOnSuccess(createdOrder -> logger.info("Order created successfully: {}", createdOrder))
                        .doOnError(error -> logger.error("Failed to create order", error)))
                .orElseThrow(() -> new IllegalArgumentException("Order cannot be null"));
    }

    /**
     * Get an order by ID.
     *
     * @param id the order ID
     * @return the order details as DTO
     */
    @Operation(summary = "Get an order by ID", description = "Fetches the details of an order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order found",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping("/{id}")
   // @PreAuthorize("hasRole('USER')")
    public Mono<OrderDTO> getOrderById(@PathVariable String id) {
        logger.info("Fetching order by ID: {}", id);
        return orderService.getOrderById(id)
                .map(orderMapper::orderToOrderDTO)
                .doOnSuccess(order -> logger.info("Fetched order: {}", order))
                .doOnError(error -> logger.error("Failed to fetch order with ID: {}", id, error));
    }

    /**
     * Get all orders.
     *
     * @return a list of all orders as DTOs
     */
    @Operation(summary = "Get all orders", description = "Returns a list of all orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders fetched",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @GetMapping
    //@PreAuthorize("hasRole('ADMIN')")
    public Flux<OrderDTO> getAllOrders() {
        logger.info("Fetching all orders");
        return orderService.getAllOrders()
                .map(orderMapper::orderToOrderDTO)
                .doOnComplete(() -> logger.info("Fetched all orders successfully"))
                .doOnError(error -> logger.error("Failed to fetch orders", error));
    }

    /**
     * Delete an order by ID.
     *
     * @param id the ID of the order to be deleted
     * @return a confirmation message
     */
    @Operation(summary = "Delete an order by ID", description = "Deletes an order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order deleted", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    //@PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> deleteOrderById(@PathVariable String id) {
        logger.info("Deleting order with ID: {}", id);
        return orderService.getOrderById(id)
                .doOnSuccess(unused -> logger.info("Order deleted successfully: {}", id))
                .doOnError(error -> logger.error("Failed to delete order with ID: {}", id, error)).then();
    }

    /**
     * Update an existing order by ID.
     *
     * @param id the ID of the order to be updated
     * @param order the updated order data
     * @return the updated order as DTO
     */
    @Operation(summary = "Update an order", description = "Updates an existing order by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order updated",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrderDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input", content = @Content),
            @ApiResponse(responseCode = "404", description = "Order not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    @PutMapping("/{id}")
    //@PreAuthorize("hasRole('USER')")
    public Mono<OrderDTO> updateOrder(@PathVariable String id, @Valid @RequestBody Order order) {
        logger.info("Updating order with ID: {}", id);
        return orderService.updateOrder(id, order)
                .map(orderMapper::orderToOrderDTO)
                .doOnSuccess(updatedOrder -> logger.info("Order updated successfully: {}", updatedOrder))
                .doOnError(error -> logger.error("Failed to update order with ID: {}", id, error));
    }

    /**
     * Handle validation exceptions and return a meaningful message.
     *
     * @param ex the exception
     * @return error response
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessage = Optional.ofNullable(ex.getBindingResult().getFieldError())
                .map(err -> err.getDefaultMessage())
                .orElse("Invalid input");
        logger.error("Validation failed: {}", errorMessage);
        return Mono.just("Validation error: " + errorMessage);
    }

    /**
     * Handle general exceptions.
     *
     * @param ex the exception
     * @return error response
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Mono<String> handleGeneralExceptions(Exception ex) {
        logger.error("An error occurred: {}", ex.getMessage());
        return Mono.just("An error occurred: " + ex.getMessage());
    }
}

