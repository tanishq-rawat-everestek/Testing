package ai.agilery.coupon.controller;

import ai.agilery.coupon.dto.CouponDto;
import ai.agilery.coupon.dto.CouponValidationRequestDto;
import ai.agilery.coupon.dto.CouponValidationResponseDto;
import ai.agilery.coupon.service.CouponService;
import ai.agilery.coupon.validation.OnCreate;
import ai.agilery.coupon.validation.OnPatch;
import ai.agilery.coupon.validation.OnUpdate;
import ai.agilery.coupon.validation.impl.service.CouponValidationService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST controller for Coupon management operations.
 * Provides endpoints for CRUD operations, validation, and discount calculation.
 */
@RestController
@RequestMapping("/api/v1/coupons")
@RequiredArgsConstructor
@Tag(name = "Coupon API", description = "Endpoints for managing promotional coupons and discount calculations")
public class CouponController {

    private final CouponService couponService;
    private final CouponValidationService couponValidationService;

    @PostMapping
    @Operation(summary = "Create a new coupon", description = "Creates a new promotional coupon with discount rules and usage restrictions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Coupon created successfully",
                    content = @Content(schema = @Schema(implementation = CouponDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
            @ApiResponse(responseCode = "409", description = "Coupon code already exists")
    })
    public ResponseEntity<CouponDto> createCoupon(
            @Validated(OnCreate.class) @RequestBody CouponDto couponDto) {
        couponValidationService.onCreate(couponDto);
        CouponDto createdCoupon = couponService.create(couponDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCoupon);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing coupon", description = "Updates all fields of an existing coupon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon updated successfully",
                    content = @Content(schema = @Schema(implementation = CouponDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ResponseEntity<CouponDto> updateCoupon(
            @PathVariable UUID id,
            @Validated(OnUpdate.class) @RequestBody CouponDto couponDto) {
        couponDto.setId(id);
        couponValidationService.onUpdate(couponDto);
        CouponDto updatedCoupon = couponService.update(id, couponDto);
        return ResponseEntity.ok(updatedCoupon);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Partially update a coupon", description = "Updates only the provided fields of an existing coupon")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon patched successfully",
                    content = @Content(schema = @Schema(implementation = CouponDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ResponseEntity<CouponDto> patchCoupon(
            @PathVariable UUID id,
            @Validated(OnPatch.class) @RequestBody CouponDto couponDto) {
        couponValidationService.onPatch(couponDto);
        CouponDto patchedCoupon = couponService.patch(id, couponDto);
        return ResponseEntity.ok(patchedCoupon);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get coupon by ID", description = "Retrieves a coupon by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CouponDto.class))),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ResponseEntity<CouponDto> getCouponById(@PathVariable UUID id) {
        CouponDto coupon = couponService.getById(id);
        return ResponseEntity.ok(coupon);
    }

    @GetMapping("/code/{code}")
    @Operation(summary = "Get coupon by code", description = "Retrieves a coupon by its unique code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon retrieved successfully",
                    content = @Content(schema = @Schema(implementation = CouponDto.class))),
            @ApiResponse(responseCode = "404", description = "Coupon not found")
    })
    public ResponseEntity<CouponDto> getCouponByCode(@PathVariable String code) {
        CouponDto coupon = couponService.getByCode(code);
        return ResponseEntity.ok(coupon);
    }

    @GetMapping
    @Operation(summary = "Get all coupons", description = "Retrieves a list of all coupons")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupons retrieved successfully")
    })
    public ResponseEntity<List<CouponDto>> getAllCoupons() {
        List<CouponDto> coupons = couponService.getAll();
        return ResponseEntity.ok(coupons);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a coupon", description = "Deletes a coupon by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Coupon deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Coupon not found"),
            @ApiResponse(responseCode = "409", description = "Coupon has active redemptions and cannot be deleted safely")
    })
    public ResponseEntity<Void> deleteCoupon(@PathVariable UUID id) {
        couponValidationService.onDelete(id);
        couponService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/validate")
    @Operation(summary = "Validate coupon and calculate discount",
            description = "Validates a coupon code against an order and calculates the discount amount and final payable amount")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Coupon validated successfully",
                    content = @Content(schema = @Schema(implementation = CouponValidationResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request")
    })
    public ResponseEntity<CouponValidationResponseDto> validateCoupon(
            @Valid @RequestBody CouponValidationRequestDto request) {
        CouponValidationResponseDto response = couponService.validateAndCalculateDiscount(request);
        return ResponseEntity.ok(response);
    }
}
