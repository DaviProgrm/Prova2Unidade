package tema.sandalias.prova.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import lombok.*;

import java.time.LocalDateTime;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sandalias {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private LocalDateTime deleted;
    private String imageUri;
    private String marca;
    @NotNull
    private float preco;
    private String modelo;
    private int tamanho;
    private String cor;

    @Transient
    public String getPhotosImagePath() {
        if (imageUri == null || id == null) return null;

        return "/user-photos/" + id + "/" + imageUri;
    }

}
