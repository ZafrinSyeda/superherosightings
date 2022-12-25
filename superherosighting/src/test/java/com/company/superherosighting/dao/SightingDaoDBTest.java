package com.company.superherosighting.dao;

import com.company.superherosighting.entities.Sighting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite to test the overall functionality of the SightingDaoDB methods
 */
@SpringBootTest
class SightingDaoDBTest {

    /**
     * counter for the number of sightings, to be compared with when adding/ deleting from the database
     */
    int sightingCount;

    @Autowired
    SightingDaoDB dao;

    /*
    used to get a Location and Superherovillain object by using the getbyid method
     */
    @Autowired
    LocationDaoDB locationDao;
    @Autowired
    SuperherovillainDaoDB superDao;

    /**
     * setup before testing to get the total number of sightings before the tests are called to be compared
     * with when adding/ deleting from the database
     */
    @BeforeEach
    void setup() {
        sightingCount = dao.getAllSightings().size();
    }

    /**
     * Tests whether entering the right ID returns the right sighting object by checking if the attributes
     * of the superhero and location object within the sighting object are correct
     */
    @Test
    void getSightingById() {
        Sighting sighting = dao.getSightingById(1);
        assertEquals("Invincible", sighting.getSuperherovillain().getName());
        assertEquals("Christ the Redeemer", sighting.getLocation().getName());
    }

    /**
     * tests whether entering an id that does not exist in the sighting table returns null
     */
    @Test
    void getSightingByNonexistentId() {
        Sighting sighting = dao.getSightingById(-1);
        assertNull(sighting);
    }

    /**
     * Tests both whether adding a sighting both increases the size of the sighting table to signify
     * that the sighting has been added, and that adding a sighting will also add that location to the
     * location list within the superherovillain object
     */
    @Test
    void addSighting() {
        Sighting sighting = new Sighting();
        sighting.setSuperherovillain(superDao.getSuperherovillainById(2));
        sighting.setLocation(locationDao.getLocationById(2));
        sighting.setTimeSighted(LocalDateTime.now());
        dao.addSighting(sighting);
        int curSize = dao.getAllSightings().size();
        assertEquals(sightingCount + 1, curSize);
        assertTrue(superDao.getSuperherovillainById(2).getLocationsSightedAt().contains(locationDao.getLocationById(2)));

    }

    /**
     * Ensures that editing the sighting works as intended by ensuring that the element changed is
     * retained when getting the sighting for the second time after making the edit
     */
    @Test
    void editSighting() {
        Sighting sighting = dao.getSightingById(3);
        // before he was sighted at machu pichu - changes it to the colleseum
        sighting.setLocation(locationDao.getLocationById(1));
        dao.editSighting(sighting);
        assertEquals("The Colleseum", dao.getSightingById(3).getLocation().getName());
    }

    /**
     * tests if deleting a sighting works as intended by adding a new sighting, then checking that after deleting it
     * the size has decreased and should thus match the sighting count
     */
    @Test
    void deleteSighting() {
        Sighting sighting = new Sighting();
        sighting.setSuperherovillain(superDao.getSuperherovillainById(2));
        sighting.setLocation(locationDao.getLocationById(2));
        sighting.setTimeSighted(LocalDateTime.now());
        dao.addSighting(sighting);
        dao.deleteSighting(sightingCount);
        int curSize = dao.getAllSightings().size();
        assertEquals(sightingCount, curSize);
    }
}