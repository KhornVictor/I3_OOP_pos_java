package main.com.pos.model;

public class Category {
    private int categoryId;
    private String name;

    public Category() {}

    public Category(int categoryId, String name) {
        this.categoryId = categoryId;
        setName(name);
    }

    public int getCategoryId() { return categoryId; }
    public String getName() { return name; }
    
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
    public final void setName(String name) { 
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Category name cannot be null or empty");
        }
        this.name = name; 
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId=" + categoryId +
                ", name='" + name + '\'' +
                '}';
    }
}
