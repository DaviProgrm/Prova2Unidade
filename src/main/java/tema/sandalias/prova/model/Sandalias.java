package tema.sandalias.prova.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sandalias {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private boolean deleted;
    private String imageUri;
    @Size(min = 2, max = 20)
    private String marca;
    @Size(min = 2)
    private String modelo;
    @Size(min = 1, max = 100)
    private int tamanho;
    @Size(min = 1, max = 20)
    private String cor;

}
