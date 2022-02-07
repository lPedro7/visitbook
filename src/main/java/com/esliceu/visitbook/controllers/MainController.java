package com.esliceu.visitbook.controllers;

import com.esliceu.visitbook.configuration.MyConfig;
import com.esliceu.visitbook.models.Entry;
import com.esliceu.visitbook.models.Person;
import com.esliceu.visitbook.services.EntryService;
import com.esliceu.visitbook.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class MainController {

    @Autowired
    AuthenticationProvider provider;

    @Autowired
    UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    HttpSession session;

    @Autowired
    EntryService entryService;

    @GetMapping("/")
    public String index(Model model) {
        Person person = (Person) session.getAttribute("person");
        model.addAttribute("person", person);
        List<Entry> entries = entryService.findAll();
        model.addAttribute("entries", entries);

        System.out.println("person " + person);

        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(Model model,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam MultipartFile photo) {

        try {
            userDetailsServiceImpl.newUser(email, password, firstName, lastName, photo);
            return "register";
        } catch (Exception e) {
            model.addAttribute("error", "No s'ha pogut crear l'usuari");
            return "register";
        }
    }


    @GetMapping("/entry")
    public String getEntry(Model model) {
        Person person = (Person) session.getAttribute("person");
        model.addAttribute("person", person);
        return "entry";
    }

    @PostMapping("/entry")
    public String getEntry(Model model, @RequestParam String text) {
        Person person = (Person) session.getAttribute("person");
        model.addAttribute("person", person);
        entryService.newEntry(text);
        return "redirect:entry";
    }

    @PreAuthorize("hasRole('ROLE_ROOT')")
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") String id, Model model) {
        Person person = (Person) session.getAttribute("person");
        model.addAttribute("person", person);

        try {
            Entry entry = entryService.getEntryById(id);
            model.addAttribute("entry", entry);

        }catch (NumberFormatException numberFormatException){
            model.addAttribute("error","Error al trobar entrada");
        }


        return "edit";
    }

    @PreAuthorize("hasRole('ROLE_ROOT')")
    @PostMapping("/edit/{id}")
    public String edit(Model model,
                       @PathVariable("id") String id,
                       @RequestParam String text) {
        try {
            entryService.updateEntry(id, text);
            Entry entry = entryService.getEntryById(id);
            model.addAttribute("entry",entry);
        } catch (NumberFormatException numberFormatException) {
            model.addAttribute("error","Error a l'actualitzar entrada amb id : " + id);
            return "edit";
        }

        return "edit";
    }

    @PreAuthorize("hasRole('ROLE_ROOT')")
    @PostMapping("/delete/{id}")
    public String delete(Model model,
                         @PathVariable String id){
        try {
            entryService.delete(id);
        }catch (NumberFormatException numberFormatException){
            model.addAttribute("error","No s'ha pogut eliminar la entrada");
            return "edit";
        }
        return "index";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_ROOT')")
    @GetMapping("/admin/{id}")
    public String admin(Model model,@PathVariable String id){
        return "admin";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:login";
    }

}
