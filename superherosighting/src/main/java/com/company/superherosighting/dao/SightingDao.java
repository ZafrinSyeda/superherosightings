package com.company.superherosighting.dao;

import com.company.superherosighting.entities.Location;
import com.company.superherosighting.entities.Sighting;
import com.company.superherosighting.entities.Superherovillain;

import java.util.List;

public interface SightingDao {
    /**
     * used to retrieve all the sightings where a superhero/ villain has been sighted
     *
     * @return all sightings
     */
    List<Sighting> getAllSightings();

    /**
     * used to only return the 10 most recent sightings to be displayed on the home page of the website
     * @return the most recent 10 sightings
     */
    List<Sighting> getRecent10Sightings();

    /**
     * used to return a single sighting object
     * @param id of the sighting
     * @return the sighting object that matches the id
     */
    Sighting getSightingById(int id);

    /**
     * Used to add a new superhero sighting to the system
     * @param sighting
     */
    Sighting addSighting(Sighting sighting);

    /**
     * used to edit the details about a pre-existing sighting within the system
     *
     * @param sighting holds the new updated information about the sighting
     */
    void editSighting(Sighting sighting);

    void deleteSighting(int id);
}
