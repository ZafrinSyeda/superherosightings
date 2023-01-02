package com.company.superherosighting.controllers;
import com.company.superherosighting.dao.SightingDaoDB;
import com.company.superherosighting.entities.Sighting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Links to the home page and provide relevant Dao methods needed to present certain items on the home page
 */
@Controller
public class MainController {
    @Autowired
    SightingDaoDB sightingDao;

    /**
     * Sets up the home page to display the 10 most recent sightings
     * @param model the homepage
     * @return the homepage with the 10 most recent sightings
     */
    @GetMapping("/")
    public String displayRecentSightings(Model model) {
        List<Sighting> recentSightings = sightingDao.getRecent10Sightings();
        model.addAttribute("sightings", recentSightings);
        return "home";
    }
}
