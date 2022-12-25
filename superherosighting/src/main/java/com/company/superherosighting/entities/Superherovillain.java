package com.company.superherosighting.entities;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.Objects;

/**
 * Used to represent one of the superheroes/ villains within the database as an object to be interacted with on the
 * website
 */
public class Superherovillain {
    private int id;
    @NotBlank(message = "Hero/ Villain name cannot be blank")
    @Size(max = 60, message="Hero/ Villain name must be less than 60 characters.")
    private String name;

    @Size(max=300, message = "Hero description must be less than 300 characters.")
    private String description;

    @Size(max=100, message = "Superpower must be less than 100 characters.")
    private String superpower;
    /**
     * is true if they are listed as a hero, and is false if they are a villain
     */
    private boolean isHero;
    /**
     * represents the organisation that the hero/ villain is a part of - also represents the one-to-many
     * relationship between the superhero and organisations, as it assumes a superhero can only be part of
     * one organisation (but an organisation can have many members)
     */
    private Organisation organisation;
    /**
     * represents the potentially various locations that the superhero/ villain has been sighted at - also
     * represents a many-to-many relationship between the many locations a superhero can be sighted at as well
     * as the fact that multiple superheroes can be sighted at the same location
     */
    private List<Location> locationsSightedAt;

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

    public String getSuperpower() {
        return superpower;
    }

    public void setSuperpower(String superpower) {
        this.superpower = superpower;
    }

    public boolean isHero() {
        return isHero;
    }

    public void setHero(boolean hero) {
        isHero = hero;
    }

    public Organisation getOrganisation() {
        return organisation;
    }

    public void setOrganisation(Organisation organisation) {
        this.organisation = organisation;
    }

    public List<Location> getLocationsSightedAt() {
        return locationsSightedAt;
    }

    public void setLocationsSightedAt(List<Location> locationsSightedAt) {
        this.locationsSightedAt = locationsSightedAt;
    }

    @Override
    public String toString() {
        return "Superherovillain{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", superpower='" + superpower + '\'' +
                ", isHero=" + isHero +
                ", organisation=" + organisation +
                ", locationsSightedAt=" + locationsSightedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Superherovillain that)) return false;
        return getId() == that.getId() && isHero() == that.isHero() && Objects.equals(getName(), that.getName()) && Objects.equals(getDescription(), that.getDescription()) && Objects.equals(getSuperpower(), that.getSuperpower()) && Objects.equals(getOrganisation(), that.getOrganisation()) && Objects.equals(getLocationsSightedAt(), that.getLocationsSightedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getSuperpower(), isHero(), getOrganisation(), getLocationsSightedAt());
    }
}
