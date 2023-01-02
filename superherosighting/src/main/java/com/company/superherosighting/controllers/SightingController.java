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
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * delegates methods between the DAO and the webpage views to provide functionality for the user by calling
 * on the right methods relating to sightings
 */
@Controller
public class SightingController {
    /*
    Represents each of the Dao objects used within the webpage
     */
    @Autowired
    SightingDaoDB sightingDao;

    @Autowired
    SuperherovillainDaoDB superDao;

    @Autowired
    LocationDaoDB locationDao;

    @Autowired
    OrganisationDaoDB orgDao;

    /*
    Represents any of the errors based on user input
     */
    Set<ConstraintViolation<Superherovillain>> shvErrors = new HashSet<>();
    Set<ConstraintViolation<Location>> locErrors = new HashSet<>();

    /**
     * Provides the relevant information for the sightings page
     * @param model represents the webpage
     * @return the sighting page with the relevant information from the database
     */
    @GetMapping("sightings")
    public String displaySightings(Model model) {

        List<Sighting> sightings = sightingDao.getAllSightings();
        List<Superherovillain> superherovillains = superDao.getAllSuperherovillain();
        List<Location> locations = locationDao.getAllLocations();
        List<Organisation> organisations = orgDao.getAllOrganisations();

        model.addAttribute("sightings", sightings);
        model.addAttribute("superherovillains", superherovillains);
        model.addAttribute("locations", locations);
        model.addAttribute("organisations", organisations);

        model.addAttribute("shvErrors", shvErrors);
        model.addAttribute("locErrors", locErrors);
        return "sightings";
    }

    /**
     * Adds a new sighting to the system, based on some given parameters
     * @param sighting the sighting to be added to the database
     * @param request used to receive some parameters from the form
     * @return the sighting page refreshed to display either the new sighting or errors
     */
    @PostMapping("addSighting")
    public String addSighting(Sighting sighting, HttpServletRequest request) {
        /*
        The user has the option to add a new hero from the sightings page
        - so if they have selected that option a new hero will be added to the database and linked with the sighting
         */
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
            /*
            generates a blank location list, as the actual location gets added at the Dao level, it just
            prepares the list to be added to
             */
            List<Location> locationsSightedAt = new ArrayList<>();
            shv.setLocationsSightedAt(locationsSightedAt);

            /*
            Checks whether there are any errors in the user's input of the new superhero if they added one
            and if it is validated and there are no errors it adds the new superhero and otherwise refreshes the page
            to present the user with their errors
             */
            Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
            shvErrors = validate.validate(shv);
            if(shvErrors.isEmpty()) {
                superDao.addSuperherovillain(shv);
                sighting.setSuperherovillain(shv);
            } else {
                return "redirect:/sightings";
            }

        } else {
            /*
            if a new superhero isn't added the id of the hero within the sighting to be added is recieved
            and that hero is added to the sighting object to be added
             */
            String superId = request.getParameter("superId");
            Superherovillain shv = superDao.getSuperherovillainById(Integer.parseInt(superId));
            sighting.setSuperherovillain(shv);
        }

        /*
        Checks if the user has opted to add a new location or not
         */
        String addLocation = request.getParameter("addNewLocationOption");

        if(addLocation != null){
            System.out.println("hit");
            Location loc = new Location();
            loc.setName(request.getParameter("locationName"));
            loc.setDescription(request.getParameter("locationDescription"));
            loc.setAddress(request.getParameter("address"));
            loc.setCity(request.getParameter("city"));
            loc.setCountry(request.getParameter("country"));
            loc.setPostcode(request.getParameter("postcode"));
            /*
            to prevent errors it catches whether the field is blank or does not present a number
             */
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
            /*
            validates the new location to be added and either adds it to both the database and the sighting
            object or redirects the user to the sightings page to show an error message
             */
            Validator validate = Validation.buildDefaultValidatorFactory().getValidator();
            locErrors = validate.validate(loc);
            if(locErrors.isEmpty()) {
                locationDao.addLocation(loc);
                sighting.setLocation(loc);
            } else {
                return "redirect:/sightings";
            }
        } else {
             /*
            if a new location isn't added the id of the location within the sighting to be added is received
            and that location is added to the sighting object to be added
             */
            String locationId = request.getParameter("locationId");
            Location loc = locationDao.getLocationById(Integer.parseInt(locationId));
            sighting.setLocation(loc);
        }
        /*
        sets the timesighted to be the current time and adds the new sighting to the database
         */
        sighting.setTimeSighted(LocalDateTime.now());
        sightingDao.addSighting(sighting);

        return "redirect:/sightings";
    }

    /**
     * performs a deletion of a sighting based on some given id of the sighting the user has chosen to delete
     * @param id of the sighting to be deleted
     * @return the sightings page refreshed to show that the previous sighting was deleted
     */
    @GetMapping("deleteSighting")
    public String deleteSighting(Integer id) {
        sightingDao.deleteSighting(id);
        return "redirect:/sightings";
    }

    /**
     * presents an edit sighting page which consists of drop down boxes of existing superheroes and locations
     * @param id of the sighting to be edited
     * @param model the editsightings page
     * @return the edit sighting page with the relevant information to be presented
     */
    @GetMapping("editSighting")
    public String editSighting(Integer id, Model model) {
        Sighting sighting = sightingDao.getSightingById(id);
        List<Superherovillain> superherovillains = superDao.getAllSuperherovillain();
        List<Location> locations = locationDao.getAllLocations();
        List<Organisation> organisations = orgDao.getAllOrganisations();

        model.addAttribute("sighting", sighting);
        model.addAttribute("superherovillains", superherovillains);
        model.addAttribute("locations", locations);
        model.addAttribute("organisations", organisations);
        return "editSighting";
    }

    /**
     * performs an edit operation based on some given attributes that the user wishes to change in the
     * sighting
     * @param sighting to be edited
     * @param request used to access the parameters representing the attributes of the sighting
     * @return the sightings page including the newly edited sighting
     */
    @PostMapping("editSighting")
    public String postEditSighting(Sighting sighting, HttpServletRequest request) {
        String locationId = request.getParameter("locationId");
        String superId = request.getParameter("superId");
        String timeSighted = request.getParameter("timeSighted");

        sighting.setLocation(locationDao.getLocationById(Integer.parseInt(locationId)));
        sighting.setSuperherovillain(superDao.getSuperherovillainById(Integer.parseInt(superId)));
        sighting.setTimeSighted(LocalDateTime.parse(timeSighted));
        sightingDao.editSighting(sighting);

        return "redirect:/sightings";
    }

    /**
     * presents a sighting detail page of a specific sighting
     * @param id of the sighting to view in more specific detail
     * @param model the sightings page
     * @return the sighting detail page of the chosen sighting
     */
    @GetMapping("sightingDetail")
    public String sightingDetail(Integer id, Model model) {
        Sighting sighting = sightingDao.getSightingById(id);
        model.addAttribute("sighting", sighting);
        return "sightingDetail";
    }


}
