package softserve.academy.simplecrud.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import softserve.academy.simplecrud.exception.ProductNotFoundException;
import softserve.academy.simplecrud.model.entity.Product;
import softserve.academy.simplecrud.model.request.NewProduct;
import softserve.academy.simplecrud.model.request.PatchProduct;
import softserve.academy.simplecrud.model.response.ProductCreated;
import softserve.academy.simplecrud.model.response.ProductDto;
import softserve.academy.simplecrud.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.util.StringUtils.hasText;

@RequiredArgsConstructor
@Service
public class ProductService {

    private final ProductRepository productRepository;

    private static long parseIdOrThrow(String rawId) {
        try {
            return Long.parseLong(rawId);
        } catch (NumberFormatException ex) {
            throw new ProductNotFoundException();
        }
    }

    /** Read-only */
    @Transactional(readOnly = true)
    public List<ProductDto> getAll() {
        return productRepository.findAll()
                .stream()
                .map(ProductDto::new)
                .toList();
    }

    @Transactional
    public ProductCreated create(NewProduct newProduct) {
        Product p = Product.builder()
                .name(newProduct.name())
                .price(newProduct.price())
                .build();
        p = productRepository.save(p);
        return new ProductCreated(p.getId().toString());
    }

    /** Пошук за id як публічне API (read-only). */
    @Transactional(readOnly = true)
    public Product findById(String id) {
        long productId = parseIdOrThrow(id);
        return productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
    }

    @Transactional
    public void patch(PatchProduct patchProduct, String id) {
        long productId = parseIdOrThrow(id);
        Product p = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);

        String newName = patchProduct.name();
        if (hasText(newName)) {
            p.setName(newName);
        }

        BigDecimal newPrice = patchProduct.price();
        if (newPrice != null) {
            p.setPrice(newPrice);
        }
    }

    @Transactional
    public void delete(String id) {
        long productId = parseIdOrThrow(id);
        Product p = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);
        productRepository.delete(p);
    }
}
