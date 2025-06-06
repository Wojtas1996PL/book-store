package mate.academy.service.repository.book.spec;

import java.util.Arrays;
import mate.academy.model.Book;
import mate.academy.service.repository.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class AuthorSpecificationProvider implements SpecificationProvider<Book> {
    @Override
    public String getKey() {
        return "author";
    }

    @Override
    public Specification<Book> getSpecification(String[] params) {
        return (root, query,
                criteriaBuilder) -> root.get("author").in(Arrays.stream(params).toArray());
    }
}
