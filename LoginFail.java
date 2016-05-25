/**
 * Created by patricridell on 2016-05-19.
 */
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;



public class LoginFail {
    public static void display(String username, Boolean loginFailure) {
        Stage window = new Stage();
        if (loginFailure){
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Failed Login");
            window.setMinWidth(250);
            window.setMinHeight(200);

            Label loginLabel = new Label("Wrong username or password.");
            Button closeButton = new Button("Close window");
            closeButton.setOnAction(e -> window.close());

            VBox layout = new VBox(10);
            layout.getChildren().addAll(loginLabel, closeButton);
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.showAndWait();


        } else { // If it is not a login failure it is a sign up failure
            window.initModality(Modality.APPLICATION_MODAL);
            window.setTitle("Failed Sign Up");
            window.setMinWidth(250);
            window.setMinHeight(200);

            Label loginLabel = new Label("The username " + username + " already exists.");

            Button closeButton = new Button("Close window");
            closeButton.setOnAction(e -> window.close());

            VBox layout = new VBox(10);
            layout.getChildren().addAll(loginLabel, closeButton);
            layout.setAlignment(Pos.CENTER);

            Scene scene = new Scene(layout);
            window.setScene(scene);
            window.showAndWait();

        }


    }

}
