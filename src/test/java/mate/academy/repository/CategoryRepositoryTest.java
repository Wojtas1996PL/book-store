package mate.academy.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import mate.academy.exception.EntityNotFoundException;
import mate.academy.model.Category;
import mate.academy.service.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase
public class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @DisplayName("Verify that method findCategoryById works")
    public void findCategoryById_CorrectCategory_ReturnsOptionalOfCategory() {
        long id = 1L;

        Category scienceFictionExpected = new Category();
        scienceFictionExpected.setId(id);
        scienceFictionExpected.setName("Science fiction");
        scienceFictionExpected.setDescription("Science fiction books");
        categoryRepository.save(scienceFictionExpected);

        Category scienceFictionActual = categoryRepository.findCategoryById(id).orElseThrow(()
                -> new EntityNotFoundException("Category does not exist"));

        assertThat(scienceFictionActual).isEqualTo(scienceFictionExpected);
        assertNotNull(scienceFictionActual);
    }
}
