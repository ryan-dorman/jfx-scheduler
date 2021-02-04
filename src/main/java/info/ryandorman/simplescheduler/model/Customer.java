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
    private int firstLevelDivisionId;

    public Customer() {
    }

    public Customer(int id, String name, String address, String postalCode, String phone,
                    int firstLevelDivisionId, ZonedDateTime created, String createdBy, ZonedDateTime updated,
                    String updatedBy) {
        super(id, created, createdBy, updated, updatedBy);
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.firstLevelDivisionId = firstLevelDivisionId;
    }

    public Customer(String name, String address, String postalCode, String phone, int firstLevelDivisionId) {
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.firstLevelDivisionId = firstLevelDivisionId;
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

    public int getFirstLevelDivisionId() {
        return firstLevelDivisionId;
    }

    public void setFirstLevelDivisionId(int firstLevelDivisionId) {
        this.firstLevelDivisionId = firstLevelDivisionId;
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
                ", firstLevelDivisionId=" + firstLevelDivisionId +
                '}';
    }
}
