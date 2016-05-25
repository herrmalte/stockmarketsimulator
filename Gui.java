/**
 * Created by patricridell on 2016-05-09.
 */
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.Stack;
import java.util.StringJoiner;
import java.sql.*;
import java.sql.Connection;
import java.sql.DriverManager;


public class Gui extends Application {


    Stage window; // window is the parent window "the parent of everything"
    //Stage graph;
    DBcon db = new DBcon();
    Connection c = db.main("");
    String usr = null;

    public Gui() throws SQLException {
    }

    Scene loginPage, homePage, buyPage; // create different scenes, one for each page


    public static void main(String args[]){
        launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        window = primaryStage;
        window.setOnCloseRequest(e -> closeProgram());
        // ##################### LOGIN PAGE ###########################

        GridPane loginGrid = new GridPane();
        loginGrid.setPadding(new Insets(15,15,15,15));
        loginGrid.setVgap(10);
        loginGrid.setHgap(10);

        // Create username label and input field

        Label usernameLabel = new Label("Username:");
        GridPane.setConstraints(usernameLabel, 0, 0);
        TextField usernameInput = new TextField();
        GridPane.setConstraints(usernameInput, 1, 0);
        usernameInput.setPromptText("Enter username"); // Text that disappear when user clicks on the input field


        // Create password label and input field

        Label passwordLabel = new Label("Password:");
        GridPane.setConstraints(passwordLabel, 0, 1);
        PasswordField passwordInput = new PasswordField();
        GridPane.setConstraints(passwordInput, 1, 1);
        passwordInput.setPromptText("Enter password");

        // Login button
        Button loginButton = new Button("Log in");
        GridPane.setConstraints(loginButton, 1,2);
        loginButton.setOnAction(e -> {
            try {
                validateLogin(usernameInput, passwordInput);
                usr = usernameInput.getText();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }); // lambda expr that sets the next scene

        // sign up button
        Button signupButton = new Button("Sign up");
        GridPane.setConstraints(signupButton, 1,3);
        signupButton.setOnAction(e -> {
            try {
                newUser(usernameInput, passwordInput);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        loginGrid.getChildren().addAll(usernameLabel, usernameInput, passwordLabel, passwordInput, loginButton, signupButton);

        loginPage = new Scene(loginGrid, 300, 200);






        // ##################### HOME PAGE ###########################

        GridPane homeGrid = new GridPane();
        homeGrid.setPadding(new Insets(15,15,15,15));
        homeGrid.setVgap(10);
        homeGrid.setHgap(10);

        // Create username label and input field
        Label homeLabel = new Label("Welcome back!");
        GridPane.setConstraints(homeLabel, 1, 0);

        // Return button
        Button returnButton = new Button("Return");
        returnButton.setOnAction(e -> window.setScene(homePage));

        // Search stock button
        Button searchButton = new Button("    Search stock     "); // SKa heta portfolioProgress
        GridPane.setConstraints(searchButton, 0,4);
        TextField searchInput = new TextField();
        GridPane.setConstraints(searchInput, 1, 4);
        searchInput.setPromptText("Ticker (eg. AAPL)");
        searchButton.setOnAction(e -> searchStock(searchInput));

        // Buy/Sell button
        Button buySellButton = new Button("         Buy/sell         ");
        GridPane.setConstraints(buySellButton, 0,3);
        buySellButton.setOnAction(e -> window.setScene(buyPage));

        // Overview button
        Button portfolioOverviewButton = new Button("Portfolio Overview");
        GridPane.setConstraints(portfolioOverviewButton, 0,2);
        portfolioOverviewButton.setOnAction(e -> {
            try {
                viewPortfolio(usernameInput);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });

        // Log out button
        Button logoutButton = new Button("Log out");
        GridPane.setConstraints(logoutButton, 0,7);
        logoutButton.setOnAction(e -> window.setScene(loginPage));

        homeGrid.getChildren().addAll(homeLabel, searchButton, searchInput, buySellButton, portfolioOverviewButton, logoutButton);

        homePage = new Scene(homeGrid, 400, 400);









        // ##################### BUY/SELL PAGE ###########################

        GridPane buySellGrid = new GridPane();
        buySellGrid.setPadding(new Insets(15,15,15,15));
        buySellGrid.setVgap(10);
        buySellGrid.setHgap(10);

        Label buySellLabel = new Label("Here you can buy/sell stocks");
        GridPane.setConstraints(buySellLabel, 0, 0);

        // Dropdown menu to choose buy or sell
        ChoiceBox<String> buySellChoiceBox = new ChoiceBox<>();
        buySellChoiceBox.getItems().addAll("Buy", "Sell");
        buySellChoiceBox.setValue("Buy");
        GridPane.setConstraints(buySellChoiceBox, 0, 1);

        // Choose-stock-label and input field
        Label chooseStockLabel = new Label("Choose stock:");
        GridPane.setConstraints(chooseStockLabel, 0,2);
        TextField chosenStock = new TextField();
        GridPane.setConstraints(chosenStock, 0,3);
        chosenStock.setPromptText("Eg. AAPL, GOOG, VOLVY");

        // Number of units and input field
        Label unitsLabel = new Label("Choose amount:");
        GridPane.setConstraints(unitsLabel, 0,4);
        TextField chosenAmount = new TextField();
        GridPane.setConstraints(chosenAmount, 0,5);
        chosenAmount.setPromptText("Number of stocks");

        // Confirm button
        Button confirmButton = new Button("Confirm transaction");
        GridPane.setConstraints(confirmButton, 0,6);

        confirmButton.setOnAction(e -> {
            completeTransaction(buySellChoiceBox, chosenStock, chosenAmount);
        });


        // Return button
        Button buyReturnButton = new Button("Back");
        GridPane.setConstraints(buyReturnButton, 0,7);
        buyReturnButton.setOnAction(e -> window.setScene(homePage));
        buySellGrid.getChildren().addAll(buySellLabel, buySellChoiceBox, chooseStockLabel, chosenStock, unitsLabel, chosenAmount, confirmButton, buyReturnButton);


        buyPage = new Scene(buySellGrid, 600, 400);




        window.setScene(loginPage);
        window.setTitle("Stockmarket simulator");
        window.show();


    }



    private void closeProgram() {
        // Closes the program correctly
        window.close();
    }

    // Change this function to query the database
    private void validateLogin(TextField usernameInput, TextField passwordInput) throws SQLException {
        if(db.userLogin(c, usernameInput.getText(),passwordInput.getText())) {
            //usr = usernameInput.getText();
            window.setScene(homePage);
        }

    }
    private void completeTransaction(ChoiceBox<String> buySellChoiceBox, TextField chosenStock, TextField chosenAmount) {
        String transType = buySellChoiceBox.getValue();
        String ticker = chosenStock.getText();
        String stockAmount = chosenAmount.getText();

        // SQL query to complete the transaction
        try {
            db.completeTransaction(c , usr, transType, ticker, Integer.valueOf(stockAmount));
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }
    private void searchStock(TextField searchInput) {
        String ticker = searchInput.getText();
        try {
            UserChart.display(ticker);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void newUser(TextField username, TextField password) throws SQLException {
        String user = username.getText();
        //LoginFail.display("hej", false);
        Boolean returnValue = db.signUp(c, user, password.getText());
        if (returnValue) {
            window.setScene(loginPage);
        } else LoginFail.display(user, false);

    }

    private void viewPortfolio(TextField username) throws SQLException {
        //db.getPortfolio(c, username.getText());
        String user = username.getText();
        try {
            //System.out.print("hej");
            PortfolioOverview.display(c, user);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
