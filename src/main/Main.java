package main;

import domain.DomainController;
import domain.Material;
import domain.MaterialIdentifier;
import domain.Visibility;
import gui.MaterialController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    
    
     @Override
    public void start(Stage stage) {
        DomainController domainController = new DomainController();

        Scene scene = new Scene(new MaterialController(domainController,stage));

        stage.setTitle("Overzicht");
        stage.setScene(scene);

        
        stage.show();
    }

    public static void main(String... args) {
        Application.launch(Main.class, args);
    }
}
