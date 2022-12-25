package com.company.superherosighting.entities;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Used to represent the various locations that the superhero can be sighted at stored within the database, to be
 * represented as an object within the application
 */
public class Location {
    private int id;
    @NotBlank(message = "Location name cannot be blank")
    @Size(max = 60, message="Location name must be less than 60 characters.")
    private String name;

    @Size(max=300, message = "Hero description must be less than 300 characters.")
    private String description;

    @Size(max=300, message = "Address must be less than 300 characters.")
    private String address;

    @Size(max=100, message = "City must be less than 100 characters.")
    private String city;

    @Size(max=100, message = "Country must be less than 100 characters.")
    private String country;

    @Size(max=15, message = "Postcode must be less than 100 characters.")
    private String postcode;

    @Digits(integer=10, fraction=6, message="Longitude's integral part goes up to 10 digits, and fractal part goes up to 6 ")
    private double longitude;

    @Digits(integer=10, fraction=6, message="latitude's integral part goes up to 10 digits, and fractal part goes up to 6 ")
    private double latitude;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", postcode='" + postcode + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location location)) return false;
        return getId() == location.getId() && Double.compare(location.getLongitude(), getLongitude()) == 0 && Double.compare(location.getLatitude(), getLatitude()) == 0 && Objects.equals(getName(), location.getName()) && Objects.equals(getDescription(), location.getDescription()) && Objects.equals(getAddress(), location.getAddress()) && Objects.equals(getCity(), location.getCity()) && Objects.equals(getCountry(), location.getCountry()) && Objects.equals(getPostcode(), location.getPostcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getAddress(), getCity(), getCountry(), getPostcode(), getLongitude(), getLatitude());
    }
}