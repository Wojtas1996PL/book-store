package mate.academy.service.repository.book.spec;

import java.util.Arrays;
import mate.academy.model.Book;
import mate.academy.service.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class TitleSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "title";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query,
                criteriaBuilder) -> root.get("title").in(Arrays.stream(params).toArray());
    }
}
