package mate.academy.service.repository.category;

import java.util.Optional;
import mate.academy.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long>,
        JpaSpecificationExecutor<Category> {
    @Query(value = "SELECT id, name, description, is_deleted "
            + "FROM categories WHERE id=:id", nativeQuery = true)
    Optional<Category> findCategoryById(Long id);
}
