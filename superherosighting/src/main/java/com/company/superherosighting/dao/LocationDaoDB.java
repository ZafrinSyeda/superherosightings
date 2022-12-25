package com.company.superherosighting.dao;

import com.company.superherosighting.entities.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of the LocationDao interface, to be used with a MySql Database
 */
@Repository //uses this annotation to signify that the class is used to perform CRUD operations on a database
public class LocationDaoDB implements  LocationDao{

    @Autowired
    JdbcTemplate jdbc;

    /**
     * Performs a Read operation in order to retrieve all the locations within the database
     *
     * @return all the locations stored within the database
     */
    @Override
    public List<Location> getAllLocations() {
        final String SELECT_ALL_LOCATIONS = "select * from location";
        return jdbc.query(SELECT_ALL_LOCATIONS, new LocationMapper());
    }

    /**
     * Retrieves a single location based on the ID of that location passed in
     *
     * @param id of the location to be retrieved
     * @return location object with the given id
     */
    @Override
    public Location getLocationById(int id) {
        try {
            final String SELECT_LOCATION_BY_ID = "select * from location where locationId = ?";
            return jdbc.queryForObject(SELECT_LOCATION_BY_ID, new LocationMapper(), id);
        } catch (DataAccessException e) {
            // reaches here if the user tries to access an id of a location that does not exist
            return null;
        }
    }

    /**
     * used to add a new location to the database
     *
     * @param location object that holds all the information to be added to the database
     */
    @Transactional //this annotation is needed as multiple queries are performed in this method
    @Override
    public Location addLocation(Location location) {
        final String INSERT_LOCATION = "insert into location(name, description, address, city, country, postcode, longitude, latitude) \n" +
                "values (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbc.update(INSERT_LOCATION,
                location.getName(),
                location.getDescription(),
                location.getAddress(),
                location.getCity(),
                location.getCountry(),
                location.getPostcode(),
                location.getLongitude(),
                location.getLatitude());
        /**
         * uses the last_insert_id mysql function to get the id of the location that has just been added to be able to set
         * it in the location object
         */
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        location.setId(newId);
        return location;
    }

    /**
     * used to edit the details of a location saved within the database
     *
     * @param location the location object with the necessary changes already in place to be saved to the database
     */
    @Override
    public void editLocation(Location location) {
        final String UPDATE_LOCATION = "update location set name = ?, description = ?, address = ? , city = ?, " +
                "country = ?, postcode = ?, longitude = ?, latitude = ? where locationid = ?";
        jdbc.update(UPDATE_LOCATION,
                location.getName(),
                location.getDescription(),
                location.getAddress(),
                location.getCity(),
                location.getCountry(),
                location.getPostcode(),
                location.getLongitude(),
                location.getLatitude(),
                location.getId());
    }

    /**
     * used to delete a location from the database based on its ID
     *
     * @param id of the location to be removed from the database
     */
    @Override
    @Transactional
    public void deleteLocationById(int id) {
        /**
         * need to delete the entry from the bridge table superherovillain_location that tracks which location
         * a superhero/ villain has been sighted as this location no longer exists
         */
        final String DELETE_SUPERHEROVILLAIN_LOCATION = "DELETE FROM superherovillain_location "
                + "WHERE locationId = ?";
        jdbc.update(DELETE_SUPERHEROVILLAIN_LOCATION, id);

        /**
         * after this processing the location itself can be deleted
         */
        final String DELETE_LOCATION = "DELETE FROM location WHERE locationid = ?";
        jdbc.update(DELETE_LOCATION, id);
    }

    /**
     * implementation of the RowMapper class for the location table to return Location objects
     */
    public static final class LocationMapper implements RowMapper<Location> {
        @Override
        public Location mapRow(ResultSet rs, int index) throws SQLException {
            Location location = new Location();
            location.setId(rs.getInt("locationId"));
            location.setName(rs.getString("name"));
            location.setDescription(rs.getString("description"));
            location.setAddress(rs.getString("address"));
            location.setCity(rs.getString("city"));
            location.setCountry(rs.getString("country"));
            location.setPostcode(rs.getString("postcode"));
            location.setLongitude(rs.getDouble("longitude"));
            location.setLatitude(rs.getDouble("latitude"));
            return location;
        }
    }
}
