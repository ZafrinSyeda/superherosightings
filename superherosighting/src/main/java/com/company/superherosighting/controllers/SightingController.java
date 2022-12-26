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
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Valid;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    Set<ConstraintViolation<Superherovillain>> shvErrors = new HashSet<>();
    Set<ConstraintViolation<Location>> locErrors = new HashSet<>();

    @GetMapping("sightings")
    public String displaySightings(Model model) {
        addNewHeroOption = true;
        List<Sighting> sightings = sightingDao.getAllSightings();
        List<Superherovillain> superherovillains = superDao.getAllSuperherovillain();
        List<Location> locations = locationDao.getAllLocations();
        List<Organisation> organisations = orgDao.getAllOrganisations();

        model.addAttribute("sightings", sightings);
        model.addAttribute("superherovillains", superherovillains);
        model.addAttribute("locations", locations);
        model.addAttribute("organisations", organisations);
       // model.addAttribute("addHeroBool", addNewHeroOption);
        model.addAttribute("shvErrors", shvErrors);
        model.addAttribute("locErrors", locErrors);
        return "sightings";
    }

    @PostMapping("addSighting")
    public String addSighting(Sighting sighting, HttpServletRequest request) {

        String addHero = request.getParameter("addNewHeroOption");
        // System.out.println(addLocation);
       // System.out.println(addHero);
       // System.out.println(addNewHeroOption);
       // System.out.println("something");
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

            Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
            shvErrors = validate.validate(shv);
            if(shvErrors.isEmpty()) {
                superDao.addSuperherovillain(shv);
                sighting.setSuperherovillain(shv);
            } else {
                return "redirect:/sightings";
            }

        } else {
            String superId = request.getParameter("superId");
            Superherovillain shv = superDao.getSuperherovillainById(Integer.parseInt(superId));
            sighting.setSuperherovillain(shv);
        }
        String addLocation = request.getParameter("addNewLocationOption");
        System.out.println(addLocation);

        if(addLocation != null){
            System.out.println("hit");
            Location loc = new Location();
            loc.setName(request.getParameter("locationName"));
            loc.setDescription(request.getParameter("locationDescription"));
            loc.setAddress(request.getParameter("address"));
            loc.setCity(request.getParameter("city"));
            loc.setCountry(request.getParameter("country"));
            loc.setPostcode(request.getParameter("postcode"));
            if(request.getParameter("longitude") != null) {
                try {
                    loc.setLongitude(Double.parseDouble(request.getParameter("longitude")));
                } catch (NumberFormatException e) {

                }
            }
            if (request.getParameter("latitude") != null) {
                try {
                    loc.setLatitude(Double.parseDouble(request.getParameter("latitude")));
                } catch (NumberFormatException e) {

                }
            }

            Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
            locErrors = validate.validate(loc);
            if(locErrors.isEmpty()) {
                locationDao.addLocation(loc);
                sighting.setLocation(loc);
            } else {
                return "redirect:/sightings";
            }
        } else {
            String locationId = request.getParameter("locationId");
            Location loc = locationDao.getLocationById(Integer.parseInt(locationId));
            sighting.setLocation(loc);
        }
        sighting.setTimeSighted(LocalDateTime.now());
        sightingDao.addSighting(sighting);

        return "redirect:/sightings";
    }

    @GetMapping("deleteSighting")
    public String deleteStudent(Integer id) {
        sightingDao.deleteSighting(id);
        return "redirect:/sightings";
    }


}
