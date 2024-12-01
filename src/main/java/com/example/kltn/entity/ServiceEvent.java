package com.example.kltn.entity;

import java.io.Serializable;
import java.util.*;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "services")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceEvent implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "serviceCategory_id")
    @JsonIgnoreProperties("services")
    private ServiceCategory serviceCategory;

    @ManyToMany
    @JoinTable(
        name = "location_service",
        joinColumns = @JoinColumn(name = "service_id"),
        inverseJoinColumns = @JoinColumn(name = "location_id")
    )
    @JsonIgnoreProperties("services")
    private Set<Location> locations = new HashSet<>();

    private String servicename;
    private String description;
    private double price;

    @OneToMany(mappedBy = "serviceEvent")
    @JsonIgnoreProperties("serviceEvent")
    private List<ImageService> imageService;
}
