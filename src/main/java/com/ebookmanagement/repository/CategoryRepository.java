package com.ebookmanagement.repository;

import com.ebookmanagement.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** Data access for Category entities. */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByCategoryName(String categoryName);

    boolean existsByCategoryName(String categoryName);
}
