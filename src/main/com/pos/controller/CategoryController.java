package main.com.pos.controller;

import java.util.List;
import main.com.pos.model.Category;
import main.com.pos.service.CategoryService;

public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController() {
        this.categoryService = new CategoryService();
    }

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    public Category getCategoryById(int id) {
        return categoryService.getCategoryById(id);
    }

    public Category getCategoryByName(String name) {
        return categoryService.getCategoryByName(name);
    }

    public boolean addCategory(Category category) {
        return categoryService.addCategory(category);
    }

    public boolean addCategory(String name) {
        return categoryService.addCategory(name);
    }

    public boolean updateCategory(Category category) {
        return categoryService.updateCategory(category);
    }

    public boolean deleteCategory(int id) {
        return categoryService.deleteCategory(id);
    }

    public boolean categoryExists(String name) {
        return categoryService.categoryExists(name);
    }

    public int getProductCount(int categoryId) {
        return categoryService.getProductCount(categoryId);
    }

    public int getTotalCategoryCount() {
        return getAllCategories().size();
    }

    public boolean canDeleteCategory(int categoryId) {
        return categoryService.canDeleteCategory(categoryId);
    }
}
