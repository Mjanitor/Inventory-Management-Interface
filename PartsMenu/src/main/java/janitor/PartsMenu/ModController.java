package janitor.PartsMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controls the FXML elements for the "Modify Part" window and includes methods and data types to act on that window.
 *
 * FUTURE ENHANCEMENT: I would probably try to include some more details for the In House versus Outsourced toggle group.  Logistically, having more information about it stored
 *     in here could be useful in case of confusion with part shipments
 */
public class ModController implements Initializable {
    // Minimum Inventory textfield
    public TextField modPartMin;
    // MachineID value textfield
    public TextField modMachineID;
    // Maximum Inventory textfield
    public TextField modPartMax;
    // Part cost textfield
    public TextField modPartCost;
    // Current inventory textfield
    public TextField modPartInv;
    // Part Name textfield
    public TextField modPartName;
    // Previously created Part ID textfield
    public TextField modID;
    // Label that changes based on which radio button was selected
    public Label partLocation;
    // Outsourced radio button
    public RadioButton modOutsourced;
    // Toggle Group for status detection
    public ToggleGroup AddToggle;
    // InHouse radio button
    public RadioButton modInHouse;
    // Data for which part was selected for modification
    public Part selection;
    public InHouse selectedInHouse;
    public Outsourced selectedOutsourced;
    // Save Button
    public Button modSave;
    // Cancel Button
    public Button modCancel;
    public Label maxError;
    public Label invError;
    public Label letterError;

    /**
     * Initializing all the imported data for the selected part.
     *
     * RUNTIME ERROR: I had the issue of running into errors whenever I would try to load the ID.  I didn't originally give the object an attribute for it and thought I could simply
     *         have a permanent incrementing value.  Trying to do this caused all kinds of runtime errors, so I realized that attaching all attributes to the main object was a much more
     *         elegant solution.  After making that adjustment in the Parts Class, everything went great.
     *
     * @param url The URL, if any, to pull data from
     * @param resourceBundle Any included resources
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        selection = MainController.getPartSelection();

        // Filling in the textfields with the existing data
        modID.setText(String.valueOf(selection.getId()));
        modPartName.setText(selection.getName());
        modPartInv.setText(String.valueOf(selection.getStock()));
        modPartCost.setText(String.valueOf(selection.getPrice()));
        modPartMax.setText(String.valueOf(selection.getMax()));
        modPartMin.setText(String.valueOf(selection.getMin()));

        if(selection instanceof InHouse){
            AddToggle.selectToggle(modInHouse);
            modMachineID.setText(String.valueOf(((InHouse) selection).getMachineId()));
            partLocation.setText("Machine ID");
        } else {
            AddToggle.selectToggle(modOutsourced);
            modMachineID.setText(String.valueOf(((Outsourced) selection).getCompanyName()));
            partLocation.setText("Outsourced");
        }
    }

    /**
     * Changes the partLocation label based on the selected radio button.
     *
     * @param actionEvent An action event
     */
    public void onHouse(ActionEvent actionEvent) {
        partLocation.setText("Machine ID");
    }

    /**
     * Changes the partLocation label based on the selected radio button.
     *
     * @param actionEvent An action event
     */
    public void onOutsourced(ActionEvent actionEvent) {
        partLocation.setText("Outsourced");
    }

    /**
     * Collects the data entered into the text fields and adds that to the main Parts table, modifying any that were changed.
     *
     * @param actionEvent An action event
     */
    public void onModSave(ActionEvent actionEvent) {
        // Clearing previous errors
        letterError.setVisible(false);
        maxError.setVisible(false);
        invError.setVisible(false);
        // Making sure all fields are filled
        if(modPartName.getText().isEmpty() || modPartCost.getText().isEmpty() || modPartInv.getText().isEmpty() || modPartMin.getText().isEmpty() || modPartMax.getText().isEmpty() || modMachineID.getText().isEmpty()){
            letterError.setVisible(true);
        }
        // Catching cases where there's text in a non-text field
        if((selection.getClass().equals(InHouse.class)) && (!(modPartInv.getText().matches("[0-9]+")) || !(modPartMin.getText().matches("[0-9]+")) || !(modPartMax.getText().matches("[0-9]+")) || !(modPartCost.getText().matches("[0.00-9.99]+")))){
            letterError.setVisible(true);
            return;
        }
        // Catching cases where min > max
        if(Integer.parseInt(modPartMin.getText()) > Integer.parseInt(modPartMax.getText())){
            maxError.setVisible(true);
            return;
        }
        // Catching cases where inv not between max and min
        if(Integer.parseInt(modPartInv.getText()) > Integer.parseInt(modPartMax.getText()) || Integer.parseInt(modPartInv.getText()) < Integer.parseInt(modPartMin.getText())){
            invError.setVisible(true);
            return;
        }
        // Grabbing current data before updating modified product
        int id = selection.getId();
        String name = modPartName.getText();
        Double price = Double.parseDouble(modPartCost.getText());
        int stock = Integer.parseInt(modPartInv.getText());
        int min = Integer.parseInt(modPartMin.getText());
        int max = Integer.parseInt(modPartMax.getText());
        int machineId;
        String companyName;

        if(modInHouse.isSelected()) {
            if(!(modMachineID.getText().matches("[0-9]+"))) {
                letterError.setVisible(true);
                return;
            }
            machineId = Integer.parseInt(modMachineID.getText());
            InHouse newPart = new InHouse(id, name, price, stock, min, max, machineId);
            Inventory.addPart(newPart);
        } else {
            if(modMachineID.getText().matches("[0-9]+")) {
                letterError.setVisible(true);
                return;
            }
            companyName = modMachineID.getText();
            Outsourced newPart = new Outsourced(id, name, price, stock, min, max, companyName);
            Inventory.addPart(newPart);
        }

        Inventory.deletePart(selection);
        Stage stage = (Stage) modCancel.getScene().getWindow();
        stage.close();

        // Reopening main page, which updates the main table
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Scene addScene = new Scene(fxmlLoader.load(), 1263, 450);
            Stage addStage = new Stage();
            addStage.setScene(addScene);
            addStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Closes the modify window, canceling the changes and opens the main menu.
     *
     * @param actionEvent An action event
     * @throws Exception to catch exceptions
     */
    public void onModCancel(ActionEvent actionEvent) throws Exception
    {
        Stage stage = (Stage) modCancel.getScene().getWindow();
        stage.close();

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
            Scene addScene = new Scene(fxmlLoader.load(), 1263, 450);
            Stage addStage = new Stage();
            addStage.setScene(addScene);
            addStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
