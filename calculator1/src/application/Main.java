package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    
    private TextField calculationDisplay;
    private TextField resultDisplay;
    private StringBuilder currentInput = new StringBuilder();
    private String currentOperator = "";
    private double result = 0;
    private boolean startNewNumber = true;
    private StringBuilder calculationHistory = new StringBuilder();

    @Override
    public void start(Stage primaryStage) {
        // Create the displays
        calculationDisplay = new TextField();
        calculationDisplay.setEditable(false);
        calculationDisplay.setStyle("-fx-font-size: 18px; " +
                                 "-fx-alignment: center-right; " +
                                 "-fx-background-color: #2d2d2d; " +
                                 "-fx-text-fill: #ffffff; " +
                                 "-fx-border-color: #444; " +
                                 "-fx-border-radius: 5; " +
                                 "-fx-padding: 10px;");
        calculationDisplay.setPrefHeight(50);
        
        resultDisplay = new TextField();
        resultDisplay.setEditable(false);
        resultDisplay.setStyle("-fx-font-size: 28px; " +
                              "-fx-font-weight: bold; " +
                              "-fx-alignment: center-right; " +
                              "-fx-background-color: #2d2d2d; " +
                              "-fx-text-fill: #ffffff; " +
                              "-fx-border-color: #444; " +
                              "-fx-border-radius: 5; " +
                              "-fx-padding: 10px;");
        resultDisplay.setPrefHeight(70);
        resultDisplay.setText("0");

        // Create buttons
        Button[] numButtons = new Button[10];
        for (int i = 0; i < 10; i++) {
            numButtons[i] = createNumberButton(String.valueOf(i), 
                "-fx-font-size: 20px; " +
                "-fx-background-radius: 5px; " +
                "-fx-background-color: #3a3a3a; " +
                "-fx-text-fill: white; " +
                "-fx-border-color: #444; " +
                "-fx-border-radius: 5px;");
        }

        Button btnDot = createButton(".", 
            "-fx-font-size: 20px; " +
            "-fx-background-radius: 5px; " +
            "-fx-background-color: #3a3a3a; " +
            "-fx-text-fill: white; " +
            "-fx-border-color: #444; " +
            "-fx-border-radius: 5px;", 
            e -> handleDotButton());

        Button btnAdd = createOperatorButton("+");
        Button btnSubtract = createOperatorButton("-");
        Button btnMultiply = createOperatorButton("*");
        Button btnDivide = createOperatorButton("/");
        
        Button btnEquals = createButton("=", 
            "-fx-font-size: 20px; " +
            "-fx-background-radius: 5px; " +
            "-fx-background-color: #4CAF50; " +
            "-fx-text-fill: white;", 
            e -> calculate());
            
        Button btnClear = createButton("C", 
            "-fx-font-size: 20px; " +
            "-fx-background-radius: 5px; " +
            "-fx-background-color: #f44336; " +
            "-fx-text-fill: white;", 
            e -> clear());

        Button btnBackspace = createButton("⌫", 
            "-fx-font-size: 20px; " +
            "-fx-background-radius: 5px; " +
            "-fx-background-color: #5a5a5a; " +
            "-fx-text-fill: white;", 
            e -> handleBackspace());

        // Layout
        GridPane buttonGrid = new GridPane();
        buttonGrid.setHgap(10);
        buttonGrid.setVgap(10);
        buttonGrid.setPadding(new Insets(15));

        // Add buttons to grid
        // Row 0: 7 8 9 +
        buttonGrid.add(numButtons[7], 0, 0);
        buttonGrid.add(numButtons[8], 1, 0);
        buttonGrid.add(numButtons[9], 2, 0);
        buttonGrid.add(btnAdd, 3, 0);
        
        // Row 1: 4 5 6 -
        buttonGrid.add(numButtons[4], 0, 1);
        buttonGrid.add(numButtons[5], 1, 1);
        buttonGrid.add(numButtons[6], 2, 1);
        buttonGrid.add(btnSubtract, 3, 1);
        
        // Row 2: 1 2 3 *
        buttonGrid.add(numButtons[1], 0, 2);
        buttonGrid.add(numButtons[2], 1, 2);
        buttonGrid.add(numButtons[3], 2, 2);
        buttonGrid.add(btnMultiply, 3, 2);
        
        // Row 3: 0 . /
        buttonGrid.add(numButtons[0], 0, 3, 2, 1); // Span 2 columns
        buttonGrid.add(btnDot, 2, 3);
        buttonGrid.add(btnDivide, 3, 3);
        
        // Row 4: = C ⌫
        buttonGrid.add(btnEquals, 0, 4, 2, 1); // Span 2 columns
        buttonGrid.add(btnClear, 2, 4);
        buttonGrid.add(btnBackspace, 3, 4);

        VBox root = new VBox(10, calculationDisplay, resultDisplay, buttonGrid);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: #1e1e1e;");
        
        Scene scene = new Scene(root, 350, 500);
        
        // Add keyboard support
        scene.setOnKeyPressed(this::handleKeyPress);
        
        primaryStage.setTitle("Calculator");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void handleKeyPress(KeyEvent event) {
        switch (event.getCode()) {
            // Number keys
            case DIGIT0:
            case NUMPAD0: handleNumberInput("0"); break;
            case DIGIT1:
            case NUMPAD1: handleNumberInput("1"); break;
            case DIGIT2:
            case NUMPAD2: handleNumberInput("2"); break;
            case DIGIT3:
            case NUMPAD3: handleNumberInput("3"); break;
            case DIGIT4:
            case NUMPAD4: handleNumberInput("4"); break;
            case DIGIT5:
            case NUMPAD5: handleNumberInput("5"); break;
            case DIGIT6:
            case NUMPAD6: handleNumberInput("6"); break;
            case DIGIT7:
            case NUMPAD7: handleNumberInput("7"); break;
            case DIGIT8:
            case NUMPAD8: handleNumberInput("8"); break;
            case DIGIT9:
            case NUMPAD9: handleNumberInput("9"); break;
                
            // Decimal point
            case DECIMAL:
            case PERIOD: handleDotButton(); break;
                
            // Operators
            case ADD: handleOperator("+"); break;
            case SUBTRACT: handleOperator("-"); break;
            case MULTIPLY: handleOperator("*"); break;
            case DIVIDE: handleOperator("/"); break;
                
            // Equals/Enter
            case ENTER:
            case EQUALS: calculate(); break;
                
            // Clear
            case ESCAPE: clear(); break;
            case DELETE: clear(); break;
                
            // Backspace
            case BACK_SPACE: handleBackspace(); break;
        }
    }

    private void handleNumberInput(String number) {
        if (startNewNumber) {
            currentInput.setLength(0);
            startNewNumber = false;
            if (calculationHistory.toString().contains("=")) {
                calculationHistory.setLength(0);
            }
        }
        currentInput.append(number);
        updateDisplay();
    }

    private void handleOperator(String operator) {
        if (currentInput.length() > 0 || calculationHistory.length() > 0) {
            if (!startNewNumber) {
                calculationHistory.append(currentInput.toString()).append(" ").append(operator).append(" ");
                calculateIntermediate();
                currentOperator = operator;
                startNewNumber = true;
            } else {
                if (calculationHistory.length() > 0) {
                    calculationHistory.setLength(calculationHistory.length() - 3);
                    calculationHistory.append(operator).append(" ");
                    currentOperator = operator;
                }
            }
            updateDisplay();
        }
    }

    private void handleBackspace() {
        if (currentInput.length() > 0) {
            currentInput.deleteCharAt(currentInput.length() - 1);
            updateDisplay();
        } else if (calculationHistory.length() > 0 && !startNewNumber) {
            // Allow backspace after operator if no number entered yet
            String history = calculationHistory.toString().trim();
            calculationHistory.setLength(history.length() - 3); // Remove " + " format
            currentOperator = "";
            startNewNumber = false;
            updateDisplay();
        }
    }

    private void handleDotButton() {
        if (startNewNumber) {
            currentInput.setLength(0);
            currentInput.append("0.");
            startNewNumber = false;
            updateDisplay();
        } else if (!currentInput.toString().contains(".")) {
            currentInput.append(".");
            updateDisplay();
        }
    }

    private Button createNumberButton(String text, String style) {
        Button button = createButton(text, style, e -> handleNumberInput(text));
        
        // Add hover effect
        button.setOnMouseEntered(e -> button.setStyle(style.replace("#3a3a3a", "#4a4a4a")));
        button.setOnMouseExited(e -> button.setStyle(style));
        
        return button;
    }

    private Button createOperatorButton(String operator) {
        String style = "-fx-font-size: 20px; " +
                      "-fx-background-radius: 5px; " +
                      "-fx-background-color: #5a5a5a; " +
                      "-fx-text-fill: white; " +
                      "-fx-border-color: #444; " +
                      "-fx-border-radius: 5px;";
                      
        Button button = createButton(operator, style, e -> handleOperator(operator));
        
        // Add hover effect for operators
        button.setOnMouseEntered(e -> button.setStyle(style.replace("#5a5a5a", "#6a6a6a")));
        button.setOnMouseExited(e -> button.setStyle(style));
        
        return button;
    }

    private Button createButton(String text, String style, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setPrefSize(75, 65);
        button.setStyle(style);
        button.setOnAction(handler);
        return button;
    }

    private void calculateIntermediate() {
        if (currentInput.length() == 0) return;
        
        try {
            double inputNumber = Double.parseDouble(currentInput.toString());
            
            if (currentOperator.isEmpty()) {
                result = inputNumber;
            } else {
                switch (currentOperator) {
                    case "+":
                        result += inputNumber;
                        break;
                    case "-":
                        result -= inputNumber;
                        break;
                    case "*":
                        result *= inputNumber;
                        break;
                    case "/":
                        result /= inputNumber;
                        break;
                }
            }
            
            currentInput.setLength(0);
            currentInput.append(result);
        } catch (NumberFormatException e) {
            clear();
            resultDisplay.setText("Error");
        }
    }

    private void calculate() {
        if (currentInput.length() == 0) return;
        
        try {
            double inputNumber = Double.parseDouble(currentInput.toString());
            
            if (calculationHistory.toString().contains("=")) {
                result = inputNumber;
                calculationHistory.setLength(0);
                return;
            }
            
            if (currentOperator.isEmpty()) {
                result = inputNumber;
            } else {
                switch (currentOperator) {
                    case "+":
                        result += inputNumber;
                        break;
                    case "-":
                        result -= inputNumber;
                        break;
                    case "*":
                        result *= inputNumber;
                        break;
                    case "/":
                        if (inputNumber == 0) {
                            resultDisplay.setText("Error: Division by zero");
                            clear();
                            return;
                        }
                        result /= inputNumber;
                        break;
                }
            }
            
            calculationDisplay.setText(calculationHistory.toString() + currentInput.toString());
            resultDisplay.setText(String.valueOf(result));
            
            currentInput.setLength(0);
            currentInput.append(result);
            calculationHistory.append(currentInput.toString()).append(" = ");
            startNewNumber = true;
        } catch (NumberFormatException e) {
            resultDisplay.setText("Error");
            clear();
        }
    }

    private void updateDisplay() {
        if (currentInput.length() == 0 && calculationHistory.length() == 0) {
            resultDisplay.setText("0");
            calculationDisplay.clear();
        } else {
            if (calculationHistory.toString().contains("=")) {
                resultDisplay.setText(currentInput.toString());
                calculationDisplay.setText(calculationHistory.toString());
            } else {
                calculationDisplay.setText(calculationHistory.toString() + currentInput.toString());
            }
        }
    }

    private void clear() {
        currentInput.setLength(0);
        calculationHistory.setLength(0);
        currentOperator = "";
        result = 0;
        startNewNumber = true;
        resultDisplay.setText("0");
        calculationDisplay.clear();
    }

    public static void main(String[] args) {
        launch(args);
    }
}