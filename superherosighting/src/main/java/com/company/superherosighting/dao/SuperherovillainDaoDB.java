package com.company.superherosighting.dao;

import com.company.superherosighting.entities.Location;
import com.company.superherosighting.entities.Organisation;
import com.company.superherosighting.entities.Superherovillain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository //uses this annotation to signify that the class is used to perform CRUD operations on a database
public class SuperherovillainDaoDB implements  SuperherovillainDao{

    @Autowired
    JdbcTemplate jdbc;

    /**
     * used to retrieve all the superheroes/ villains stored within the database
     *
     * @return all superheroes/ villains saved represented as a list of superherovillain objects
     */
    @Override
    public List<Superherovillain> getAllSuperherovillain() {
        final String SELECT_ALL_SUPERHEROVILLAINS = "select * from superherovillain";
        /**
         * has to save it as a list to pass it through to addLocationsOrganisationToList method to set the locations and
         * organisation objects relating to each superhero/villains within the database
         */
        List<Superherovillain> allSuperherovillains = jdbc.query(SELECT_ALL_SUPERHEROVILLAINS, new SuperherovillainMapper());
        addLocationsOrganisationToList(allSuperherovillains);
        return allSuperherovillains;
    }

    /**
     * used to retrieve only the superheroes from the database
     *
     * @return all the superheroes stored within the system
     */
    @Override
    public List<Superherovillain> getAllSuperheroes() {
        final String SELECT_ALL_SUPERHEROES = "select * from superherovillain where ishero = true";
        /**
         * has to save it as a list to pass it through to addLocationsOrganisationToList method to set the locations and
         * organisation objects relating to each superhero within the database
         */
        List<Superherovillain> allSuperheroes = jdbc.query(SELECT_ALL_SUPERHEROES, new SuperherovillainMapper());
        addLocationsOrganisationToList(allSuperheroes);
        return allSuperheroes;
    }

    /**
     * @return all the supervillains stored within the system
     */
    @Override
    public List<Superherovillain> getAllSupervillains() {
        final String SELECT_ALL_SUPERVILLAINS = "select * from superherovillain where ishero = false";
        /**
         * has to save it as a list to pass it through to addLocationsOrganisationToList method to set the locations and
         * organisation objects relating to each supervillain within the database
         */
        List<Superherovillain> allSupervillains = jdbc.query(SELECT_ALL_SUPERVILLAINS, new SuperherovillainMapper());
        addLocationsOrganisationToList(allSupervillains);
        return allSupervillains;
    }

    /**
     * used to retrieve a single superhero/ villain object based on their id
     *
     * @param id of the superhero/ villain to be retrieved from the database
     * @return a superherovillain object with the given id
     */
    @Override
    public Superherovillain getSuperherovillainById(int id) {
        try {
            final String SELECT_SUPERHEROVILLAIN_BY_ID = "select * from superherovillain where superId = ?";
            Superherovillain superherovillain =  jdbc.queryForObject(SELECT_SUPERHEROVILLAIN_BY_ID,
                    new SuperherovillainMapper(), id);
            /**
             * needs to call on separate methods to return organisation and location objects
             */
            superherovillain.setOrganisation(getSuperherovillainOrganisation(id));
            superherovillain.setLocationsSightedAt(getSuperherovillainLocations(id));
            return superherovillain;
        } catch (DataAccessException e) {
            // reaches here if the user tries to access an id of a superherovillain that does not exist
            return null;
        }
    }

    /**
     * since superherovillain objects include an Organisation object to represent which organisation they are a part of
     * a method is required to get the Organisation object that relates to the superherovillain as in the database it
     * is stored as a foreign key of the id of the organisation
     *
     * @param id of the superhero whose organisation you wish to obtain
     * @return an organisation object reflecting the organisation that the superhero is a part of
     */
    private Organisation getSuperherovillainOrganisation(int id) {
        try {
            /**
             * uses a join clause to get all the attributes of the organisation based on the organisationid foreign key
             * within the superherovillain object
             */
            final String SELECT_ORGANISAION_FOR_SUPERHEROVILLAIN = "SELECT org.* \n" +
                    "FROM organisation org \n" +
                    "JOIN superherovillain svh \n" +
                    "ON svh.organisationId = org.organisationId \n" +
                    "WHERE svh.superid = ?";
            /**
             * uses queryForObject since only one organisation is returned (one-to-many)
             */
            return jdbc.queryForObject(SELECT_ORGANISAION_FOR_SUPERHEROVILLAIN, new OrganisationDaoDB.OrganisationMapper(), id);
        } catch (DataAccessException e) {
            //if the superhero isn't part of an organisation
            return null;
        }
    }

