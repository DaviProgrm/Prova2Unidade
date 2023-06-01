package tema.sandalias.prova.Controller;



import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import tema.sandalias.prova.model.Sandalias;
import tema.sandalias.prova.service.SandaliasService;
import tema.sandalias.prova.util.Upload;
import tema.sandalias.prova.repository.SandaliasRepository;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SandaliasController {

    SandaliasService service;

    public SandaliasController(SandaliasService service) {
        this.service = service;
    }

    @RequestMapping(value = {"/admin", "/admin.html"}, method = RequestMethod.GET)
    public String getAdmin(Model model){
        List<Sandalias> sandaliasList = service.findAllNotDeleted();
        model.addAttribute("sandaliasList", sandaliasList);
        return "admin";
    }

    @RequestMapping(value = {"/", "/index", "/index.html"}, method = RequestMethod.GET)
    public String getIndex(Model model, HttpServletRequest request, HttpServletResponse response){

        Cookie[] cookies = request.getCookies();
        boolean visitCookieExists = false;
        String lastAccess = null;
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("lastAccess")) {
                    visitCookieExists = true;
                    // Obter o valor do cookie que representa a última data e hora de acesso
                    lastAccess = cookie.getValue();
                    break;
                }
            }
        }

        // Se o cookie de data e hora de acesso não existir, criar um novo cookie com a data e hora atual
        if (!visitCookieExists) {
            // Obter a data e hora atual
            LocalDateTime now = LocalDateTime.now();
            // Converter a data e hora em uma string no formato desejado, substituindo espaços por underscores
            String formattedDateTime = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm:ss"));
            formattedDateTime = formattedDateTime.replace(" ", "_"); // Replace space with underscore
            Cookie visitCookie = new Cookie("lastAccess", formattedDateTime);
            response.addCookie(visitCookie);
            lastAccess = formattedDateTime;
        }

        List<Sandalias> sandaliasList = service.findAllNotDeleted();
        model.addAttribute("sandaliasList", sandaliasList);


        HttpSession session = request.getSession();
        List<Sandalias> carrinho = (List<Sandalias>) session.getAttribute("carrinho");
        int quantidadeProdutos = carrinho != null ? carrinho.size() : 0;
        model.addAttribute("quantidadeProdutos", quantidadeProdutos);

        model.addAttribute("lastAccess", lastAccess); // Adicionar a data e hora de acesso ao modelo
        return "index";
    }

    @GetMapping("/cadastrarPage")
    public String getCadastrarPage(Model model){
        Sandalias p = new Sandalias();
        model.addAttribute("produto", p);
        return "cadastrarPage";
    }
     @Autowired
    private SandaliasRepository repo;
    @PostMapping("/doSalvar")
    public RedirectView saveProduto(@Valid @ModelAttribute("produto") Sandalias sandalias, BindingResult bindingResult, RedirectAttributes redirectAttributes, @RequestParam("image") MultipartFile multipartFile) throws IOException {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Erro ao editar o sandalias. Certifique-se de preencher todos os campos obrigatórios.");
            return new RedirectView("/admin", true);
        }else{
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            sandalias.setImageUri(fileName);

            Sandalias savedSandalias = repo.save(sandalias);

            String uploadDir = "user-photos/" + savedSandalias.getId();

            Upload.saveFile(uploadDir, fileName, multipartFile);
            redirectAttributes.addFlashAttribute("success", "Sandalias editado com sucesso!");
            return new RedirectView("/admin", true);

        }

    }

    @GetMapping("/editarPage/{id}")
    public String getEditarPage(@PathVariable(name = "id") String id, Model model){

        Optional<Sandalias> p = service.findById(id);
        if (p.isPresent()){
            model.addAttribute("produto", p.get());
        }else{
            return "redirect:/admin";
        }

        return "editarPage";
    }

    @GetMapping("/deletar/{id}")
    public String doDeletar(@PathVariable(name = "id") String id){
        Optional<Sandalias> produto = service.findById(id);
        if (produto.isPresent()) {
            service.delete(id);
        }
        return "redirect:/admin";
    }

 @GetMapping("/adicionarCarrinho/{id}")
    public void adicionarAoCarrinho(@PathVariable(name = "id") String id, HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        List<Sandalias> carrinho = (List<Sandalias>) session.getAttribute("carrinho");
        if (carrinho == null) {
            carrinho = new ArrayList<>();
        }
        Optional<Sandalias> produtoOptional = service.findById(id);
        if (produtoOptional.isPresent()) {
            Sandalias sandalias = produtoOptional.get();
            carrinho.add(sandalias);
        }
        session.setAttribute("carrinho", carrinho);
        response.sendRedirect("/index");
    }

    @GetMapping("/carrinhoPage")
    public String exibirCarrinho(Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession();
        List<Sandalias> carrinho = (List<Sandalias>) session.getAttribute("carrinho");

        if (carrinho == null || carrinho.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "O seu carrinho está vazio.");
            return "redirect:/index";
        } else {
            model.addAttribute("carrinho", carrinho);
            model.addAttribute("quantidadeProdutos", carrinho.size());
            return "carrinhoPage";
        }
    }

    @GetMapping("/finalizarCompra")
    public String finalizarCompra(HttpServletRequest request) {
        HttpSession session = request.getSession();

        // Invalida a sessão existente
        session.invalidate();

        // Redireciona para a página "index"
        return "redirect:/index";
    }

    @GetMapping("/logout")
    public String doLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();

        // Invalida a sessão existente
        session.invalidate();

        // Redireciona para a página "index"
        return "redirect:/index";
    }

}