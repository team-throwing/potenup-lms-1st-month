package com.lms.dto;

import com.lms.domain.category.CategoryLevel;

public class CategoryRequestDto {
    private Integer id;                // Optional: 수정할 때 사용
    private String name;            // 필수
    private CategoryLevel level;    // 필수
    private Integer parentId;       // 선택적

    public CategoryRequestDto() {}

    public CategoryRequestDto(Integer id, String name, CategoryLevel level, Integer parentId) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.parentId = parentId;
    }

    // getter / setter
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public CategoryLevel getLevel() { return level; }
    public void setLevel(CategoryLevel level) { this.level = level; }

    public Integer getParentId() { return parentId; }
    public void setParentId(Integer parentId) { this.parentId = parentId; }
}
