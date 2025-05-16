package org.restoranproje.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class WaiterguiController {

    @FXML
    private Button cancel_button;

    @FXML
    private Button deliver_button;

    @FXML
    private Text durum_text;

    @FXML
    private TableView<?> menu_table;

    @FXML
    private TableView<?> order_table;

    @FXML
    private AnchorPane screen_pane;

    @FXML
    private Button takeorder;

}
