package tema.sandalias.prova.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SandaliasController {

    @GetMapping(value = {"/", "/index", "/index.html"})
    public String telainicial(){
        return "index";
    }
}
