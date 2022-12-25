package com.company.superherosighting.controllers;

import com.company.superherosighting.dao.SightingDaoDB;
import com.company.superherosighting.entities.Sighting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class SightingController {
    @Autowired
    SightingDaoDB sightingDao;

    @GetMapping("sightings")
    public String displaySightings(Model model) {
        List<Sighting> sightings = sightingDao.getAllSightings();
        model.addAttribute("sightings", sightings);
        return "sightings";
    }
}
