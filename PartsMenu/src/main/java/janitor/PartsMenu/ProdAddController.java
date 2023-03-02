package janitor.PartsMenu;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Controls the FXML elements for the "Add Product" window and includes methods and data types to act on that window.
 *
 * FUTURE ENHANCEMENT: I might add some slight user instruction to the tables area, potentially with some error catching.  It can be a little daunting to look at and not fully
 *     understandable what each part is doing if you're an end-user.
 */
public class ProdAddController implements Initializable{

    // Parts Table
    public TableView<Part> partsTable;
    // Products Table
    public TableView<Part> productsTable;
    // Product Part ID
    public TableColumn prPartID;
    // Product Part Name
    public TableColumn prPartName;
    // Product Inventory Level
    public TableColumn priLevel;
    // Product Price per Unit
    public TableColumn pappu;
    // Parts Table Part ID
    public TableColumn paPartID;
    // Parts Table Part name
    public TableColumn paPartName;
    // Parts Table inventory level
    public TableColumn paiLevel;
    // Parts Table Price per Unit
    public TableColumn prppu;

    /**
     * Temporary List to hold working parts
     */
    public ObservableList<Part> parts = FXCollections.observableArrayList();

    /**
     * Temporary List to hold working products
     */
    public ObservableList<Part> products = FXCollections.observableArrayList();
    // Part ID textfield
    public TextField partID;
    // Part name textfield
    public TextField partName;
    // Part Inventory textfield
    public TextField partInv;
    // Part Cost textfield
    public TextField partCost;
    // Part Max Inventory textfield
    public TextField partMax;
    // Part Min Inventory textfield
    public TextField partMin;
    // Search bar textfield
    public TextField addProdSearch;
    // Cancel Button
    public Button addCancel;
    // Add button to copy from the upper parts table to the lower products table
    public Button prodAdd;

    /**
     * Captures which item is selected for the purpose of adding and removing from the lower table
     */
    private static Part selectedPart = null;
    // Integer to auto-increment the product ID
    public int productID = 1002;
    // Save Button
    public Button prodAddSave;
    // Remove button - for lower products table
    public Button prodAddRemove;
    public Label maxError;
    public Label invError;
    public Label letterError;

    /**
     * Assigns all imported data upon initialization, including the tables and text fields.
     *
     * @param url The URL, if any, to pull data from
     * @param resourceBundle Any included resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Loads Initial Table values for the various Parts columns
        paPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        paPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        paiLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        pappu.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Loads Initial Table values for the various Products columns
        prPartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        prPartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        priLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        prppu.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Grabs and loads all table data
        partsTable.setItems(Inventory.getAllParts());
    }

    /**
     * Gets the text currently typed into the Parts search bar and filters the parts table results to match.
     *
     * @param actionEvent An action event
     */
    public void onPartSearch(ActionEvent actionEvent) {
        String q = addProdSearch.getText();

        ObservableList<Part> results = Inventory.lookupPart(q); // Temp list to hold the items that match the entered text

        if(results.size() == 0) {
            Alert noneFound = new Alert(Alert.AlertType.ERROR);
            noneFound.setTitle("Results");
            noneFound.setHeaderText("No Results Found");
            noneFound.showAndWait();
        } else {
            partsTable.setItems(results);
        }
    }

    /**
     * Adds the selected part to the product table to be saved for later.
     *
     * RUNTIME ERROR: I experienced a good number of errors here, specifically null pointer exceptions.  This was due to trying to add data from one table to another without being
     *         too familiar with the data objects needed to be passed.  It wasn't until I fully understood how to assess and pass appropriate data types that I was able to succeed
     *
     * @param actionEvent An action event
     */
    public void onProdAdd(ActionEvent actionEvent) {
        selectedPart = partsTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null){
            return;
        }

        products.add(selectedPart);
        productsTable.setItems(products); // Displaying the added part
    }

    /**
     * Removes the selected part that was added to the bottom "Products" table.
     *
     * @param actionEvent An action event
     */
    public void onProdAddRemove(ActionEvent actionEvent) {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);

        selectedPart = productsTable.getSelectionModel().getSelectedItem();

        confirmation.setTitle("Confirm");
        confirmation.setContentText("Are you sure you want to delete?");
        Optional<ButtonType> choice = confirmation.showAndWait();
        if(choice.get() == ButtonType.OK){
            productsTable.getItems().remove(selectedPart);
        }
    }

    /**
     * Collects the entered product information, including the Products table with its associated "Added" parts, and passes it back to the main Products table.
     *
     * @param actionEvent An action event
     * @throws Exception to catch exceptions
     */
    public void onProdAddSave(ActionEvent actionEvent) throws Exception {
        // Clearing previous errors
        maxError.setVisible(false);
        invError.setVisible(false);

        // Making sure all fields are filled
        if(partName.getText().isEmpty() || partCost.getText().isEmpty() || partInv.getText().isEmpty() || partMax.getText().isEmpty() || partMin.getText().isEmpty()){
            letterError.setVisible(true);
        }

        // Catching cases where there's text in a non-text field
        if(!(partInv.getText().matches("[0-9]+")) || !(partMax.getText().matches("[0-9]+")) || !(partMin.getText().matches("[0-9]+")) || !(partCost.getText().matches("[0.00-9.99]+"))){
            return;
        }

        // Catching cases where min > max
        if(Integer.parseInt(partMin.getText()) > Integer.parseInt(partMax.getText())){
            maxError.setVisible(true);
            return;
        }

        // Catching cases where inv not between max and min
        if(Integer.parseInt(partInv.getText()) > Integer.parseInt(partMax.getText()) || Integer.parseInt(partInv.getText()) < Integer.parseInt(partMin.getText())){
            invError.setVisible(true);
            return;
        }

        // Grabbing current data before updating modified product
        int id = productID++;
        String name = partName.getText();
        Double price = Double.parseDouble(partCost.getText());
        int stock = Integer.parseInt(partInv.getText());
        int min = Integer.parseInt(partMin.getText());
        int max = Integer.parseInt(partMax.getText());

        // Making a new object
        Product tempProduct = new Product(id, name, price, stock, min, max);

        // Adding any associated parts
        for(Part p : products){
            tempProduct.addAssociatedPart(p);
        }

        Inventory.addProduct(tempProduct);
        Stage stage = (Stage) addCancel.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Scene addScene = new Scene(fxmlLoader.load(), 1263, 450);
            Stage addStage = new Stage();
            addStage.setScene(addScene);
            addStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the Add window and opens the main menu.
     *
     * @param actionEvent An action event
     */
    public void onCancel(ActionEvent actionEvent) {
        {
            Stage stage = (Stage) addCancel.getScene().getWindow();
            stage.close();

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
                Scene addScene = new Scene(fxmlLoader.load(), 1263, 450);
                Stage addStage = new Stage();
                addStage.setScene(addScene);
                addStage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