    /**
     * since superherovillain contains a list of locations of where the superhero has been (many to many relationship)
     * in order to get a superherovillain object it needs to generate that list of possible locations
     *
     * @param id of the superherovillain whose locations are being returned
     * @return a list of all locations where the superherovillain has been sighted
     */
    private List<Location> getSuperherovillainLocations(int id) {
        try {
            /**
             * uses a join clause between the location table which holds all the location attribute and the bridge table
             * between the superherovillain table
             */
            final String SELECT_LOCATIONS_FOR_SUPERHEROVILLAIN = "SELECT l.* \n" +
                    "FROM location l \n" +
                    "JOIN superherovillain_location sl \n" +
                    "ON sl.locationId = l.locationId \n" +
                    "WHERE sl.superId = ?; ";
            return jdbc.query(SELECT_LOCATIONS_FOR_SUPERHEROVILLAIN, new LocationDaoDB.LocationMapper(), id);
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public int countSuperheroesAtLocation(int locationId) {
        final String SELECT_COUNT_SUPERHEROES_AT_LOCATION = "SELECT COUNT(sl.superId)\n" +
                "FROM location l \n" +
                "JOIN superherovillain_location sl \n" +
                "ON sl.locationId = l.locationId \n" +
                "WHERE sl.locationid = ?";
        int count = jdbc.queryForObject(SELECT_COUNT_SUPERHEROES_AT_LOCATION, Integer.class, locationId);
        return count;
    }

    /**
     * goes through a list of superherovillain objects and sets the organisation object and location lists
     * @param superherovillains the list of superherovillains to set the organisation and locations
     */
    private void addLocationsOrganisationToList(List<Superherovillain> superherovillains) {
        for (Superherovillain shv : superherovillains) {
            shv.setOrganisation(getSuperherovillainOrganisation(shv.getId()));
            shv.setLocationsSightedAt(getSuperherovillainLocations(shv.getId()));
        }
    }

    /**
     * returns all the members within a certain organisation based on a given organisation id
     *
     * @param organisationId of the organisation whose members you wish to view
     * @return all the members within the organisation
     */
    @Override
    public List<Superherovillain> getAllMembersFromOrganisationById(int organisationId) {
        final String SELECT_MEMBER_BY_ORGANISATION = "select * \n" +
                "from superherovillain\n" +
                "where organisationid = ?";
        List<Superherovillain> organisationMembers = jdbc.query(SELECT_MEMBER_BY_ORGANISATION, new SuperherovillainMapper(), organisationId);
        addLocationsOrganisationToList(organisationMembers);
        return organisationMembers;
    }

    /**
     * used to add a new superhero/ villain to the system
     *
     * @param superherovillain object representing all the data about the hero/ villain to be added to the system
     */
    @Override
    @Transactional // multiple queries are processed
    public Superherovillain addSuperherovillain(Superherovillain superherovillain) {
        if (superherovillain.getOrganisation() != null) {
            final String INSERT_SUPERHEROVILLAIN = "insert into superherovillain(name, description, superpower, ishero, organisationId)\n" +
                    "values(?, ? , ?, ?, ? )";
            jdbc.update(INSERT_SUPERHEROVILLAIN,
                    superherovillain.getName(),
                    superherovillain.getDescription(),
                    superherovillain.getSuperpower(),
                    superherovillain.isHero(),
                    superherovillain.getOrganisation().getId()); //gets the id of the organisation object as that's whats held in the database
        } else {
            final String INSERT_SUPERHEROVILLAIN = "insert into superherovillain(name, description, superpower, ishero)\n" +
                    "values(?, ? , ?, ?)";
            jdbc.update(INSERT_SUPERHEROVILLAIN,
                    superherovillain.getName(),
                    superherovillain.getDescription(),
                    superherovillain.getSuperpower(),
                    superherovillain.isHero()); //gets the id of the organisation object as that's whats held in the database

        }
            /**
         * uses the last_insert_id mysql function to get the id of the superherovillain that has just been added to be able to set
         * it in the superherovillain object
         */
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        superherovillain.setId(newId);
        return superherovillain;
    }

    /**
     * used to edit the details about a pre-existing superhero/ villain within the system
     *
     * @param superherovillain holds the new updated information about the superhero/ villain
     */
    @Override
    public void editSuperherovillain(Superherovillain superherovillain) {
        try {
            final String UPDATE_SUPERHEROVILLAIN = "update superherovillain set name = ?, description = ?, superpower = ? , ishero = ?, " +
                    "organisationid = ? where superid = ?";
            jdbc.update(UPDATE_SUPERHEROVILLAIN,
                    superherovillain.getName(),
                    superherovillain.getDescription(),
                    superherovillain.getSuperpower(),
                    superherovillain.isHero(),
                    superherovillain.getOrganisation().getId(),
                    superherovillain.getId());
        } catch (DataAccessException e) {
        // reaches here if the user tries to access an id of a superherovillain that does not exist

    }
    }

    /**
     * used to delete a superhero/ villain based on their id
     *
     * @param id of the superhero/ villain to be removed from the database
     */
    @Override
    @Transactional
    public void deleteSuperherovillainById(int id) {
        try {
        /**
         * need to delete the entry from the bridge table superherovillain_location that tracks which location
         * a superhero/ villain has been sighted as this superherovillain no longer exists
         */
        final String DELETE_SUPERHEROVILLAIN_LOCATION = "DELETE FROM superherovillain_location "
                + "WHERE superId = ?";
        jdbc.update(DELETE_SUPERHEROVILLAIN_LOCATION, id);

        /**
         * after this processing the location itself can be deleted
         */
        final String DELETE_SUPERHEROVILLAIN = "DELETE FROM superherovillain WHERE superid = ?";
        jdbc.update(DELETE_SUPERHEROVILLAIN, id);
        } catch (DataAccessException e) {
            // reaches here if the user tries to access an id of a superherovillain that does not exist

        }
    }

    public static final class SuperherovillainMapper implements RowMapper<Superherovillain> {
        @Override
        public Superherovillain mapRow(ResultSet rs, int index) throws SQLException {
            Superherovillain superhv = new Superherovillain();
            superhv.setId(rs.getInt("superId"));
            superhv.setName(rs.getString("name"));
            superhv.setDescription(rs.getString("description"));
            superhv.setSuperpower(rs.getString("superpower"));
            superhv.setHero(rs.getBoolean("isHero"));
            return superhv;
        }
    }
}
