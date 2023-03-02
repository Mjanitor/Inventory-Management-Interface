package janitor.PartsMenu;

import javafx.application.Platform;
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
 * Controls the FXML elements for the main application window and includes methods and data types to act on that window.
 *
 * FUTURE ENHANCEMENT: One thing that wasn't asked of me but would be cool to implement is filtering without having to input the "enter" key.  I think it's much sleeker to have
    your results filter as you type, but the way it is now is okay.  That's just the thing that most jumped out at me when going through the functionality.
 */
public class MainController implements Initializable {

    // Parts Table
    public TableView<Part> partsTable;
    // Products Table
    public TableView<Product> productsTable;
    // Product Part ID
    public TableColumn prPartID;
    // Product Part Name
    public TableColumn prPartName;
    // Product Inventory Level
    public TableColumn priLevel;
    // Part ID
    public TableColumn paPartID;
    // Part Name
    public TableColumn paPartName;
    // Part Inventory Level
    public TableColumn paiLevel;
    // Part Price Per Unit
    public TableColumn pappu;
    // Product Price Per Unit
    public TableColumn prppu;
    // Part Search Textfield
    public TextField partSearch;
    // Product Search Textfield
    public TextField productSearch;
    // Part Delete Button
    public Button deleteButton1;
    // Product Delete Button
    public Button deleteButton2;
    // Part Add Button
    public Button addButton1;
    // Product Add Button
    public Button addButton2;
    // Part Mod Button
    public Button modButton1;
    // Product Mod Button
    public Button prodMod;
    // Exit Button
    public Button quit;

    /**
     * Temporary List to hold working parts
     */
    public ObservableList<Part> parts = FXCollections.observableArrayList();

    /**
     * Temporary List to hold working products
     */
    public ObservableList<Product> products = FXCollections.observableArrayList();

    /**
     * Temporary List to hold parts from another class
     */
    private static ObservableList<Part> tempParts = null;

    /**
     * Temporary List to hold products from another class
     */
    private static ObservableList<Product> tempProducts = null;

    /**
     * Temporary Table to hold Part values to be added to a main table
     */
    private static TableView<Part> tempPartsTable = null;

    /**
     * Temporary Table to hold Product values to be added to a main table
     */
    private static TableView<Product> tempProductsTable = null;

    /**
     * Holds the values of the highlighted part
     */
    private static Part selectedPart; // Grabs the currently highlighted item to be modified

    /**
     * Holds the values of the highlighted product
     */
    private static Product selectedProduct; // Grabs the currently highlighted item to be modified

    /**
     * Error display for Part Modify Button
     */
    public Label modLabel;

    /**
     * Error display for Product Modify Button
     */
    public Label modLabel2;

    /**
     * Loads all initial data pertaining to the Tables/Columns.
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
        ObservableList<Part> parts = Inventory.getAllParts();
        partsTable.setItems(parts);
        ObservableList<Product> products = Inventory.getAllProducts();
        productsTable.setItems(products);
    }

    /**
     * Returns the Parts Table.
     *
     * @return the parts table
     */
    public static TableView<Part> getPartsTable() {
        return tempPartsTable;
    }

    /**
     * Returns the Products Table.
     *
     * @return the products table
     */
    public static TableView<Product> getProductsTable() {
        return tempProductsTable;
    }

    /**
     * Returns the selected Part.
     *
     * @return the selected part
     */
    public static Part getPartSelection() {
        return selectedPart;
    }

    /**
     * Returns the selected Product.
     *
     * @return the selected product
     */
    public static Product getProductSelection(){ return selectedProduct;}

    /**
     * Returns the list of temporary parts to be manipulated.
     *
     * @return the list of temporary parts
     */
    public static ObservableList<Part> getParts() {
        return tempParts;
    }

    /**
     * Returns the list of temporary products to be manipulated.
     *
     * @return the list of temporary products
     */
    public static ObservableList<Product> getProducts() {
        return tempProducts;
    }

    /**
     * Gets the text currently typed into the Parts search bar and filters the parts table results to match.
     *
     * @param actionEvent An event action
     */
    public void onPartSearch(ActionEvent actionEvent) {
        String q = partSearch.getText();

        ObservableList<Part> results = Inventory.lookupPart(q); // Temp list to hold the items that match the entered text

        if(results.size() == 0){
            Alert noneFound = new Alert(Alert.AlertType.ERROR);
            noneFound.setTitle("Results");
            noneFound.setHeaderText("No Results Found");
            noneFound.showAndWait();
        } else {
            partsTable.setItems(results);
        }
    }

    /**
     * Gets the text currently typed into the Products search bar and filters the parts table results to match.
     *
     * @param actionEvent An event action
     */
    public void onProductSearch(ActionEvent actionEvent) {
        String q = productSearch.getText();

        ObservableList<Product> results = Inventory.lookupProduct(q); // Temp list to hold the items that match the entered text

        if(results.size() == 0){
            Alert noneFound = new Alert(Alert.AlertType.ERROR);
            noneFound.setTitle("Results");
            noneFound.setHeaderText("No Results Found");
            noneFound.showAndWait();
        } else {
            productsTable.setItems(results);
        }
    }

