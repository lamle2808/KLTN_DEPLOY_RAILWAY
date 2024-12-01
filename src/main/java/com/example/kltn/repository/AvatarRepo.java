package com.example.kltn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kltn.entity.Avatar;

@Repository
public interface AvatarRepo extends JpaRepository<Avatar,Long> {
    Avatar findAvatarById(Long id);
}
