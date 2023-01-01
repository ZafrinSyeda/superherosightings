package com.company.superherosighting.dao;

import com.company.superherosighting.entities.Location;
import com.company.superherosighting.entities.Sighting;
import com.company.superherosighting.entities.Superherovillain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class SightingDaoDB implements SightingDao{

    @Autowired
    JdbcTemplate jdbc;

    /**
     * used to retrieve all the sightings where a superhero/ villain has been sighted
     *
     * @return all sightings
     */
    @Override
    public List<Sighting> getAllSightings() {
        /**
         * alongside returning all the sightings, it does it in an order so that the most recent ones are returned
         */
        final String SELECT_ALL_SIGHTINGS = "select * from superherovillain_location order by timesighted desc";
        List<Sighting> allSightings = jdbc.query(SELECT_ALL_SIGHTINGS, new SightingMapper());
        addSuperherovillainLocations(allSightings);
        return allSightings;
    }

    /**
     * used to only return the 10 most recent sightings to be displayed on the home page of the website
     *
     * @return the most recent 10 sightings
     */
    @Override
    public List<Sighting> getRecent10Sightings() {
        final String SELECT_TEN_SIGHTINGS = "select * from superherovillain_location order by timesighted desc limit 10";
        List<Sighting> tenSightings = jdbc.query(SELECT_TEN_SIGHTINGS, new SightingMapper());
        addSuperherovillainLocations(tenSightings);
        return tenSightings;
    }

    /**
     * used to return a single sighting object
     *
     * @param id of the sighting
     * @return the sighting object that matches the id
     */
    @Override
    public Sighting getSightingById(int id) {
        try {
            final String SELECT_SIGHTING_BY_ID = "select * from superherovillain_location where sightingId = ?";
            Sighting sighting =  jdbc.queryForObject(SELECT_SIGHTING_BY_ID,
                    new SightingMapper(), id);
            /**
             * needs to call on separate methods to return organisation and location objects
             */
            sighting.setSuperherovillain(getSightingSuperherovillain(id));
            sighting.setLocation(getSightingLocation(id));
            return sighting;
        } catch (DataAccessException e) {
            // reaches here if the user tries to access an id of a sighting that does not exist
            return null;
        }
    }

    /**
     * used to get the details of the superherovillain linked with the sighting to add to an object
     *
     * @param id of the sighting
     * @return the superherovillain linked with the sighting
     */
    private Superherovillain getSightingSuperherovillain(int id) {
        /**
         * uses a join clause to get all the attributes of the superherovillain linked to the sighting
         */
        final String SELECT_SUPERHEROVILLAIN_FOR_SIGHTING = "select sh.*\n" +
                "from superherovillain sh\n" +
                "join superherovillain_location sl\n" +
                "on sh.superid = sl.superid\n" +
                "where sl.sightingid = ?";
        /**
         * uses queryForObject since only one superhero is returned (one-to-many)
         */
        Superherovillain superherovillain = jdbc.queryForObject(SELECT_SUPERHEROVILLAIN_FOR_SIGHTING, new SuperherovillainDaoDB.SuperherovillainMapper(), id);
        return superherovillain;
    }

    /**
     * for each list of sightings returned it will set the necesary superherovillain and location objects
     */
    private void addSuperherovillainLocations(List<Sighting> sightings){
        for (Sighting s : sightings) {
            s.setSuperherovillain(getSightingSuperherovillain(s.getId()));
            s.setLocation(getSightingLocation(s.getId()));
        }
    }

    /**
     * used to get the details of the location linked with the sighting to add to an object
     *
     * @param id of the sighting
     * @return the location linked with the sighting
     */
    private Location getSightingLocation(int id) {
        /**
         * uses a join clause to get all the attributes of the location lineked to the sighting
         */
        final String SELECT_LOCATION_FOR_SIGHTING = "select l.*\n" +
                "from location l\n" +
                "join superherovillain_location sl\n" +
                "on l.locationid = sl.locationid\n" +
                "where sl.sightingid = ?";
        /**
         * uses queryForObject since only one location is returned (one-to-many)
         */
        return jdbc.queryForObject(SELECT_LOCATION_FOR_SIGHTING, new LocationDaoDB.LocationMapper(), id);
    }

    /**
     * Used to add a new superhero sighting to the system
     *
     * @param sighting
     */
    @Override
    @Transactional
    public Sighting addSighting(Sighting sighting) {
        final String INSERT_SIGHTING = "insert into superherovillain_location(superid, locationid, timesighted)\n" +
                "values(?, ? , ?)";
        jdbc.update(INSERT_SIGHTING,
                sighting.getSuperherovillain().getId(),
                sighting.getLocation().getId(),
                Timestamp.valueOf(sighting.getTimeSighted())); // need to convert datetime to work with sql
        /**
         * uses the last_insert_id mysql function to get the id of the sighting that has just been added to be able to set
         * it in the sighting object
         */
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        sighting.setId(newId);
        /**
         * adds the new location to the superhero object that holds a list of it
         */
        sighting.getSuperherovillain().getLocationsSightedAt().add(sighting.getLocation());
        return sighting;
    }

    /**
     * used to edit the details about a pre-existing sighting within the system
     *
     * @param sighting holds the new updated information about the sighting
     */
    @Override
    public void editSighting(Sighting sighting) {
        try {
            final String UPDATE_SIGHTING = "update superherovillain_location set superid = ?, locationid = ?, "
                    + "timesighted = ? where sightingid = ?";
            jdbc.update(UPDATE_SIGHTING,
                    sighting.getSuperherovillain().getId(),
                    sighting.getLocation().getId(),
                    Timestamp.valueOf(sighting.getTimeSighted()),
                    sighting.getId());
        } catch (DataAccessException e){

        }
    }

    @Override
    public void deleteSighting(int id) {
        try {
            final String DELETE_SUPERHEROVILLAIN_LOCATION = "DELETE FROM superherovillain_location "
                    + "WHERE sightingid = ?";
            jdbc.update(DELETE_SUPERHEROVILLAIN_LOCATION, id);
        } catch (DataAccessException e){

        }
    }

    public static final class SightingMapper implements RowMapper<Sighting> {
        @Override
        public Sighting mapRow(ResultSet rs, int index) throws SQLException {
            Sighting sighting = new Sighting();
            sighting.setId(rs.getInt("sightingId"));
            sighting.setTimeSighted(rs.getTimestamp("timeSighted").toLocalDateTime());
            return sighting;
        }
    }
}
