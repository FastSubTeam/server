package com.popple.server.domain.user.repository;

import com.popple.server.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByIdIn(List<Long> ids);
}
