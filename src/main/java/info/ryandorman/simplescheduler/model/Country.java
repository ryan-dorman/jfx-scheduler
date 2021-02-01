package info.ryandorman.simplescheduler.model;

/*
 *   Ryan Dorman
 *   ID: 001002824
 */

public class Country extends Base {
    private String name;

    public Country() {
    }

    public Country(Long id, String name) {
        super(id);
        this.name = name;
    }

    public Country(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
