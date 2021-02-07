package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

import java.time.ZonedDateTime;

public class Customer extends Base {
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private FirstLevelDivision firstLevelDivision;

    public Customer() {
    }

    public Customer(int id, String name, String address, String postalCode, String phone,
                    FirstLevelDivision firstLevelDivision, ZonedDateTime created, String createdBy, ZonedDateTime updated,
                    String updatedBy) {
        super(id, created, createdBy, updated, updatedBy);
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.firstLevelDivision = firstLevelDivision;
    }

    public Customer(String name, String address, String postalCode, String phone, FirstLevelDivision firstLevelDivision) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.firstLevelDivision = firstLevelDivision;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public FirstLevelDivision getFirstLevelDivision() {
        return firstLevelDivision;
    }

    public void setFirstLevelDivision(FirstLevelDivision firstLevelDivision) {
        this.firstLevelDivision = firstLevelDivision;
    }

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
                ", firstLevelDivision=" + firstLevelDivision +
                '}';
    }
}
