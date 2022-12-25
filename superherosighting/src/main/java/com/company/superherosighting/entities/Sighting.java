package com.company.superherosighting.entities;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * An object that will represent any entities within a bridge table that denotes where and when a superhero or villain
 * was sighted
 */
public class Sighting {
    private int id;
    private Superherovillain superherovillain;
    private Location location;
    private LocalDateTime timeSighted; //separate object created for the bridge table as it contains this unique attribute

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Superherovillain getSuperherovillain() {
        return superherovillain;
    }

    public void setSuperherovillain(Superherovillain superherovillain) {
        this.superherovillain = superherovillain;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public LocalDateTime getTimeSighted() {
        return timeSighted;
    }

    public void setTimeSighted(LocalDateTime timeSighted) {
        this.timeSighted = timeSighted;
    }

    @Override
    public String toString() {
        return "Sighting{" +
                "id=" + id +
                ", superherovillain=" + superherovillain +
                ", location=" + location +
                ", timeSighted=" + timeSighted +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sighting sighting)) return false;
        return getId() == sighting.getId() && Objects.equals(getSuperherovillain(), sighting.getSuperherovillain()) && Objects.equals(getLocation(), sighting.getLocation()) && Objects.equals(getTimeSighted(), sighting.getTimeSighted());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSuperherovillain(), getLocation(), getTimeSighted());
    }
}
