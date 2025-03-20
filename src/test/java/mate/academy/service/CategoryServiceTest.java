package mate.academy.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import mate.academy.dto.category.CategoryDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CategoryMapper;
import mate.academy.model.Category;
import mate.academy.service.repository.category.CategoryRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @Test
    @DisplayName("Verify that method findAll works")
    public void findAll_CorrectCategories_ReturnsCategoryDtoList() {
        Category scienceFiction = new Category();
        scienceFiction.setId(1L);
        scienceFiction.setName("Science fiction");
        scienceFiction.setDescription("Science fiction books");

        CategoryDto scienceFictionDto = new CategoryDto();
        scienceFictionDto.setId(1L);
        scienceFictionDto.setName("Science fiction");
        scienceFictionDto.setDescription("Science fiction books");

        Category horror = new Category();
        horror.setId(2L);
        horror.setName("Horror");
        horror.setDescription("Horror books");

        CategoryDto horrorDto = new CategoryDto();
        horrorDto.setId(2L);
        horrorDto.setName("Horror");
        horrorDto.setDescription("Horror books");

        List<Category> categories = List.of(scienceFiction, horror);
        List<CategoryDto> categoriesDtoExpected = List.of(scienceFictionDto, horrorDto);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Category> categoryPage = new PageImpl<>(categories);

        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);
        when(categoryMapper.toDto(scienceFiction)).thenReturn(scienceFictionDto);
        when(categoryMapper.toDto(horror)).thenReturn(horrorDto);

        List<CategoryDto> categoriesDtoActual = categoryService.findAll(pageable);

        assertThat(categoriesDtoActual).isEqualTo(categoriesDtoExpected);
        assertNotNull(categoriesDtoActual);

        verify(categoryRepository, times(1)).findAll(pageable);
        verify(categoryMapper, times(1)).toDto(scienceFiction);
        verify(categoryMapper, times(1)).toDto(horror);
    }

    @Test
    @DisplayName("Verify that method getById works")
    public void getById_CorrectCategory_ReturnsCategoryDto() {
        long id = 1L;

        Category scienceFiction = new Category();
        scienceFiction.setId(id);
        scienceFiction.setName("Science fiction");
        scienceFiction.setDescription("Science fiction books");

        CategoryDto scienceFictionDtoExpected = new CategoryDto();
        scienceFictionDtoExpected.setId(id);
        scienceFictionDtoExpected.setName("Science fiction");
        scienceFictionDtoExpected.setDescription("Science fiction books");

        when(categoryRepository.findCategoryById(id)).thenReturn(Optional.of(scienceFiction));
        when(categoryMapper.toDto(scienceFiction)).thenReturn(scienceFictionDtoExpected);

        CategoryDto scienceFictionDtoActual = categoryService.getById(id);

        assertThat(scienceFictionDtoActual).isEqualTo(scienceFictionDtoExpected);
        assertThat(scienceFictionDtoActual).isNotNull();

        verify(categoryRepository, times(1)).findCategoryById(id);
        verify(categoryMapper, times(1)).toDto(scienceFiction);
    }

    @Test
    @DisplayName("Verify that method getById throws EntityNotFoundException")
    public void getById_CategoryWithIncorrectId_ThrowsEntityNotFoundException() {
        long id = 1L;

        assertThrows(EntityNotFoundException.class, () -> categoryService.getById(id));
    }

    @Test
    @DisplayName("Verify that method save works")
    public void save_CorrectCategory_ReturnsCategoryDto() {
        long id = 1L;

        Category scienceFiction = new Category();
        scienceFiction.setId(id);
        scienceFiction.setName("Science fiction");
        scienceFiction.setDescription("Science fiction books");

        CategoryDto scienceFictionDtoExpected = new CategoryDto();
        scienceFictionDtoExpected.setId(id);
        scienceFictionDtoExpected.setName("Science fiction");
        scienceFictionDtoExpected.setDescription("Science fiction books");

        when(categoryMapper.toEntity(scienceFictionDtoExpected)).thenReturn(scienceFiction);
        when(categoryMapper.toDto(scienceFiction)).thenReturn(scienceFictionDtoExpected);
        when(categoryRepository.save(scienceFiction)).thenReturn(scienceFiction);

        CategoryDto scienceFictionDtoActual = categoryService.save(scienceFictionDtoExpected);

        assertThat(scienceFictionDtoActual).isEqualTo(scienceFictionDtoExpected);

        verify(categoryMapper, times(1)).toEntity(scienceFictionDtoExpected);
        verify(categoryMapper, times(1)).toDto(scienceFiction);
        verify(categoryRepository, times(1)).save(scienceFiction);
    }

    @Test
    @DisplayName("Verify that method update works")
    public void update_CorrectCategory_ReturnsCategoryDto() {
        long id = 1L;

        Category scienceFiction = new Category();
        scienceFiction.setId(id);
        scienceFiction.setName("Science fiction");
        scienceFiction.setDescription("Science fiction books");

        CategoryDto scienceFictionDto = new CategoryDto();
        scienceFictionDto.setId(id);
        scienceFictionDto.setName("Science fiction");
        scienceFictionDto.setDescription("Science fiction books");

        Category horror = new Category();
        horror.setId(id);
        horror.setName("Horror");
        horror.setDescription("Horror books");

        CategoryDto horrorDtoExpected = new CategoryDto();
        horrorDtoExpected.setId(id);
        horrorDtoExpected.setName("Horror");
        horrorDtoExpected.setDescription("Horror books");

        when(categoryRepository.findCategoryById(id)).thenReturn(Optional.of(scienceFiction));
        when(categoryRepository.save(horror)).thenReturn(horror);
        when(categoryMapper.toDto(horror)).thenReturn(horrorDtoExpected);

        CategoryDto horrorDtoActual = categoryService.update(id, horrorDtoExpected);

        assertThat(horrorDtoActual).isEqualTo(horrorDtoExpected);
        assertThat(horrorDtoActual).isNotNull();

        verify(categoryRepository, times(1)).findCategoryById(id);
        verify(categoryRepository, times(1)).save(horror);
        verify(categoryMapper, times(1)).toDto(horror);
    }

    @Test
    @DisplayName("Verify that method update throws EntityNotFoundException")
    public void update_IncorrectId_ThrowsEntityNotFoundException() {
        long id = 1L;

        CategoryDto scienceFictionDto = new CategoryDto();
        scienceFictionDto.setId(id);
        scienceFictionDto.setName("Science fiction");
        scienceFictionDto.setDescription("Science fiction books");

        assertThrows(EntityNotFoundException.class,
                () -> categoryService.update(id, scienceFictionDto));
    }

    @Test
    @DisplayName("Verify that method deleteById works")
    public void deleteById_Category_ReturnsNothing() {
        long id = 1L;

        categoryService.deleteById(id);

        verify(categoryRepository, times(1)).deleteById(id);
    }
}
