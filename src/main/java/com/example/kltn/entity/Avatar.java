package com.example.kltn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "avatars")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Avatar extends Image{
    private String name;
}
