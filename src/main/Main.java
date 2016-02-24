package main;

import domain.DomainController;
import gui.LoginScreenController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    @Override
    public void start(Stage stage) {
        DomainController domainController = new DomainController();
        Scene scene = new Scene(new LoginScreenController(domainController));
        stage.setTitle("Login - IIM");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String... args) {
        Application.launch(Main.class, args);
    }
}
