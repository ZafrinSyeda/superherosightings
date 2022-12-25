package com.company.superherosighting.controllers;

import com.company.superherosighting.dao.LocationDaoDB;
import com.company.superherosighting.dao.SightingDaoDB;
import com.company.superherosighting.dao.SuperherovillainDaoDB;
import com.company.superherosighting.entities.Sighting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    SightingDaoDB sightingDao;

    @Autowired
    LocationDaoDB locationDaoDB;

    @Autowired
    SuperherovillainDaoDB superDao;

    @GetMapping("home")
    public String displayRecentSightings(Model model) {
        List<Sighting> recentSightings = sightingDao.getRecent10Sightings();
        model.addAttribute("sightings", recentSightings);
        return "home";
    }
}
