package com.snorlax.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.snorlax.modal.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{

	Category findByCategoryId(String categoryId);

}
