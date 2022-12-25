package com.company.superherosighting.dao;

import com.company.superherosighting.entities.Superherovillain;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for all the methods associated with adding, updating, viewing, and deleting from the
 * superherovillain table
 */
@SpringBootTest
class SuperherovillainDaoDBTest {

    /**
     * when adding/ deleting a superhero it will test to see if it worked by checking whether the total count
     * has changed
     */
    int heroesCount;

    @Autowired
    SuperherovillainDaoDB dao;

    /**
     * has a set count for what the total number of heroes is before the tests are run
     */
    @BeforeEach
    void setup() {
        List<Superherovillain> allHeroes = dao.getAllSuperherovillain();
        heroesCount = allHeroes.size();
    }


    /**
     * testing to see if the right superhero is returned when calling this method
     */
    @Test
    void getSuperherovillainById() {
        Superherovillain superhero = dao.getSuperherovillainById(1);
        assertEquals("Invincible", superhero.getName());
    }

    @Test
    void getSuperherovillainByNonexistentId() {
        Superherovillain notSoSuperherovillain = dao.getSuperherovillainById(-1);
        assertNull(notSoSuperherovillain);
    }


    /**
     * testing to see if it counts the number of superheroes who appear at a specific location the correct amount
     */
    @Test
    void countSuperheroesAtLocation() {
        int numLoc1 = dao.countSuperheroesAtLocation(1);
        assertEquals(2, numLoc1);
        int numLoc7 = dao.countSuperheroesAtLocation(7);
        assertEquals(3, numLoc7);
    }

    /**
     * testing to see if adding a new superhero will increment the size of the superheroes list
     */
    @Test
    void addSuperherovillain() {
        Superherovillain sh = new Superherovillain();
        sh.setName("Superman");
        sh.setDescription("he is one of the strongest");
        sh.setSuperpower("superhuman");
        dao.addSuperherovillain(sh);
        List<Superherovillain> allHeroes = dao.getAllSuperherovillain();
        int newCount = allHeroes.size();
        // checks to see if the size of the list of heroes has increased by one
        assertEquals(newCount, heroesCount + 1);
    }

    /**
     * testing to see if the changes made when editing an existing superhero are retained
     */
    @Test
    void editSuperherovillain() {
        /**
         * takes a pre-existing hero from the database and modifies it and then checks that those changes have persisted
         */
        Superherovillain invincible = dao.getSuperherovillainById(1);
        invincible.setDescription("he wears a yellow suit");
        dao.editSuperherovillain(invincible);
        Superherovillain invincibleCheck = dao.getSuperherovillainById(1);
        assertEquals("he wears a yellow suit", invincibleCheck.getDescription());
    }

    /**
     * testing to see if a superhero is deleted from the system by checking whether the size of the list decreases
     */
    @Test
    void deleteSuperherovillainById() {
        addSuperherovillain();
        dao.deleteSuperherovillainById(heroesCount + 1);
        List<Superherovillain> allHeroes = dao.getAllSuperherovillain();
        int newCount = allHeroes.size();
        assertEquals(heroesCount, newCount);
        assertEquals(null, dao.getSuperherovillainById(heroesCount + 1));
    }
}