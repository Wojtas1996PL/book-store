package mate.academy.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.book.BookDtoWithoutCategoryIds;
import mate.academy.dto.category.CategoryDto;
import mate.academy.service.BookService;
import mate.academy.service.CategoryService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Category management")
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final BookService bookService;
    private final CategoryService categoryService;

    @Operation(summary = "Create category")
    @PostMapping("/api/categories")
    public CategoryDto createCategory(CategoryDto categoryDto) {
        return categoryService.save(categoryDto);
    }

    @Operation(summary = "Get list of all categories")
    @GetMapping("/api/categories")
    public List<CategoryDto> getAll() {
        return categoryService.findAll();
    }

    @Operation(summary = "Get category by id")
    @GetMapping("/api/categories/{id}")
    public CategoryDto getCategoryById(Long id) {
        return categoryService.getById(id);
    }

    @Operation(summary = "Update category by id")
    @PutMapping("/api/categories/{id}")
    public CategoryDto updateCategory(Long id, CategoryDto categoryDto) {
        return categoryService.update(id, categoryDto);
    }

    @Operation(summary = "Delete category by id")
    @DeleteMapping("/api/categories/{id}")
    public void deleteCategory(Long id) {
        categoryService.deleteById(id);
    }

    @Operation(summary = "Get list of books by specific category")
    @GetMapping("/api/categories/{id}/books")
    public List<BookDtoWithoutCategoryIds> getBooksByCategoryId(Long id) {
        return bookService.findAllByCategoryId(id);
    }
}
