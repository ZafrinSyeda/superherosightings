package com.company.superherosighting.dao;

import com.company.superherosighting.entities.Organisation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository  //uses this annotation to signify that the class is used to perform CRUD operations on a database
public class OrganisationDaoDB implements OrganisationDao{

    @Autowired
    JdbcTemplate jdbc;

    /**
     * Used to retrieve all the organisations saved within the database
     *
     * @return all the organisations stored
     */
    @Override
    public List<Organisation> getAllOrganisations() {
        final String SELECT_ALL_ORGANISATIONS = "select * from organisation";
        return jdbc.query(SELECT_ALL_ORGANISATIONS, new OrganisationMapper());
    }

    /**
     * Used to retrieve one of the organisations saved within the database represented as an organisation object based
     * on its ID
     *
     * @param id of the organisation to be retrieved
     * @return an object representing the organisation that has the given id
     */
    @Override
    public Organisation getOrganisationById(int id) {
        try {
            final String SELECT_ORGANISATION_BY_ID = "select * from organisation where organisationid = ?";
            return jdbc.queryForObject(SELECT_ORGANISATION_BY_ID, new OrganisationMapper(), id);
        } catch (DataAccessException e) {
            // reaches here if the user tries to access an id that doesn't exist
            return null;
        }
    }

    /**
     * returns the organisation that a certain superhero/ villain is a part of
     *
     * @param superId of the superhero whose organisation you wish to retrieve
     * @return the organisation the superhero/ villain is a part of
     */
    @Override
    public Organisation getOrganisationByHeroVillain(int superId) {
        try {
            final String SELECT_ORGANISATION_BY_HEROVILLAIN =
                    "select org.organisationId, org.name, org.description, org.address, org.city, org.country,\n" +
                            "org.postcode, org.phoneNumber, org.email\n" +
                            "from organisation org \n" +
                            "join superherovillain sh\n" +
                            "on sh.organisationid = org.organisationid\n" +
                            "where sh.superId = ?";
            return jdbc.queryForObject(SELECT_ORGANISATION_BY_HEROVILLAIN, new OrganisationMapper(), superId);

        } catch (DataAccessException e) {
            /* reaches here if the user tries to access an id of a superhero that is not part of an organisation, or
               a superhero id that does not exist
             */
            return null;
        }
    }

    /**
     * used to add a new organisation to the database
     *
     * @param organisation an object holding all the information about a particular organisation to be added to the
     *                     database
     */
    @Transactional //this annotation is needed as multiple queries are performed in this method
    @Override
    public Organisation addOrganisation(Organisation organisation) {
        final String INSERT_ORGANISATION = "insert into organisation(name, description, address, city, country, postcode, phoneNumber, email)\n" +
                "values (?, ?, ?, ?, ?, ?, ?, ?)";
        jdbc.update(INSERT_ORGANISATION,
                organisation.getName(),
                organisation.getDescription(),
                organisation.getAddress(),
                organisation.getCity(),
                organisation.getCountry(),
                organisation.getPostcode(),
                organisation.getPhoneNumber(),
                organisation.getEmail());
        /**
         * uses the last_insert_id mysql function to get the id of the organisation that has just been added to be able to set
         * it in the Organisation object
         */
        int newId = jdbc.queryForObject("SELECT LAST_INSERT_ID()", Integer.class);
        organisation.setId(newId);
        return organisation;
    }

    /**
     * used to edit some details about an organisation
     *
     * @param organisation an object holding the newly edited information of a pre-existing organisation within the
     *                     database
     */
    @Override
    public void editOrganisation(Organisation organisation) {
        final String UPDATE_ORGANISATION = "update organisation set name = ?, description = ?, address = ?, " +
                "city  = ?, country  = ?, postcode  = ?, phoneNumber  = ?, email  = ? where organisationid = ?";
        jdbc.update(UPDATE_ORGANISATION,
                organisation.getName(),
                organisation.getDescription(),
                organisation.getAddress(),
                organisation.getCity(),
                organisation.getCountry(),
                organisation.getPostcode(),
                organisation.getPhoneNumber(),
                organisation.getEmail(),
                organisation.getId());
    }

    /**
     * used to delete a database entry from the database
     *
     * @param id of the database to be deleted from the system
     */
    @Transactional // needed as multiple SQL queries are run
    @Override
    public void deleteOrganisationById(int id) {
        try {
            /**
             * superheroes aren't neccessarily part of an organisation, so deleting an organisation should
             * not delete the superhero from the system, and instead change their organisation to be null
             */
            final String UPDATE_HERO_BY_SETTING_ORGANISATION_TO_NULL =
                    "update superherovillain set organisationid = null where organisationId = ?";
            jdbc.update(UPDATE_HERO_BY_SETTING_ORGANISATION_TO_NULL, id);

            final String DELETE_ORGANISATION_BY_ID = "delete from organisation where organisationId = ?";
            jdbc.update(DELETE_ORGANISATION_BY_ID, id);
        } catch (DataAccessException e) {
            // reaches here if the user tries to access an id of an organisation that doesn't exist
        }
    }

    /**
     * implementation of the RowMapper class for the Organisation table to return Organisation objects
     */
    public static final class OrganisationMapper implements RowMapper<Organisation> {
        @Override
        public Organisation mapRow(ResultSet rs, int index) throws SQLException {
            Organisation org = new Organisation();
            org.setId(rs.getInt("organisationId"));
            org.setName(rs.getString("name"));
            org.setDescription(rs.getString("description"));
            org.setAddress(rs.getString("address"));
            org.setCity(rs.getString("city"));
            org.setCountry(rs.getString("country"));
            org.setPostcode(rs.getString("postcode"));
            org.setPhoneNumber(rs.getString("phonenumber"));
            org.setEmail(rs.getString("email"));
            return org;
        }
    }
}
