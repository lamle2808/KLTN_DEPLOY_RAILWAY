package com.example.kltn.entity;

import java.io.Serializable;
import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
@Table(name = "locations")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Location implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String title;
    
    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ImageLocation> images = new ArrayList<>();
    
    private String img;
    
    private String address;
    private Double latitude;
    private Double longitude;
    private Integer capacity;
    private Double price;
    private Double rating;
    private Integer size;
    private String description;
    
    @ElementCollection
    @CollectionTable(name = "location_features", 
        joinColumns = @JoinColumn(name = "location_id"))
    @Column(name = "feature")
    private List<String> features = new ArrayList<>();
    
    @ElementCollection
    @CollectionTable(name = "location_event_types", 
        joinColumns = @JoinColumn(name = "location_id"))
    @Column(name = "event_type")
    private List<String> eventTypes = new ArrayList<>();
    
    private String placeType;
    
    @ManyToMany(mappedBy = "locations")
    @JsonIgnoreProperties("locations")
    private Set<ServiceEvent> services = new HashSet<>();
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Account author;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<ImageLocation> imageLocations = new ArrayList<>();

    @OneToMany(mappedBy = "eventLocation", cascade = CascadeType.ALL)
    private List<Event> events = new ArrayList<>();
}
