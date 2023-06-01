package tema.sandalias.prova.service;

import org.springframework.stereotype.Service;
import tema.sandalias.prova.model.Sandalias;
import tema.sandalias.prova.repository.SandaliasRepository;

import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;

@Service
public class SandaliasService {

    private SandaliasRepository repository;

    public SandaliasService(SandaliasRepository repository) {
        this.repository = repository;
    }

    public void save(Sandalias p){
//        p.tituloMaiusculo();
        repository.save(p);
    }

    public List<Sandalias> findAll(){
        return repository.findAll();
    }

    public Optional<Sandalias> findById(String id){
        return repository.findById(id);
    }

    public void delete(String id) {
        Optional<Sandalias> produtoOptional = repository.findById(id);
        if (produtoOptional.isPresent()) {
            Sandalias sandalias = produtoOptional.get();
            sandalias.setDeleted(LocalDateTime.now()); // Definir a data e hora atual local
            repository.save(sandalias);
        }
    }

    public List<Sandalias> findAllNotDeleted(){
        return repository.findAllNotDeleted();
    }

}
