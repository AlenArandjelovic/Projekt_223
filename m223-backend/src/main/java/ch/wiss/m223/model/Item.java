package ch.wiss.m223.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter @Getter @NoArgsConstructor
public class Item {
    
    @Id
    @GeneratedValue(strategy=jakarta.persistence.GenerationType.AUTO)

    private Integer id;
    @NotNull
    private String titel;

    @NotNull
    private String description;

    public String getTitel() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User owner;
}
