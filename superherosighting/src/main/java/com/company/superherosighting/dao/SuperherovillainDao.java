package com.company.superherosighting.dao;

import com.company.superherosighting.entities.Location;
import com.company.superherosighting.entities.Superherovillain;

import java.util.List;

/**
 * Represents the various CRUD actions that can be performed with the database table representing a superhero/ villain
 */
public interface SuperherovillainDao {
    /**
     * used to retrieve all the superheroes/ villains stored within the database
     *
     * @return all superheroes/ villains saved represented as a list of superherovillain objects
     */
    List<Superherovillain> getAllSuperherovillain();

    /**
     * used to retrieve only the superheroes from the database
     *
     * @return all the superheroes stored within the system
     */
    List<Superherovillain> getAllSuperheroes();

    /**
     *
     * @return all the supervillains stored within the system
     */
    List<Superherovillain> getAllSupervillains();

    /**
     * used to retrieve a single superhero/ villain object based on their id
     *
     * @param id of the superhero/ villain to be retrieved from the database
     * @return a superherovillain object with the given id
     */
    Superherovillain getSuperherovillainById(int id);

    /**
     * returns all the members within a certain organisation based on a given organisation id
     *
     * @param organisationId of the organisation whose members you wish to view
     * @return all the members within the organisation
     */
    public List<Superherovillain> getAllMembersFromOrganisationById(int organisationId);

    int countSuperheroesAtLocation(int locationId);

    /**
     * used to add a new superhero/ villain to the system
     *
     * @param superherovillain object representing all the data about the hero/ villain to be added to the system
     */
    Superherovillain addSuperherovillain(Superherovillain superherovillain);

    /**
     * used to edit the details about a pre-existing superhero/ villain within the system
     *
     * @param superherovillain holds the new updated information about the superhero/ villain
     */
    void editSuperherovillain(Superherovillain superherovillain);

    /**
     * used to delete a superhero/ villain based on their id
     *
     * @param id of the superhero/ villain to be removed from the database
     */
    void deleteSuperherovillainById(int id);
}
