package app.eurocars.engine.model;

import app.eurocars.car.models.model.Model;
import app.eurocars.part.model.Part;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "engines")
@Builder
@AllArgsConstructor
public class Engine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String type;

    @Column(name = "prod_years", nullable = false)
    private String prodYears;

    @Basic
    private Integer kw;

    @Basic
    private Integer hp;

    @Column(name = "eng_capacity")
    private Integer engCapacity;

    @Column(name = "car_img")
    private String carImg;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private Model model;

    @ManyToMany(mappedBy = "engines")
    private Set<Part> parts;

    public Engine() {
        parts = new HashSet<>();
    }
}
