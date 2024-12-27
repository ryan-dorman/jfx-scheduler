package info.ryandorman.simplescheduler.model;

import java.time.ZonedDateTime;

/**
 * Holds metadata that exist across entities.
 */
public abstract class Base {
    protected int id;
    protected ZonedDateTime created;
    protected String createdBy;
    protected ZonedDateTime updated;
    protected String updatedBy;

    /**
     * Accepts no parameters to allow for entity creation and dynamic property assignment post-creation.
     */
    public Base() {
    }

    /**
     * Accepts all parameters to allow for population of existing entity metadata.
     *
     * @param id        Unique entity identifier
     * @param created   Date and time entity was created
     * @param createdBy Source that created the entity
     * @param updated   Date and time entity was last updated
     * @param updatedBy Source that updated the entity
     */
    public Base(int id, ZonedDateTime created, String createdBy, ZonedDateTime updated, String updatedBy) {
        this.id = id;
        this.created = created;
        this.createdBy = createdBy;
        this.updated = updated;
        this.updatedBy = updatedBy;
    }

    /**
     * Gets unique entity identifier.
     *
     * @return Unique entity identifier
     */
    public int getId() {
        return id;
    }

    /**
     * Sets unique entity identifier.
     *
     * @param id Unique entity identifier to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets date and time entity was created.
     *
     * @return Date and time entity was created
     */
    public ZonedDateTime getCreated() {
        return created;
    }

    /**
     * Sets date and time entity was created.
     *
     * @param created Date and time to set
     */
    public void setCreated(ZonedDateTime created) {
        this.created = created;
    }

    /**
     * Gets name of the source that created the entity.
     *
     * @return Name of source that created the entity
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * Sets name of the source that created the entity.
     *
     * @param createdBy Name of source to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * Gets date and time entity was last updated.
     *
     * @return Date and time entity was last updated
     */
    public ZonedDateTime getUpdated() {
        return updated;
    }

    /**
     * Sets date and time entity was last updated.
     *
     * @param updated Date and time to set
     */
    public void setUpdated(ZonedDateTime updated) {
        this.updated = updated;
    }

    /**
     * Gets name of the source that updated the entity.
     *
     * @return Name of source that updated the entity
     */
    public String getUpdatedBy() {
        return updatedBy;
    }

    /**
     * Sets name of the source that updated the entity.
     *
     * @param updatedBy Name of source to set
     */
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
