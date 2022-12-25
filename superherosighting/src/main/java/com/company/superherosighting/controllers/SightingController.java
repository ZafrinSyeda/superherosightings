package com.company.superherosighting.controllers;

import com.company.superherosighting.dao.LocationDaoDB;
import com.company.superherosighting.dao.SightingDaoDB;
import com.company.superherosighting.dao.SuperherovillainDaoDB;
import com.company.superherosighting.entities.Location;
import com.company.superherosighting.entities.Sighting;
import com.company.superherosighting.entities.Superherovillain;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class SightingController {
    @Autowired
    SightingDaoDB sightingDao;

    @Autowired
    SuperherovillainDaoDB superDao;

    @Autowired
    LocationDaoDB locationDao;

    @GetMapping("sightings")
    public String displaySightings(Model model) {
        List<Sighting> sightings = sightingDao.getAllSightings();
        List<Superherovillain> superherovillains = superDao.getAllSuperheroes();
        List<Location> locations = locationDao.getAllLocations();
        model.addAttribute("sightings", sightings);
        model.addAttribute("superherovillains", superherovillains);
        model.addAttribute("locations", locations);
        return "sightings";
    }

    @PostMapping("addSighting")
    public String addSighting(Sighting sighting, HttpServletRequest request) {
        String superId = request.getParameter("superId");
        String locationId = request.getParameter("locationId");
        sighting.setSuperherovillain(superDao.getSuperherovillainById(Integer.parseInt(superId)));
        sighting.setLocation(locationDao.getLocationById(Integer.parseInt(locationId)));
        sighting.setTimeSighted(LocalDateTime.now());
        sightingDao.addSighting(sighting);

        return "redirect:/sightings";

    }
}
