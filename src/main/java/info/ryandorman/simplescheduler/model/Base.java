package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

/**
 * The Base data model includes the shared metadata properties coming from the data layer that exist across
 * entities.
 *
 * @author Ryan Dorman - ID: 001002824
 */
public abstract class Base {
    protected int id;
    protected ZonedDateTime created;
    protected String createdBy;
    protected ZonedDateTime updated;
    protected String updatedBy;

    /**
     * Class Constructor accepts no parameters to allow for dynamic property assignment post-creation.
     */
    public Base() {
    }

    public Base(int id, ZonedDateTime created, String createdBy, ZonedDateTime updated, String updatedBy) {
        this.id = id;
        this.created = created;
        this.createdBy = createdBy;
        this.updated = updated;
        this.updatedBy = updatedBy;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ZonedDateTime getCreated() {
        return created;
    }

    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getUpdated() {
        return updated;
    }

    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
