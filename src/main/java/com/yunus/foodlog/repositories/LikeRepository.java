package com.yunus.foodlog.repositories;

import com.yunus.foodlog.entities.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
    List<Like> findByUserIdAndPostId(Long userId, Long postId);

    List<Like> findByUserId(Long userId);

    List<Like> findByPostId(Long postId);

    @Query(value = "SELECT 'liked', l.post_id, u.avatar, u.user_name FROM p_like l LEFT JOIN foodlog_user u ON u.id=l.user_id WHERE l.post_id IN :postIds LIMIT 5", nativeQuery = true)
    List<Object> findUserLikesByPostId(@Param("postIds") List<Long> postIds);
}
