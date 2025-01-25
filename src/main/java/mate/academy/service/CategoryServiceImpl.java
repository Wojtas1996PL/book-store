package mate.academy.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.dto.category.CategoryDto;
import mate.academy.exception.EntityNotFoundException;
import mate.academy.mapper.CategoryMapper;
import mate.academy.service.repository.category.CategoryRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Override
    public List<CategoryDto> findAll() {
        return categoryRepository
                .findAll()
                .stream()
                .map(categoryMapper::toDto)
                .toList();
    }

    @Override
    public CategoryDto getById(Long id) {
        return categoryMapper
                .toDto(categoryRepository
                .findCategoryById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException("Could not get category by id: " + id)));
    }

    @Override
    public CategoryDto save(CategoryDto categoryDto) {
        categoryRepository.save(categoryMapper.toEntity(categoryDto));
        return categoryDto;
    }

    @Override
    public CategoryDto update(Long id, CategoryDto categoryDto) {
        categoryRepository.findCategoryById(id)
                .map(c -> {
                    c.setId(categoryDto.getId());
                    c.setName(categoryDto.getName());
                    c.setDescription(categoryDto.getDescription());
                    return categoryRepository.save(categoryMapper.toEntity(categoryDto));
                })
                .orElseThrow(() -> new RuntimeException("Could not update category by id: " + id));
        return categoryDto;
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }
}
