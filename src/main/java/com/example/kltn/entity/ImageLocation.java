package com.example.kltn.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "image_locations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageLocation extends Image {
    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location location;
}
