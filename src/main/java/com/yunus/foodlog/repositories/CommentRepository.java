package com.yunus.foodlog.repositories;

import com.yunus.foodlog.entities.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserIdAndPostId(Long userId, Long postId);

    List<Comment> findByUserId(Long userId);

    List<Comment> findByPostId(Long postId);

    @Query(value = "SELECT 'commented_on', c.post_id, u.avatar, u.user_name FROM p_comment c LEFT JOIN foodlog_user u ON u.id=c.user_id WHERE c.post_id IN :postIds LIMIT 5", nativeQuery = true)
    List<Object> findUserCommentsByPostId(@Param("postIds") List<Long> postIds);
}
