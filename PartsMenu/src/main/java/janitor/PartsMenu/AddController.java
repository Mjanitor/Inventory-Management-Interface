package janitor.PartsMenu;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * Controls the FXML elements for the "Add Part" window and includes methods and data types to act on that window.
 *
 * FUTURE ENHANCEMENT: I think having the Add Part form so separate from the main display makes it hard to refer to what information you're currently working with.  I think,
    similar to the Add Product form, I would have a Table that displays your current parts inside the Add Part form
 */
public class AddController {

    // InHouse radio button
    public RadioButton inHouse;
    // Toggle Group for status detection
    public ToggleGroup AddToggle;
    // Outsourced radio button
    public RadioButton outsourced;
    // Changeable label based on which radio button is selected
    public Label partLocation;
    // Part Name textfield
    public TextField partName;
    // Part Inventory textfield
    public TextField partInv;
    // Part Cost textfield
    public TextField partCost;
    // Part Max amount textfield
    public TextField partMax;
    // Part Minimum textfield
    public TextField partMin;
    // Part machine ID textfield
    public TextField machineID;
    // Starting point for the auto-generating part ID number
    public int partID = 4;
    // Cancel button
    public Button addCancel;
    // Integer that will be either a 0 or 1 based on which radio button is selected
    public int toggleStatus;
    public Label maxError;
    public Label invError;
    public Label letterError;

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
     * Collects the data entered into the text fields and adds that to the main Parts table.
     *
     * RUNTIME ERROR: When I first tried running this, it wouldn't actually update the table.  So I tried fighting with it, trying Main controller refreshing of the table, but it still
        didn't work out quite how I wanted it to.  I googled around a bit and realized that the Table is refreshed whenever the FXML is initialized.  So instead, I just made sure the
        Main window closes when I open the "Add Part" and then it would handle that functionality itself
     *
     * @param actionEvent An action event
     * @throws Exception to catch exceptions
     */
    public void savePart(ActionEvent actionEvent) throws Exception {
        // Clearing previous errors
        maxError.setVisible(false);
        invError.setVisible(false);
        // Making sure all fields are filled
        if(partName.getText().isEmpty() || partCost.getText().isEmpty() || partInv.getText().isEmpty() || partMax.getText().isEmpty() || partMin.getText().isEmpty() || machineID.getText().isEmpty()){
            letterError.setVisible(true);
        }
        // Catching cases where there's text in a non-text field
        if(!(partInv.getText().matches("[0-9]+")) || !(partMax.getText().matches("[0-9]+")) || !(partMin.getText().matches("[0-9]+")) || !(partCost.getText().matches("[0.00-9.99]+"))){
            letterError.setVisible(true);
            return;
        }
        // Checking which radio button is selected
        if(outsourced.isSelected()){
            toggleStatus = 1;
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
        // Adding to the Inventory List
        if(inHouse.isSelected()){
            if(!(machineID.getText().matches("[0-9]+"))){
                letterError.setVisible(true);
                return;
            }
            Inventory.addPart(new InHouse(partID++, partName.getText(), Double.parseDouble(partCost.getText()), Integer.parseInt(partInv.getText()), Integer.parseInt(partMin.getText()), Integer.parseInt(partMax.getText()), Integer.parseInt(machineID.getText())));
        } else {
            if(machineID.getText().matches("[0-9]+")){
                letterError.setVisible(true);
                return;
            }
            Inventory.addPart(new Outsourced(partID++, partName.getText(), Double.parseDouble(partCost.getText()), Integer.parseInt(partInv.getText()), Integer.parseInt(partMin.getText()), Integer.parseInt(partMax.getText()), machineID.getText()));
        }
        System.out.println(Inventory.getAllParts());
        Stage stage = (Stage) addCancel.getScene().getWindow();
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

    /**
     * Closes the add part page and returns to the main menu.
     *
     * @param actionEvent An action event
     * @throws Exception to catch exceptions
     */
    public void onAddCancel(ActionEvent actionEvent) throws Exception {
        Stage stage = (Stage) addCancel.getScene().getWindow();
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
