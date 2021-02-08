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
    private FirstLevelDivision division;

    public Customer() {
    }

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

    public Customer(String name, String address, String postalCode, String phone, FirstLevelDivision division) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.division = division;
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

    public FirstLevelDivision getDivision() {
        return division;
    }

    public void setDivision(FirstLevelDivision division) {
        this.division = division;
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
                ", firstLevelDivision=" + division +
                '}';
    }
}
