package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

public class Customer extends Base {
    private String name;
    private String address;
    private String postalCode;
    private String phone;
    private long firstLevelDivisionId;

    public Customer() {
    }

    public Customer(Long id, String name, String address, String postalCode, String phone, long firstLevelDivisionId) {
        super(id);
        this.name = name;
        this.address = address;
        this.postalCode = postalCode;
        this.phone = phone;
        this.firstLevelDivisionId = firstLevelDivisionId;
    }

    public Customer(String name, String address, String postalCode, String phone, long firstLevelDivisionId) {
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

    public long getFirstLevelDivisionId() {
        return firstLevelDivisionId;
    }

    public void setFirstLevelDivisionId(long firstLevelDivisionId) {
        this.firstLevelDivisionId = firstLevelDivisionId;
    }
}
