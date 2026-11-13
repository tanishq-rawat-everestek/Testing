package ai.agilery.coupon.controller;

import ai.agilery.coupon.dto.CouponValidationRequestDto;
import ai.agilery.coupon.dto.RedemptionDto;
import ai.agilery.coupon.service.RedemptionService;
import ai.agilery.coupon.validation.impl.service.RedemptionValidationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Redemption management operations.
 * Provides endpoints for recording redemptions and retrieving redemption history.
 */
@RestController
@RequestMapping("/api/v1/redemptions")
@RequiredArgsConstructor
@Tag(name = "Redemption API", description = "Endpoints for managing coupon redemptions and history tracking")
public class RedemptionController {

    private final RedemptionService redemptionService;
    private final RedemptionValidationService redemptionValidationService;

    @PostMapping
    @Operation(summary = "Record a coupon redemption",
            description = "Records a coupon redemption attempt for an order. Validates the coupon and creates a redemption record.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Redemption recorded successfully",
                    content = @Content(schema = @Schema(implementation = RedemptionDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request or validation error"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ResponseEntity<RedemptionDto> recordRedemption(
            @Valid @RequestBody CouponValidationRequestDto request) {
        redemptionValidationService.onCreate(request);
        RedemptionDto redemption = redemptionService.recordRedemption(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(redemption);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get redemption by ID", description = "Retrieves a redemption record by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Redemption retrieved successfully",
                    content = @Content(schema = @Schema(implementation = RedemptionDto.class))),
            @ApiResponse(responseCode = "404", description = "Redemption not found")
    })
    public ResponseEntity<RedemptionDto> getRedemptionById(@PathVariable UUID id) {
        RedemptionDto redemption = redemptionService.getById(id);
        return ResponseEntity.ok(redemption);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(summary = "Get redemptions by customer",
            description = "Retrieves all redemption records for a specific customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Redemptions retrieved successfully")
    })
    public ResponseEntity<List<RedemptionDto>> getRedemptionsByCustomerId(@PathVariable UUID customerId) {
        List<RedemptionDto> redemptions = redemptionService.getByCustomerId(customerId);
        return ResponseEntity.ok(redemptions);
    }

    @GetMapping("/coupon/{couponId}")
    @Operation(summary = "Get redemptions by coupon",
            description = "Retrieves all redemption records for a specific coupon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Redemptions retrieved successfully")
    })
    public ResponseEntity<List<RedemptionDto>> getRedemptionsByCouponId(@PathVariable UUID couponId) {
        List<RedemptionDto> redemptions = redemptionService.getByCouponId(couponId);
        return ResponseEntity.ok(redemptions);
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get redemptions by order",
            description = "Retrieves all redemption records for a specific order")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Redemptions retrieved successfully")
    })
    public ResponseEntity<List<RedemptionDto>> getRedemptionsByOrderId(@PathVariable UUID orderId) {
        List<RedemptionDto> redemptions = redemptionService.getByOrderId(orderId);
        return ResponseEntity.ok(redemptions);
    }

    @GetMapping
    @Operation(summary = "Get all redemptions", description = "Retrieves a list of all redemption records")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Redemptions retrieved successfully")
    })
    public ResponseEntity<List<RedemptionDto>> getAllRedemptions() {
        List<RedemptionDto> redemptions = redemptionService.getAll();
        return ResponseEntity.ok(redemptions);
    }
}
