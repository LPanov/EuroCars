package app.eurocars.part.model;

import app.eurocars.category.model.Category;
import app.eurocars.engine.model.Engine;
import app.eurocars.manufacturer.model.Manufacturer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Table(name = "parts")
@Entity
@Builder
@AllArgsConstructor
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Basic
    private String description;

    @Column(name = "images")
    private Set<String> imgUrls;

    @Basic
    private BigDecimal price;

    @Basic
    private Double weight;

    @Column(name = "other_numbers")
    private Set<String> otherNumbers;

    @Column(name = "additional_information")
    private String additionalInformation;

    @Basic
    private LocalDateTime createdDate;

    @Basic
    private LocalDateTime updatedOn;

    @ManyToOne
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "parts_engines",
            joinColumns = @JoinColumn(name = "part_id"),
            inverseJoinColumns = @JoinColumn(name = "engine_id"))
    private Set<Engine> engines;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Part() {
        this.imgUrls = new HashSet<>();
        this.otherNumbers = new HashSet<>();
        this.engines = new HashSet<>();
    }
}
