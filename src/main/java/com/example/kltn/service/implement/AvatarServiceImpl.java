package com.example.kltn.service.implement;

import com.example.kltn.entity.Avatar;
import com.example.kltn.repository.AvatarRepo;
import com.example.kltn.service.AvatarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.annotation.SessionScope;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@SessionScope
public class AvatarServiceImpl implements AvatarService {
    
    private final AvatarRepo avatarRepo;
    
    @Override
    public Avatar save(Avatar avatar) {
        return avatarRepo.save(avatar);
    }
    
    @Override
    public boolean check(Long id) {
        return avatarRepo.existsById(id);
    }
    
    @Override
    public Boolean remove(Long id) {
        try {
            avatarRepo.deleteById(id);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    @Override
    public Avatar getById(Long id) {
        return avatarRepo.findAvatarById(id);
    }
    
    @Override
    public Avatar addAvatar(Avatar avatar) {
        return avatarRepo.save(avatar);
    }
} 