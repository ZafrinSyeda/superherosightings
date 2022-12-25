package com.company.superherosighting.controllers;

import com.company.superherosighting.dao.LocationDaoDB;
import com.company.superherosighting.dao.OrganisationDaoDB;
import com.company.superherosighting.dao.SightingDaoDB;
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
public class SightingController {
    @Autowired
    SightingDaoDB sightingDao;

    @Autowired
    SuperherovillainDaoDB superDao;

    @Autowired
    LocationDaoDB locationDao;

    @Autowired
    OrganisationDaoDB orgDao;

    boolean addNewHeroOption;

    @GetMapping("sightings")
    public String displaySightings(Model model) {
       // addNewHeroOption = false;
        List<Sighting> sightings = sightingDao.getAllSightings();
        List<Superherovillain> superherovillains = superDao.getAllSuperherovillain();
        List<Location> locations = locationDao.getAllLocations();
        List<Organisation> organisations = orgDao.getAllOrganisations();

        model.addAttribute("sightings", sightings);
        model.addAttribute("superherovillains", superherovillains);
        model.addAttribute("locations", locations);
        model.addAttribute("organisations", organisations);
       // model.addAttribute("addNewHeroOption", addNewHeroOption);
        return "sightings";
    }

    @PostMapping("addSighting")
    public String addSighting(Sighting sighting, HttpServletRequest request) {
        String addHero = request.getParameter("addNewHeroOption");
        if (addHero != null) {
            Superherovillain shv = new Superherovillain();
            shv.setName(request.getParameter("heroName"));
            shv.setDescription(request.getParameter("heroDesc"));
            shv.setSuperpower(request.getParameter("superpower"));
            String orgId = request.getParameter("organisationId");
            if ((orgId) != null) {
                Organisation org = orgDao.getOrganisationById(Integer.parseInt(orgId));
                shv.setOrganisation(org);
            }
            String isHeroStr = request.getParameter("isHero");
            if (isHeroStr == null) {
                shv.setHero(false);
            } else {
                shv.setHero(true);
            }
            List<Location> locationsSightedAt = new ArrayList<>();
            shv.setLocationsSightedAt(locationsSightedAt);

            superDao.addSuperherovillain(shv);
            sighting.setSuperherovillain(shv);
        } else {
            String superId = request.getParameter("superId");
            sighting.setSuperherovillain(superDao.getSuperherovillainById(Integer.parseInt(superId)));
        }
        String addLocation = request.getParameter("addNewLocationOption");
        if(addLocation != null){
            Location loc = new Location();
            loc.setName(request.getParameter("locationName"));
            loc.setDescription(request.getParameter("locationDescription"));
            loc.setAddress(request.getParameter("address"));
            loc.setCity(request.getParameter("city"));
            loc.setCountry(request.getParameter("country"));
            loc.setPostcode(request.getParameter("postcode"));
            System.out.println(request.getParameter("longitude"));
            //loc.setLongitude(Double.parseDouble(request.getParameter("longitude")));
            //loc.setLatitude(Double.parseDouble(request.getParameter("latitude")));
            locationDao.addLocation(loc);
            sighting.setLocation(loc);
        } else {
            String locationId = request.getParameter("locationId");
            sighting.setLocation(locationDao.getLocationById(Integer.parseInt(locationId)));
        }
        sighting.setTimeSighted(LocalDateTime.now());
        sightingDao.addSighting(sighting);

        return "redirect:/sightings";

    }
}
