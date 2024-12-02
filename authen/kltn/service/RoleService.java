package com.example.kltn.service;

import java.util.List;

import com.example.kltn.entity.Role;

public interface RoleService {
    Role createRole(Role role);

    List<Role> getAll();

    List<Role> getByName(String name);

    Role getById(Long id);
}
