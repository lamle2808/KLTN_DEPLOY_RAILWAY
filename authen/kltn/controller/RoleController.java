package com.example.kltn.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.kltn.entity.Role;
import com.example.kltn.service.RoleService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Role role) {
        try {
            return ResponseEntity.ok().body(roleService.createRole(role));
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("There is an exception when execute !! --> " + exception);
        }
    }

     @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable("id") Long id) {
        try {
            Role role = roleService.getById(id);
            if (role == null) {
                return ResponseEntity.badRequest().body("id is not found !!");
            }
            return ResponseEntity.ok().body(role);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("There is an exception when execute !! --> " + exception);
        }
    }

    @GetMapping("/getByName/{name}")
    public ResponseEntity<?> getByName(@PathVariable("name") String name) {
        try {
            List<Role> roles = roleService.getByName(name);
            if (roles.isEmpty()) {
                return ResponseEntity.badRequest().body(name + " not found!");
            }
            return ResponseEntity.ok().body(roles);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("There is an exception when execute !! --> " + exception);
        }

    }

     @GetMapping("/getList")
    public ResponseEntity<?> getAll() {
        try {
            List<Role> roleList = roleService.getAll();
            if (!roleList.isEmpty()) {
                return ResponseEntity.ok().body(roleList);
            }
            return ResponseEntity.badRequest().body("There are no roles in the database yet");
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body("There is an exception when execute !! --> " + exception);
        }

    }
}
