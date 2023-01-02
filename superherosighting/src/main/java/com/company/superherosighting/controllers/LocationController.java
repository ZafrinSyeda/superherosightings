package com.company.superherosighting.controllers;

import com.company.superherosighting.dao.LocationDaoDB;
import com.company.superherosighting.entities.Location;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

/**
 * Links with the Location and LocationEdit webpages, and provides the backend functionality for presenting
 * or modifying some of the location entries
 */
@Controller
public class LocationController {
    /**
     * uses the locationDao functions to allow the user to perform CRUD operations from the front end
     */
    @Autowired
    LocationDaoDB locationDao;

    /**
     * Used to provide the list of all the locations to be presented on the website
     * @param model represents the user interface
     * @return the model with the locations list to be used
     */
    @GetMapping("locations")
    public String displayLocations(Model model) {
        List<Location> locations = locationDao.getAllLocations();
        model.addAttribute("locations", locations);
        return "locations";
    }

    /**
     * Calls on the method to delete some location based on a given location id
     * @param id of the location to be deleted
     * @return redirect to the location page, where it refreshes to the page such that will no longer display the
     * deleted location
     */
    @GetMapping("deleteLocation")
    public String deleteLocation(Integer id) {
        locationDao.deleteLocationById(id);
        return "redirect:/locations";
    }

    /**
     * Generates an edit location page based on some given ID of the location to edit
     * @param id of the location to edit
     * @param model of the webpage that presents the location editing options
     * @return an edit location page based on some specific location
     */
    @GetMapping("editLocation")
    public String editLocation(Integer id, Model model) {
        Location loc = locationDao.getLocationById(id);
        model.addAttribute("loc", loc);
        return "editLocation";
    }

    /**
     * Performs the editing operation on the location to be edited
     * @param loc the location to be edited
     * @param request used to get the names of paramaters from the webpage
     * @return the location page with the newly edited location
     */
    @PostMapping("editLocation")
    public String postEditLocation(Location loc, HttpServletRequest request) {
        /*
        Gets the relevant parameters from the editing form and uses them to set within the location object
         */
        loc.setName(request.getParameter("locationName"));
        loc.setDescription(request.getParameter("locationDescription"));
        loc.setAddress(request.getParameter("address"));
        loc.setCity(request.getParameter("city"));
        loc.setCountry(request.getParameter("country"));
        loc.setPostcode(request.getParameter("postcode"));
        /*
        If it's null or is not written as a double it throws errors, so the if statements and try/catch block
        is needed to deal with those 2 issues respectively
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
        locationDao.editLocation(loc);

        return "redirect:/locations";
    }
}

