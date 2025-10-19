package com.lms.domain.category;

import lombok.Getter;

@Getter
public class Category {
    private Integer id;
    private String name;
    private CategoryLevel level;
    private Integer parentId;

    public static Category create(
        String name, CategoryLevel level, Integer parentId
    ) {
        return new Category(null, name, level, parentId);
    }

    public static Category rebuild(
        Integer id, String name, CategoryLevel level, Integer parentId
    ) {
        return new Category(id, name, level, parentId);
    }

    public void rename(String newName) {
        validateName(newName);

        name = newName;
    }

    public void updateLevel(CategoryLevel level) {
        validateLevel(level, parentId);

        this.level = level;
    }

    public void updateParentId(Integer parentId) {
        validateParent(parentId);

        this.parentId = parentId;
    }

    private void validateLevel(CategoryLevel level, Integer parentId) {
        if (level == null) {
            throw new IllegalArgumentException("카테고리 레벨을 설정해주세요.");
        }

        if (level == CategoryLevel.ONE && parentId != null) {
            throw new IllegalArgumentException("상위 카테고리는 또 다른 상위 카테고리를 가질 수 없습니다.");
        }
    }

    private void validateParent(Integer parentId) {
        if (parentId == null) {
            throw new IllegalArgumentException("변경하려는 상위 카테고리 아이디를 입력해주세요.");
        }

        if (this.level == CategoryLevel.ONE) {
            throw new IllegalArgumentException("상위 카테고리는 또 다른 상위 카테고리를 가질 수 없습니다.");
        }
    }

    private void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("카테고리 이름을 작성해주세요");
        }
    }

    private Category(
        Integer id,
        String name,
        CategoryLevel level,
        Integer parentId
    ) {
        validateName(name);
        validateLevel(level, parentId);

        this.id = id;
        this.name = name;
        this.level = level;
        this.parentId = parentId;
    }
}
