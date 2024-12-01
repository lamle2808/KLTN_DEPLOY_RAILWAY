package com.example.kltn.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.kltn.entity.Role;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findRoleById(Long id);

    List<Role> findRoleByNameContaining(String name);

    Optional<Role> findByName(String name);
}
