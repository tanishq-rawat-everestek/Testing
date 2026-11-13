package ai.agilery.coupon.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

/**
 * Configuration class for OpenAPI (Swagger) documentation setup.
 *
 * <p>
 * This class configures the Swagger UI and OpenAPI metadata, and registers the
 * Bearer token authentication scheme so that secured APIs are properly
 * displayed in Swagger.
 * </p>
 *
 * <p>
 * Note: This only configures the API documentation. Actual authentication and
 * authorization logic is handled by Spring Security.
 * </p>
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "${spring.application.name}",
                version = "${spring.application.version}",
                contact = @Contact(
                        name = "Agilery", email = "contacts@agilery.ai", url = "https://www.agilery.ai"
                        ),

                description = "The Coupon Management Service provides REST APIs to create, manage, and validate promotional discount coupons for e-commerce orders. It supports percentage-based and fixed-amount discounts with configurable rules including expiry dates, minimum order values, maximum discount caps, and usage limits per customer and overall. The service calculates final discounts and payable amounts while maintaining a complete redemption history for tracking and auditing coupon usage across customers and orders."
                        + "It provides basic swagger html and API endpoints for information and health check."
                ),
        security = {
                @SecurityRequirement(name = "BearerAuth")
        }
        )
@SecuritySchemes({
    @SecurityScheme(
            name = "BearerAuth",
            type = SecuritySchemeType.HTTP,
            scheme = "bearer",
            bearerFormat = "JWT"
            )
})
public class OpenApiConfig {

}
