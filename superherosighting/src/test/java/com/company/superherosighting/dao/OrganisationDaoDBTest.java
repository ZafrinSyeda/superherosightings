package com.company.superherosighting.dao;

import com.company.superherosighting.entities.Organisation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test suite to test the functionality of the OrganisationDaoDB methods
 */
@SpringBootTest
class OrganisationDaoDBTest {

    @Autowired
    OrganisationDaoDB dao;

    /**
     * Tests the getOrganisationById method to ensure that the correct organisation is returned by testing
     * whether the attributes match as they should
     */
    @Test
    void getOrganisationById() {
        Organisation org = dao.getOrganisationById(1);
        assertEquals(org.getName(), "Guardians of the Globe");
    }

    /**
     * Tests whether calling a non-existent id in getOrganisationById returns null
     */
    @Test
    void getOrganisationByNonexistentId() {
        Organisation org = dao.getOrganisationById(-1);
        assertNull(org);
    }

    /**
     * Tests whether the organisation that a particular hero belongs to can be obtained
     * by entering the hero's id into the method
     */
    @Test
    void getOrganisationByHeroVillain() {
        Organisation org = dao.getOrganisationByHeroVillain(2); //batman's id
        assertEquals(org.getName(), "The Justice League");
    }
}