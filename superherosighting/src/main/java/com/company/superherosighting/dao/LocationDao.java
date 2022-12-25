package com.company.superherosighting.dao;

import com.company.superherosighting.entities.Location;

import java.util.List;

/**
 * Represents the various CRUD actions that can be performed with the database table representing the locations that a
 * superhero/ villain may be sighted at
 */
public interface LocationDao {

    /**
     * Performs a Read operation in order to retrieve all the locations within the database
     *
     * @return all the locations stored within the database
     */
    List<Location> getAllLocations();

    /**
     * Retrieves a single location based on the ID of that location passed in
     *
     * @param id of the location to be retrieved
     * @return location object with the given id
     */
    Location getLocationById(int id);

    /**
     * used to add a new location to the database
     *
     * @param location object that holds all the information to be added to the database
     * @return the location added to the system
     */
    Location addLocation(Location location);

    /**
     * used to edit the details of a location saved within the database
     * @param location the location object with the necessary changes already in place to be saved to the database
     */
    void editLocation(Location location);

    /**
     * used to delete a location from the database based on its ID
     * @param id of the location to be removed from the database
     */
    void deleteLocationById(int id);
}
