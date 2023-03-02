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
 * Controls the FXML elements for the "Modify Product" window and includes methods and data types to act on that window.
 *
 * FUTURE ENHANCEMENT: Automatic display of stats related to the items associated parts list would be helpful.  That way you could have easier access to information when evaluating
 *     how worthwhile the product is.  It could help make purchasing decisions and that way you could better align your goals with your finances
 */
public class ProdModController implements Initializable {
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
    /**
     * Add button to copy from the upper parts table to the lower products table
     */
    public Button prodAdd;
    /**
     * Captures which item is selected for the purpose of adding and removing from the lower table
     */
    private static Part selectedPart = null;
    // Save Button
    public Button prodAddSave;
    // Remove button - for lower products table
    public Button prodAddRemove;
    // Grabs the selected product to be modified
    public Product selection;
    public Label maxError;
    public Label invError;
    public Label letterError;

    /**
     * Main initialize function that loads the data to be modified, including the table values, the Object data, and its previous state.
     *
     * RUNTIME ERROR: I had a very difficult time trying to properly instantiate the data for the Products Table when using baseline, empty Data table values.  The issue was
     *         that when you have a preset amount of items that don't have explicit parts associated with them, you try and initialize them, and you get an error stating that it tried
     *         to load null data tables.  To fix that, I had to add another data attribute to the Parts object and then give it something so that the table wouldn't fail to load
     *
     * @param url The URL, if any, to pull data from
     * @param resourceBundle Any included resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selection = MainController.getProductSelection();

        partID.setText(String.valueOf(selection.getId()));
        partName.setText(selection.getName());
        partInv.setText(String.valueOf(selection.getStock()));
        partCost.setText(String.valueOf(selection.getPrice()));
        partMax.setText(String.valueOf(selection.getMax()));
        partMin.setText(String.valueOf(selection.getMin()));

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
        parts = Inventory.getAllParts();
        partsTable.setItems(parts);
        products = selection.getAllAssociatedParts();
        productsTable.setItems(products);
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
     * @param actionEvent An action event
     */
    public void onProdAdd(ActionEvent actionEvent) {
        selectedPart = partsTable.getSelectionModel().getSelectedItem();

        if(selectedPart == null){
            return;
        }
        products.add(selectedPart);
        productsTable.setItems(products);
    }

    /**
     * Removes the selected part that was added to the bottom "Products" table.
     *
     * @param actionEvent An action event
     */
    public void onProdAddRemove(ActionEvent actionEvent){
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
     * @return the modified products table
     * @throws Exception to catch exceptions
     */
    public TableView<Part> onProdAddSave(ActionEvent actionEvent) throws Exception {
        // Clearing previous errors
        letterError.setVisible(false);
        maxError.setVisible(false);
        invError.setVisible(false);
        // Making sure all fields are filled
        if(partName.getText().isEmpty() || partCost.getText().isEmpty() || partInv.getText().isEmpty() || partMax.getText().isEmpty() || partMin.getText().isEmpty()){
            letterError.setVisible(true);
        }
        // Catching cases where there's text in a non-text field
        if(!(partInv.getText().matches("[0-9]+")) || !(partMax.getText().matches("[0-9]+")) || !(partMin.getText().matches("[0-9]+")) || !(partCost.getText().matches("[0.00-9.99]+"))){
            letterError.setVisible(true);
            return null;
        }
        // Catching cases where min > max
        if(Integer.parseInt(partMin.getText()) > Integer.parseInt(partMax.getText())){
            maxError.setVisible(true);
            return null;
        }
        // Catching cases where inv not between max and min
        if(Integer.parseInt(partInv.getText()) > Integer.parseInt(partMax.getText()) || Integer.parseInt(partInv.getText()) < Integer.parseInt(partMin.getText())){
            invError.setVisible(true);
            return null;
        }
        // Grabbing current data before updating modified product
        int id = selection.getId();
        String name = partName.getText();
        Double price = Double.parseDouble(partCost.getText());
        int stock = Integer.parseInt(partInv.getText());
        int min = Integer.parseInt(partMin.getText());
        int max = Integer.parseInt(partMax.getText());

        Product tempProduct = new Product(id, name, price, stock, min, max);

        for(Part p : products){
            tempProduct.addAssociatedPart(p);
        }

        if(Inventory.deleteProduct(selection)){
            Inventory.addProduct(tempProduct);
        }

        // Closing the modify window
        Stage stage = (Stage) addCancel.getScene().getWindow();
        stage.close();

        // Opening the main menu
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Scene addScene = new Scene(fxmlLoader.load(), 1263, 450);
            Stage addStage = new Stage();
            addStage.setScene(addScene);
            addStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }

        return productsTable;
    }

    /**
     * Closes the Modify window and opens the main menu.
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
