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
@Table(name = "imageServices")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageService extends Image{
    @ManyToOne
    @JoinColumn(name = "serviceEvent_id")
    private ServiceEvent serviceEvent;
}
