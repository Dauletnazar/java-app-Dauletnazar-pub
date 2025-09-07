package softserve.academy.simplecrud.model.request;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record PatchProduct(
        String name,
        @Positive BigDecimal price
) {
}
