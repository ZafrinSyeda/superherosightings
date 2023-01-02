package com.company.superherosighting.controllers;

import com.company.superherosighting.dao.OrganisationDaoDB;
import com.company.superherosighting.dao.SuperherovillainDaoDB;
import com.company.superherosighting.entities.Organisation;
import com.company.superherosighting.entities.Superherovillain;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * delegates methods between the DAO and the webpage views to provide functionality for the user by calling
 * on the right methods relating to superheroes/ villains
 */
@Controller
public class SuperherovillainController {
    @Autowired
    SuperherovillainDaoDB superDao;

    @Autowired
    OrganisationDaoDB orgDao;

    /**
     * Used to set up the superheroes page by setting up the 2 lists the webpage uses to present heroes and villains
     * @param model the webpage
     * @return the superheroes page with the relevant information to be presented
     */
    @GetMapping("superheroes")
    public String displaySightings(Model model) {
        List<Superherovillain> superheroes = superDao.getAllSuperheroes();
        List<Superherovillain> supervillains = superDao.getAllSupervillains();
        model.addAttribute("superheroes", superheroes);
        model.addAttribute("supervillains", supervillains);
        return "superheroes";
    }

    /**
     * Performs a deletion of a user-selected superhero/ villain
     * @param id of the superhero/ villain to be deleted
     * @return the superheroes page without the newly deleted hero/ villain
     */
    @GetMapping("deleteSuperherovillain")
    public String deleteSuperherovillain(Integer id) {
        superDao.deleteSuperherovillainById(id);
        return "redirect:/superheroes";
    }

    /**
     * presents an editsuperhero page based on some chosen superhero/ villain
     * @param id of the superhero/ villain to edit
     * @param model the edit webpage
     * @return the webpage with the relevant information about the superhero being edited and organisations they could
     * be a part of
     */
    @GetMapping("editSuperhero")
    public String editSuperherovillain(Integer id, Model model) {
        Superherovillain shv = superDao.getSuperherovillainById(id);
        List<Organisation> organisations = orgDao.getAllOrganisations();
        model.addAttribute("shv", shv);
        model.addAttribute("organisations", organisations);
        return "editSuperhero";
    }

    /**
     * applies the changes that the user wishes to make to the superhero
     * @param shv the superhero/ villain object being modified
     * @param request used to get the values the user submitted
     * @return the superhero page with the newly edited values
     */
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
