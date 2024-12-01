package com.example.kltn.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "image_events")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ImageEvent extends Image {
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
}
