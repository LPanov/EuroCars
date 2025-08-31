package app.eurocars.user.model;

import app.eurocars.cart.model.Cart;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "company_name", nullable = false, unique = true)
    private String companyName;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "company_address", nullable = false)
    private String companyAddress;

    @Basic
    private String city;

    @Basic
    private String state;

    @Column(name = "zip_code")
    private String zipCode;

    @Enumerated(EnumType.STRING)
    private Country country;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    @Column(name = "updated_on")
    private LocalDateTime updatedOn;

    @Column(name = "prices_with_VAT")
    private Boolean pricesWithVAT;

    @Column(name = "wholesale_prices")
    private Boolean wholesalePrices;

    @Column(name = "priducts_order")
    private Boolean productsOrder;

    @Column(name = "show_weight")
    private Boolean showWeight;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Cart cart;
}
