package tema.sandalias.prova.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tema.sandalias.prova.model.Sandalias;


import java.util.List;
import java.util.Optional;


public interface SandaliasRepository extends JpaRepository<Sandalias, String> {
    @Query("SELECT p FROM Sandalias p WHERE p.deleted IS NULL")
    List<Sandalias> findAllNotDeleted();
    Optional<Sandalias> findById(String id);
}




