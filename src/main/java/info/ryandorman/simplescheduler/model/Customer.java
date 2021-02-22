package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

/**
 * Holds Customer data.
 */
public class Customer extends Base {
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private FirstLevelDivision division;

    /**
     * Accepts no parameters to allow for Customer creation and dynamic property assignment post-creation.
     */
    public Customer() {
    }

    /**
     * Accepts all parameters to allow for population of existing Customer data.
     * @param id Unique identifier
     * @param name First and lLast Name of Customer
     * @param address Street address of Customer (e.g., 123 ABC Street, White Plains)
     * @param postalCode Postal Code of Customer's address
     * @param phone Phone number of customer
     * @param division Region or State of Customer
     * @param created Date and time Customer was created
     * @param createdBy Source that created Customer
     * @param updated Date and time Customer was last updated
     * @param updatedBy Source that updated Customer
     */
    public Customer(int id, String name, String address, String postalCode, String phone,
                    FirstLevelDivision division, ZonedDateTime created, String createdBy, ZonedDateTime updated,
                    String updatedBy) {
        super(id, created, createdBy, updated, updatedBy);
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
    }

    /**
     * Gets the first and last Name of Customer.
     * @return First and last Name of Customer
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the first and last Name of Customer.
     * @param name First and last Name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the street address of Customer.
     * @return Street address of Customer
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets the street address of Customer.
     * @param address Street address to set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets postal Code of Customer's address.
     * @return Postal Code of Customer
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * Sets postal Code of Customer's address.
     * @param postalCode Postal code to set
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * Gets phone number of customer.
     * @return Phone number of customer
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets phone number of customer.
     * @param phone Phone number to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * Gets Region or State of Customer.
     * @return Region or State of Customer
     */
    public FirstLevelDivision getDivision() {
        return division;
    }

    /**
     * Sets Region or State of Customer.
     * @param division Region or State to set
     */
    public void setDivision(FirstLevelDivision division) {
        this.division = division;
    }

    /**
     * Provides meaningful string representation of Customer properties and their values.
     * @return String representation of Customer properties
     */
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", created=" + created +
                ", createdBy='" + createdBy + '\'' +
                ", updated=" + updated +
                ", updatedBy='" + updatedBy + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", phone='" + phone + '\'' +
                ", firstLevelDivision=" + division +
                '}';
    }
}
