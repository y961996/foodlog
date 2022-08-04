package com.yunus.foodlog.services;

import com.yunus.foodlog.repositories.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
}