    /**
     * Loads the "Add Parts" Screen.
     *
     * @param event An action event
     * @throws Exception to catch exceptions
     */
    public void add1ButtonPress(ActionEvent event) throws Exception {
        tempParts = parts; // Setting the temporary parts list equal to the current state of the parts data structure

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add.fxml"));
            Scene addScene = new Scene(fxmlLoader.load(), 650, 454);
            Stage addStage = new Stage();
            addStage.setScene(addScene);
            addStage.show();
            // Closing the main screen
            Stage stage = (Stage) addButton2.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the "Add Products" Screen.
     *
     * @param event An action event
     * @throws Exception to catch exceptions
     */
    public void add2ButtonPress(ActionEvent event) throws Exception {
        tempProducts = products; // Setting the temporary products list equal to the current state of the products data structure

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("productsAdd.fxml"));
            Scene addScene = new Scene(fxmlLoader.load(), 1000, 615);
            Stage addStage = new Stage();
            addStage.setScene(addScene);
            addStage.show();
            // Closing the main screen
            Stage stage = (Stage) addButton2.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the "Modify Parts" screen. RUNTIME ERROR:  The big issue I had here was that I would constantly have errors when nothing was selected.  It was because it was trying to modify a null value, so it
     *         made total sense, I just had to figure a way around it.  My thinking was that if an end user messes up and forgets to select something, they'll have no idea why the
     *         application just stopped working.  To that end, I made a very visible label that will come up and inform the user of the problem.
     *
     * @param event An action event
     * @throws Exception to catch exceptions
     */
    public void modButtonPress(ActionEvent event) throws Exception {
        tempPartsTable = partsTable; // Setting the temporary table equal to the current state of the partsTable data structure
        selectedPart = partsTable.getSelectionModel().getSelectedItem(); // Grabs the currently highlighted item to be modified
        boolean isInHouse = false;

        try {
            // Error handler
            if (partsTable.getSelectionModel().getSelectedItem() == null) {
                modLabel.setVisible(true);
                return;
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("modify.fxml"));
            Scene addScene = new Scene(fxmlLoader.load(), 650, 454);
            Stage addStage = new Stage();
            addStage.setScene(addScene);
            addStage.show();
            // Closing the main screen
            Stage stage = (Stage) modButton1.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the "Modify Products" screen.
     *
     * @param event An action event
     * @throws Exception to catch exceptions
     */
    public void prodModButtonPress(ActionEvent event) throws Exception {
        tempPartsTable = partsTable;
        tempProductsTable = productsTable; // Setting the temporary table equal to the current state of the productsTable data structure
        selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        try {
            // Error handler
            if (selectedProduct == null) {
                modLabel2.setVisible(true);
                return;
            }

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("prodModify.fxml"));
            Scene addScene = new Scene(fxmlLoader.load(), 1000, 615);
            Stage addStage = new Stage();
            addStage.setScene(addScene);
            addStage.show();
            // Closing the main screen
            Stage stage = (Stage) modButton1.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the Parts "Confirm Delete" window to remove a Part.
     *
     * @param event An action event
     */
    public void deleteButtonPress(ActionEvent event) {
        tempPartsTable = partsTable; // Setting the temporary table equal to the current state of the partsTable data structure
        selectedPart = partsTable.getSelectionModel().getSelectedItem(); // Grabs the currently highlighted item to be modified
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);

        // Error handler
        if (selectedPart == null) {
            modLabel.setVisible(true);
            return;
        } else {
            confirmation.setTitle("Confirm");
            confirmation.setContentText("Are you sure you want to delete?");
            Optional<ButtonType> choice = confirmation.showAndWait();
            if(choice.get() == ButtonType.OK){
                tempPartsTable.getItems().remove(selectedPart);
            }
        }
    }

    /**
     * Opens the Products "Confirm Delete" window to remove a Product.
     *
     * @param event An action event
     */
    public void deleteButton2Press(ActionEvent event) {
        tempProductsTable = productsTable; // Setting the temporary table equal to the current state of the partsTable data structure
        selectedProduct = productsTable.getSelectionModel().getSelectedItem(); // Grabs the currently highlighted item to be modified
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        Alert error = new Alert(Alert.AlertType.ERROR);

        // Error handler
        if (selectedProduct == null) {
            modLabel2.setVisible(true);
            return;
        }

        if(!(selectedProduct.getAllAssociatedParts().isEmpty())){
            error.setTitle("Error");
            error.setHeaderText("Parts Associated");
            error.setContentText("Please remove any associated Parts before deleting this Product");
            error.showAndWait();
        } else {
            confirmation.setTitle("Confirm");
            confirmation.setContentText("Are you sure you want to delete?");
            Optional<ButtonType> choice = confirmation.showAndWait();
            if(choice.get() == ButtonType.OK){
                tempProductsTable.getItems().remove(selectedProduct);
            }
        }
    }

    /**
     * Handling the "Exit Button" to close the application.
     *
     * @param actionEvent An action event
     */
    public void closeApplication(ActionEvent actionEvent) {
        Platform.exit();
    }
}