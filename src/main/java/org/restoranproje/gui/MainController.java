package org.restoranproje.gui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainController {

    @FXML
    private Button chef_button;

    @FXML
    private Pane inner_pane;

    @FXML
    private Button menu_button;

    @FXML
    private Pane most_inner_pane;

    @FXML
    private Button order_button;

    @FXML
    private AnchorPane side_ankerpane;

    @FXML
    private Button stock_button;

    @FXML
    private Pane view_pane;

    @FXML
    private Button waiter_button;

    @FXML
    void handleChefButtonClick(MouseEvent event) {
        loadPage("chefedit.fxml");
    }

    @FXML
    void handleMenuButtonClick(MouseEvent event) {
        loadPage("menuedit.fxml");
    }

    @FXML
    void handleOrderButtonClick(MouseEvent event) {
        loadPage("orderedit.fxml");
    }

    @FXML
    void handleStockButtonClick(MouseEvent event) {
        loadPage("stockedit.fxml");
    }

    @FXML
    void handleWaiterButtonClick(MouseEvent event) {
        loadPage("waiteredit.fxml");
    }

    private void loadPage(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/restoranproje/gui/" + fxmlFile));
            Pane newPage = loader.load();

            view_pane.getChildren().clear();
            view_pane.getChildren().add(newPage);

            newPage.setLayoutX(0);
            newPage.setLayoutY(0);
            newPage.prefWidthProperty().bind(view_pane.widthProperty());
            newPage.prefHeightProperty().bind(view_pane.heightProperty());

        } catch (IOException e) {
            System.err.println("FXML yüklenemedi: " + fxmlFile);
            e.printStackTrace();
        }
    }
}
