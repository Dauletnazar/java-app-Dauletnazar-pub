package softserve.academy.simplecrud;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import softserve.academy.simplecrud.exception.ProductNotFoundException;
import softserve.academy.simplecrud.model.entity.Product;
import softserve.academy.simplecrud.model.request.PatchProduct;
import softserve.academy.simplecrud.repository.ProductRepository;
import softserve.academy.simplecrud.service.ProductService;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SimplecrudApplicationTests {

    @MockBean
    ProductRepository repo;

    @Autowired
    ProductService service;

    @Test
    void findById_ok() {
        Product p = Product.builder().name("a").price(new BigDecimal("1.00")).build();
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        Product got = service.findById("1");

        assertSame(p, got);
        verify(repo).findById(1L);
    }

    @Test
    void findById_badId_throws() {
        assertThrows(ProductNotFoundException.class, () -> service.findById("abc"));
        verifyNoInteractions(repo);
    }

    @Test
    void findById_notFound_throws() {
        when(repo.findById(9L)).thenReturn(Optional.empty());
        assertThrows(ProductNotFoundException.class, () -> service.findById("9"));
    }

    @Test
    void patch_updatesFields() {
        Product p = Product.builder().name("old").price(new BigDecimal("1.00")).build();
        when(repo.findById(1L)).thenReturn(Optional.of(p));

        PatchProduct patch = new PatchProduct("new", new BigDecimal("2.50"));
        service.patch(patch, "1");

        assertEquals("new", p.getName());
        assertEquals(new BigDecimal("2.50"), p.getPrice());
    }
}
