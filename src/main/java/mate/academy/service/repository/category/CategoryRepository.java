package mate.academy.service.repository.category;

import mate.academy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>,
        JpaSpecificationExecutor<Category> {
    @Query(value = "SELECT id, name, description "
            + "FROM categories WHERE id=:id",
            nativeQuery = true)
    Category findCategoryById(Long id);
}
