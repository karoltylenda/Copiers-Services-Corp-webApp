package com.tytanisukcesu.copiers.entity;

import com.tytanisukcesu.copiers.types.PrintingFormat;
import lombok.*;
import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "models")
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean printsInColor;

    private Integer productionYear;

    @Column(nullable = false)
    private Integer printingSpeed;

    @ManyToOne(
            fetch = FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "manufacturer_id")
    private Manufacturer manufacturer;

    private PrintingFormat printingFormat;

    @ManyToMany(mappedBy = "models",
            cascade = CascadeType.REMOVE)
    @EqualsAndHashCode.Exclude
    private Set<Article> consumables;
}
