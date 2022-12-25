package com.company.superherosighting.dao;

import com.company.superherosighting.entities.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite to test the functionality of the LocationDaoDB methods
 */
@SpringBootTest
class LocationDaoDBTest {

    /**
     * used to keep track of the total number of locations to see if the number changes when a location is
     * added or deleted
     */
    int locationCount;

    @Autowired
    LocationDaoDB dao;

    /**
     * setup before running the tests to count the total number of locations stored within the database
     * before something is added or deleted
     */
    @BeforeEach
    void setup() {
        List<Location> locations = dao.getAllLocations();
        locationCount = locations.size();
    }

    /**
     * tests to see if the correct location object is returned when calling by its ID
     */
    @Test
    void getLocationById() {
        Location colleseum = dao.getLocationById(1);
        assertEquals("The Colleseum", colleseum.getName());
    }

    /**
     * tests to see if null is returned when calling getlocationbyid to an id that should not exist within
     * the database
     */
    @Test
    void getLocationByNonexistsentId() {
        Location unknown = dao.getLocationById(-1);
        assertNull(unknown);
    }

    /**
     * tests to see if adding a new location works correctly by creating a new object, adding it, and checking
     * if the total count has increased, and if the most recent item added to the database has the same
     * attributes as the newly added location
     */
    @Test
    void addLocation() {
        Location newlocation = new Location();
        newlocation.setName("wiley edge");
        newlocation.setDescription(null);
        newlocation.setCity("london");
        newlocation.setCountry("england");
        newlocation.setAddress("3 Harbour Exchange Square");
        newlocation.setPostcode("E14 9GE");
        newlocation.setLongitude(-1);
        newlocation.setLatitude(-1);
        dao.addLocation(newlocation);
        assertEquals(locationCount + 1, dao.getAllLocations().size());
        assertEquals("wiley edge", dao.getLocationById(locationCount + 1).getName());
    }

    /**
     * tests to see if editing a location works by getting a preexisting location from the table, and changing
     * the description, then checking if it has changed as intended
     */
    @Test
    void editLocation() {
        Location colleseum = dao.getLocationById(1);
        colleseum.setDescription("i went there once on holiday and it was pretty cool");
        dao.editLocation(colleseum);
        assertEquals("i went there once on holiday and it was pretty cool", dao.getLocationById(1).getDescription());
    }

    /**
     * tests to see if deleting locations work by adding a new location to the database and then
     * removing it and checking if the count goes from being increased by one, back to the initial count
     */
    @Test
    void deleteLocationById() {
        Location newlocation = new Location();
        newlocation.setName("wiley edge");
        newlocation.setDescription(null);
        newlocation.setCity("london");
        newlocation.setCountry("england");
        newlocation.setAddress("3 Harbour Exchange Square");
        newlocation.setPostcode("E14 9GE");
        newlocation.setLongitude(-1);
        newlocation.setLatitude(-1);
        dao.addLocation(newlocation);
        dao.deleteLocationById(locationCount + 1);
        assertEquals(locationCount, dao.getAllLocations().size());
    }
}