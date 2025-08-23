package app.eurocars.car.models.model;

import app.eurocars.brand.model.Brand;
import app.eurocars.engine.model.Engine;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "models")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(name = "production_years", nullable = false)
    private String prodYears;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @OneToMany(mappedBy = "model")
    private List<Engine> engines = new ArrayList<>();

}
