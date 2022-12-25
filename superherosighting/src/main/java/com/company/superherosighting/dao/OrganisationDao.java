package com.company.superherosighting.dao;


import com.company.superherosighting.entities.Organisation;

import java.util.List;

/**
 * Represents the various CRUD actions that can be performed with the database table representing the organisation that
 * a superhero/ villain may be a part of
 */
public interface OrganisationDao {
    /**
     * Used to retrieve all the organisations saved within the database
     *
     * @return all of the organisations stored
     */
    List<Organisation> getAllOrganisations();

    /**
     * Used to retrieve one of the organisations saved within the database represented as an organisation object based
     * on its ID
     *
     * @param id of the organisation to be retrieved
     * @return an object representing the organisation that has the given id
     */
    Organisation getOrganisationById(int id);

    /**
     * returns the organisation that a certain superhero/ villain is a part of
     *
     * @param superId of the superhero whose organisation you wish to retrieve
     * @return the organisation the superhero/ villain is a part of
     */
    Organisation getOrganisationByHeroVillain(int superId);

    /**
     * used to add a new organisation to the database
     *
     * @param organisation an object holding all the information about a particular organisation to be added to the
     *                     database
     */
    Organisation addOrganisation(Organisation organisation);

    /**
     * used to edit some of the details about an organisation
     *
     * @param organisation an object holding the newly edited information of a pre-existing organisation within the
     *                     database
     */
    void editOrganisation(Organisation organisation);

    /**
     * used to delete a database entry from the database
     *
     * @param id of the database to be deleted from the system
     */
    void deleteOrganisationById(int id);
}
