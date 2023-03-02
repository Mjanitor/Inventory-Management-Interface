package janitor.PartsMenu;

/**
 * Extends the Part class to delineate between In-House created parts and Outsourced parts.
 * This generates an Outsourced Part
 */
public class Outsourced extends Part{
    private String companyName;

    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * Sets the company name.
     *
     * @param companyName the company name to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Returns the company name.
     *
     * @return the company name
     */
    public String getCompanyName(){
        return companyName;
    }
}
