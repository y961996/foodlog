package com.yunus.foodlog.services;

import com.yunus.foodlog.repositories.LikeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
}
