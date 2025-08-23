package app.eurocars.manufacturer.model;

import app.eurocars.part.model.Part;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;


@Getter
@Setter
@Entity
@Table(name = "manufacturers")
public class Manufacturer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "img_ulr", nullable = false)
    private String imgUrl;

    @OneToMany(mappedBy = "manufacturer")
    private Set<Part> parts = new HashSet<>();
}
