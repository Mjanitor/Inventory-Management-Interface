package janitor.PartsMenu;

/**
 * Extends the Part class to delineate between In-House created parts and Outsourced parts.
 * This generates an In-House Part
 */
public class InHouse extends Part {
    private int machineId;

    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        this.machineId = machineId;
    }

    /**
     * Sets the machine ID.
     *
     * @param machineId the machine ID to set
     */
    public void setMachineId(int machineId){
        this.machineId = machineId;
    }

    /**
     * Returns the machine ID.
     *
     * @return the machine ID
     */
    public int getMachineId(){
        return machineId;
    }
}
