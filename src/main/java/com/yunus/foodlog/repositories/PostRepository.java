package com.yunus.foodlog.repositories;

import com.yunus.foodlog.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserId(Long userId);

    @Query(value = "SELECT id FROM post WHERE user_id = :userId ORDER BY create_date DESC LIMIT 5", nativeQuery = true)
    List<Long> findTopByUserId(@Param("userId") Long userId);
}
