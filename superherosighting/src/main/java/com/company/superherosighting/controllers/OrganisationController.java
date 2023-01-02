package com.company.superherosighting.controllers;

import com.company.superherosighting.dao.OrganisationDaoDB;
import com.company.superherosighting.entities.Location;
import com.company.superherosighting.entities.Organisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Provides the relevant operations relating to presenting organisation items
 */
@Controller
public class OrganisationController {
    @Autowired
    OrganisationDaoDB orgDao;

    /**
     * presents the organisation page and displays all the organisations within the database
     * @param model the organisation webpage
     * @return the organisation page
     */
    @GetMapping("organisations")
    public String displayOrganisations(Model model) {
        List<Organisation> organisations = orgDao.getAllOrganisations();
        model.addAttribute("organisations", organisations);
        return "organisations";
    }
}
