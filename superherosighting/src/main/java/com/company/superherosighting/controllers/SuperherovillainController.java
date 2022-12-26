package com.company.superherosighting.controllers;

import com.company.superherosighting.dao.LocationDaoDB;
import com.company.superherosighting.dao.OrganisationDaoDB;
import com.company.superherosighting.dao.SuperherovillainDaoDB;
import com.company.superherosighting.entities.Location;
import com.company.superherosighting.entities.Organisation;
import com.company.superherosighting.entities.Sighting;
import com.company.superherosighting.entities.Superherovillain;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SuperherovillainController {
    @Autowired
    SuperherovillainDaoDB superDao;

    @Autowired
    LocationDaoDB locationDao;

    @Autowired
    OrganisationDaoDB orgDao;

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

    @GetMapping("editSuperhero")
    public String editSuperherovillain(Integer id, Model model) {
        Superherovillain shv = superDao.getSuperherovillainById(id);
        List<Organisation> organisations = orgDao.getAllOrganisations();
        model.addAttribute("shv", shv);
        model.addAttribute("organisations", organisations);
        return "editSuperhero";
    }

    @PostMapping("editSuperhero")
    public String postEditSuperherovillain(Superherovillain shv, HttpServletRequest request) {
        shv.setName(request.getParameter("heroName"));
        shv.setDescription(request.getParameter("heroDesc"));
        shv.setSuperpower(request.getParameter("superpower"));
        String orgId = request.getParameter("organisationId");
        if (orgId != null) {
            shv.setOrganisation(orgDao.getOrganisationById(Integer.parseInt(orgId)));
        } else {
            shv.setOrganisation(null);
        }
        String isHeroStr = request.getParameter("isHero");
        if (isHeroStr == null) {
            shv.setHero(false);
        } else {
            shv.setHero(true);
        }
        superDao.editSuperherovillain(shv);

        return "redirect:/superheroes";
    }

}
