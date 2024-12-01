package com.example.kltn.service.implement;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import com.example.kltn.entity.Role;
import com.example.kltn.repository.RoleRepo;
import com.example.kltn.service.RoleService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
@SessionScope
public class RoleServiceImpl implements RoleService {
    private final RoleRepo roleRepo;

    @Override
    public Role createRole(Role role) {
        return roleRepo.save(role);
    }

    @Override
    public List<Role> getAll() {
        return roleRepo.findAll();
    }

    @Override
    public Role getById(Long id) {
        return roleRepo.findById(id).orElse(null);
    }

    @Override
    public void deleteRole(Long id) {
        roleRepo.deleteById(id);
    }

}
