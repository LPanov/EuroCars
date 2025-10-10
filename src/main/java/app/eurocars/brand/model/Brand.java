package app.eurocars.brand.model;

import app.eurocars.car.models.model.Model;
import app.eurocars.vehicle.type.model.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "brands")
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @ManyToMany(mappedBy = "brands", fetch = FetchType.EAGER)
    private List<VehicleType> types;

    @OneToMany(mappedBy = "brand")
    private List<Model> models = new ArrayList<>();
}
