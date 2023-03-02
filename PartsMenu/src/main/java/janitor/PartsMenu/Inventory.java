package janitor.PartsMenu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * The containing class for the application's current inventory.
 *
 * It houses the master list for Parts and Products and contains methods for manipulating those lists.
 */
public class Inventory {

    /**
     * The total list of Parts that can be iterated upon.
     *
     * It begins with starter data that simulates a small set of inventory parts
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList(new InHouse(1, "Brakes", 15, 10, 1, 10, 555),
            new InHouse(2, "Wheel", 11, 16, 1, 10, 666), new InHouse(3, "Seat", 15, 10, 1, 10, 777));

    /**
     * The total list of Products that can be iterated upon.
     *
     * It begins with starter data that simulates a small set of inventory products
     */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList(new Product(1000, "Giant Bike", 299.99, 5, 1, 5),
            new Product(1001, "Tricycle", 99.99, 3, 1, 10));

    /**
     * Adds a part to the master list.
     *
     * @param newPart the part to be added
     */
    public static void addPart(Part newPart){
        allParts.add(newPart);
    }

    /**
     * Adds a product to the master list.
     *
     * @param newProduct the product to be added
     */
    public static void addProduct(Product newProduct){
        allProducts.add(newProduct);
    }

    /**
     * Searches for a specific part based upon the Part ID supplied.
     *
     * @param partId the Part ID to reference
     * @return the part that was found, if any
     */
    public static Part lookupPart(int partId){
        for(Part p: allParts){
            if(partId == p.getId()){
                return p;
            }
        }
        return null;
    }

    /**
     * Searches for a specific product based upon the Product ID supplied.
     *
     * @param productId the Product ID to reference
     * @return the Product that was found, if any
     */
    public static Product lookupProduct(int productId){
        for(Product p: allProducts){
            if(productId == p.getId()){
                return p;
            }
        }
        return null;
    }

    /**
     * Searches the master list of Parts to find any case-insensitive, partial matches.
     *
     * @param partName the part name to search for
     * @return the list of parts that match the input, if any
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> namedParts = FXCollections.observableArrayList(); // Temporary holder list

        // Checking for partial matches
        for (Part p : allParts) {
            if (p.getName().toLowerCase().contains(partName.toLowerCase())) {
                namedParts.add(p);
            }
        }
        return namedParts;
    }

    /**
     * Searches the master list of Products to find any case-insensitive, partial matches.
     *
     * @param productName the product name to search for
     * @return the list of products that match the input, if any
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> namedProducts = FXCollections.observableArrayList(); // Temporary holder list

        // Checking for partial matches
        for (Product p : allProducts) {
            if (p.getName().toLowerCase().contains(productName.toLowerCase())) {
                namedProducts.add(p);
            }
        }
        return namedProducts;
    }

    /**
     * Updates the existing part based upon its index.
     *
     * @param index the Part index to reference
     * @param selectedPart the Part to update
     */
    public static void updatePart(int index, Part selectedPart){
        allParts.set(index, selectedPart);
    }

    /**
     * Updates the existing product based upon its index.
     *
     * @param index the Product index to reference
     * @param newProduct the Product to update
     */
    public static void updateProduct(int index, Product newProduct){
        allProducts.set(index, newProduct);
    }

    /**
     * Deletes a Part from the master list.
     *
     * @param selectedPart the Part to delete
     * @return a boolean of whether the Part was successfully removed
     */
    public static boolean deletePart(Part selectedPart){
        if(allParts.contains(selectedPart)){
            allParts.remove(selectedPart);
            return true;
        }
        return false;
    }

    /**
     * Deletes a Product from the master list.
     *
     * @param selectedProduct the Product to delete
     * @return a boolean of whether the Product was successfully removed
     */
    public static boolean deleteProduct(Product selectedProduct){
        if(allProducts.contains(selectedProduct)){
            allProducts.remove((selectedProduct));
            return true;
        }
        return false;
    }

    /**
     * Returns the master list of all parts.
     *
     * @return the master list of all parts
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * Returns the master list of all products.
     *
     * @return the master list of all products
     */
    public static ObservableList<Product> getAllProducts(){
        return allProducts;
    }
}
