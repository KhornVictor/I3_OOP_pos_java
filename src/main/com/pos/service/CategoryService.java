package main.com.pos.service;

import java.util.List;
import main.com.pos.dao.CategoryDAO;
import main.com.pos.model.Category;

public class CategoryService {
    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    public CategoryService(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = categoryDAO.getAll();
        if (categories.isEmpty()) {
            System.out.println("⚠️ No categories found");
        }
        return categories;
    }

    public Category getCategoryById(int id) {
        if (id <= 0) {
            System.out.println("❌ Invalid category ID");
            return null;
        }
        Category category = categoryDAO.getById(id);
        if (category == null) {
            System.out.println("⚠️ Category not found with ID: " + id);
        }
        return category;
    }

    public Category getCategoryByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("❌ Category name cannot be empty");
            return null;
        }
        Category category = categoryDAO.getByName(name);
        if (category == null) {
            System.out.println("⚠️ Category not found with name: " + name);
        }
        return category;
    }

    public boolean addCategory(Category category) {
        if (category == null) {
            System.out.println("❌ Category cannot be null");
            return false;
        }
        try {
            if (category.getName() == null || category.getName().trim().isEmpty()) {
                System.out.println("❌ Category name cannot be empty");
                return false;
            }
            
            // Check if category already exists
            if (categoryDAO.exists(category.getName())) {
                System.out.println("❌ Category already exists: " + category.getName());
                return false;
            }
            
            return categoryDAO.create(category);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Validation error: " + e.getMessage());
            return false;
        }
    }

    public boolean addCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            System.out.println("❌ Category name cannot be empty");
            return false;
        }
        Category category = new Category(0, name);
        return addCategory(category);
    }

    public boolean updateCategory(Category category) {
        if (category == null) {
            System.out.println("❌ Category cannot be null");
            return false;
        }
        if (category.getCategoryId() <= 0) {
            System.out.println("❌ Invalid category ID");
            return false;
        }
        
        // Check if category exists
        Category existingCategory = categoryDAO.getById(category.getCategoryId());
        if (existingCategory == null) {
            System.out.println("❌ Category not found with ID: " + category.getCategoryId());
            return false;
        }
        
        // Check if new name already exists (for a different category)
        Category nameCheck = categoryDAO.getByName(category.getName());
        if (nameCheck != null && nameCheck.getCategoryId() != category.getCategoryId()) {
            System.out.println("❌ Category name already exists: " + category.getName());
            return false;
        }
        
        try {
            return categoryDAO.update(category);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Validation error: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteCategory(int id) {
        if (id <= 0) {
            System.out.println("❌ Invalid category ID");
            return false;
        }
        
        // Check if category exists
        Category category = categoryDAO.getById(id);
        if (category == null) {
            System.out.println("❌ Category not found with ID: " + id);
            return false;
        }
        
        // Check if category has products
        int productCount = categoryDAO.getProductCount(id);
        if (productCount > 0) {
            System.out.println("❌ Cannot delete category with " + productCount + " product(s)");
            return false;
        }
        
        return categoryDAO.delete(id);
    }

    public boolean categoryExists(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return categoryDAO.exists(name);
    }

    public int getProductCount(int categoryId) {
        if (categoryId <= 0) {
            return 0;
        }
        return categoryDAO.getProductCount(categoryId);
    }

    public boolean canDeleteCategory(int categoryId) {
        if (categoryId <= 0) {
            return false;
        }
        return categoryDAO.getProductCount(categoryId) == 0;
    }
}
