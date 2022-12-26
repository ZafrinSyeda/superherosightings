package com.company.superherosighting.controllers;

import com.company.superherosighting.dao.LocationDaoDB;
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

import java.util.List;

@Controller
public class LocationController {
    @Autowired
    LocationDaoDB locationDao;

    @Autowired
    SuperherovillainDaoDB superDao;

    @GetMapping("locations")
    public String displayLocations(Model model) {
        List<Location> locations = locationDao.getAllLocations();
        model.addAttribute("locations", locations);
        return "locations";
    }

    @GetMapping("deleteLocation")
    public String deleteLocation(Integer id) {
        locationDao.deleteLocationById(id);
        return "redirect:/locations";
    }

    @GetMapping("editLocation")
    public String editLocation(Integer id, Model model) {
        Location loc = locationDao.getLocationById(id);
        model.addAttribute("loc", loc);
        return "editLocation";
    }

    @PostMapping("editLocation")
    public String postEditLocation(Location loc, HttpServletRequest request) {
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
        locationDao.editLocation(loc);

        return "redirect:/locations";
    }
}

