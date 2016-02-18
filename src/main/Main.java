package main;

import domain.DomainController;
import gui.OverviewController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage stage) {
        DomainController domainController = new DomainController();
        Scene scene = new Scene(new OverviewController(domainController));
        stage.setMinWidth(620);
        stage.setMinHeight(463);
        stage.setTitle("Overzicht - IIM");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String... args) {
        Application.launch(Main.class, args);
    }
}
