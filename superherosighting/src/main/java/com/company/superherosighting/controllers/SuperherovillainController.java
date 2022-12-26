package com.company.superherosighting.controllers;

import com.company.superherosighting.dao.SuperherovillainDaoDB;
import com.company.superherosighting.entities.Superherovillain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SuperherovillainController {
    @Autowired
    SuperherovillainDaoDB superDao;

    @GetMapping("superheroes")
    public String displaySightings(Model model) {
        List<Superherovillain> superheroes = superDao.getAllSuperheroes();
        List<Superherovillain> supervillains = superDao.getAllSupervillains();
        model.addAttribute("superheroes", superheroes);
        model.addAttribute("supervillains", supervillains);
        return "superheroes";
    }

    @GetMapping("deleteSuperherovillain")
    public String deleteSuperherovillain(Integer id) {
        superDao.deleteSuperherovillainById(id);
        return "redirect:/superheroes";
    }
}
