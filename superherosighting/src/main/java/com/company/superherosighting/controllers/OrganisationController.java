package com.company.superherosighting.controllers;

import com.company.superherosighting.dao.OrganisationDaoDB;
import com.company.superherosighting.entities.Location;
import com.company.superherosighting.entities.Organisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

import java.util.List;

@Controller
public class OrganisationController {
    @Autowired
    OrganisationDaoDB orgDao;

    public String displayOrganisations(Model model) {
        List<Organisation> organisations = orgDao.getAllOrganisations();
        model.addAttribute("organisations", organisations);
        return "organisations";
    }
}
