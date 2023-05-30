package tema.sandalias.prova.service;


import org.springframework.beans.factory.annotation.Autowired;
import tema.sandalias.prova.model.Usuario;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import tema.sandalias.prova.repository.UsuarioRepository;

import java.util.List;
import java.util.Optional;

    @Service
    public class UsuarioService implements UserDetailsService {

        UsuarioRepository repository;
        BCryptPasswordEncoder encoder;

        @Autowired
        public UsuarioService(UsuarioRepository repository, BCryptPasswordEncoder encoder) {
            this.repository = repository;
            this.encoder = encoder;
        }

        public void create(Usuario u){
            u.setPassword(encoder.encode(u.getPassword()));
            this.repository.save(u);
        }

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

            Optional<Usuario> user = repository.findByUsername(username);
            if (user.isPresent()){
                return user.get();
            }else{
                throw new UsernameNotFoundException("Username not found");
            }
        }

        public List<Usuario> listAll(){
            return  repository.findAll();
        }
}
