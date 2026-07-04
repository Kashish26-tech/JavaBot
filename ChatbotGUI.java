import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import java.util.ArrayList;

public class ChatbotGUI extends Application {

    // ── Core modules ───────────────────────────────────────────
    private ResponseEngine engine = new ResponseEngine();
    private UserProfile profile = new UserProfile();
    private ChatHistory history = new ChatHistory();

    // ── UI components ──────────────────────────────────────────
    private VBox chatBox;
    private ScrollPane scrollPane;
    private TextField inputField;
    private HBox headerBox;
    private HBox inputBox;
    private VBox root;
    private Button darkBtn;

    // ── Dark mode state ────────────────────────────────────────
    private boolean darkMode = false;

    // ── Store all message bubbles for theme switching ──────────
    private ArrayList<Label> userBubbles = new ArrayList<>();
    private ArrayList<Label> botBubbles = new ArrayList<>();

    // ── Colors ─────────────────────────────────────────────────
    // Light mode
    private static final String LIGHT_BG = "#F5F5F5";
    private static final String LIGHT_HEADER = "#4A90D9";
    private static final String LIGHT_INPUT_BG = "#FFFFFF";
    private static final String LIGHT_BOT_BG = "#FFFFFF";
    private static final String LIGHT_BOT_TEXT = "#333333";

    // Dark mode
    private static final String DARK_BG = "#1E1E2E";
    private static final String DARK_HEADER = "#2D2D44";
    private static final String DARK_INPUT_BG = "#2D2D44";
    private static final String DARK_BOT_BG = "#2D2D44";
    private static final String DARK_BOT_TEXT = "#E0E0E0";

    @Override
    public void start(Stage stage) {

        // ── Header ─────────────────────────────────────────────
        Label headerLabel = new Label("🤖 JavaBot  (Powered by Groq AI)");
        headerLabel.setFont(Font.font("Arial", 18));
        headerLabel.setTextFill(Color.WHITE);

        darkBtn = new Button("🌙 Dark");
        darkBtn.setFont(Font.font("Arial", 12));
        darkBtn.setStyle("-fx-background-color: rgba(255,255,255,0.2); " +
                "-fx-text-fill: white; -fx-background-radius: 20; " +
                "-fx-cursor: hand;");
        darkBtn.setOnAction(e -> toggleDarkMode());

        Button saveBtn = new Button("💾 Save");
        saveBtn.setFont(Font.font("Arial", 12));
        saveBtn.setStyle("-fx-background-color: rgba(255,255,255,0.2); " +
                "-fx-text-fill: white; -fx-background-radius: 20; " +
                "-fx-cursor: hand;");
        saveBtn.setOnAction(e -> {
            addBotMessage("✅ Chat history is saved automatically to:\nE:\\ChatbotApp\\chat_history.txt");
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerBox = new HBox(10, headerLabel, spacer, saveBtn, darkBtn);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(12, 16, 12, 16));
        headerBox.setStyle("-fx-background-color: " + LIGHT_HEADER + ";");

        // ── Chat area ──────────────────────────────────────────
        chatBox = new VBox(10);
        chatBox.setPadding(new Insets(14));
        chatBox.setFillWidth(true);

        scrollPane = new ScrollPane(chatBox);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: " + LIGHT_BG +
                "; -fx-background-color: " + LIGHT_BG + ";");
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // ── Input bar ──────────────────────────────────────────
        inputField = new TextField();
        inputField.setPromptText("Ask me anything...");
        inputField.setFont(Font.font("Arial", 14));
        inputField.setPrefHeight(42);
        inputField.setStyle("-fx-background-color: " + LIGHT_BG +
                "; -fx-text-fill: #333333; -fx-background-radius: 6;");
        HBox.setHgrow(inputField, Priority.ALWAYS);
        inputField.setOnAction(e -> sendMessage());

        Button sendBtn = new Button("Send ➤");
        sendBtn.setPrefHeight(42);
        sendBtn.setFont(Font.font("Arial", 14));
        sendBtn.setStyle("-fx-background-color: #4A90D9; -fx-text-fill: white; " +
                "-fx-background-radius: 6; -fx-cursor: hand;");
        sendBtn.setOnAction(e -> sendMessage());

        inputBox = new HBox(10, inputField, sendBtn);
        inputBox.setPadding(new Insets(10));
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setStyle("-fx-background-color: " + LIGHT_INPUT_BG +
                "; -fx-border-color: #DDDDDD; -fx-border-width: 1 0 0 0;");

        // ── Root layout ────────────────────────────────────────
        root = new VBox(headerBox, scrollPane, inputBox);
        root.setPrefSize(480, 600);

        // ── Welcome message ────────────────────────────────────
        addBotMessage("👋 Hi! I'm JavaBot powered by Groq AI.\n" +
                "Ask me anything! Click 🌙 for dark mode.");

        Scene scene = new Scene(root);
        stage.setTitle("JavaBot — Groq AI");
        stage.setScene(scene);
        stage.show();
    }

    // ──────────────────────────────────────────────────────────
    // toggleDarkMode() — switches between light and dark
    // ──────────────────────────────────────────────────────────
    void toggleDarkMode() {
        darkMode = !darkMode;

        if (darkMode) {
            // ── Apply dark theme ───────────────────────────────
            darkBtn.setText("☀️ Light");
            headerBox.setStyle("-fx-background-color: " + DARK_HEADER + ";");
            scrollPane.setStyle("-fx-background: " + DARK_BG +
                    "; -fx-background-color: " + DARK_BG + ";");
            chatBox.setStyle("-fx-background-color: " + DARK_BG + ";");
            inputBox.setStyle("-fx-background-color: " + DARK_INPUT_BG +
                    "; -fx-border-color: #444466; -fx-border-width: 1 0 0 0;");
            inputField.setStyle("-fx-background-color: #3D3D5C; " +
                    "-fx-text-fill: #E0E0E0; -fx-background-radius: 6; " +
                    "-fx-prompt-text-fill: #888888;");

            // Update all bot bubbles to dark style
            for (Label l : botBubbles) {
                l.setStyle("-fx-background-color: " + DARK_BOT_BG + "; " +
                        "-fx-text-fill: " + DARK_BOT_TEXT + "; " +
                        "-fx-background-radius: 16 16 16 4; " +
                        "-fx-border-color: #444466; " +
                        "-fx-border-radius: 16 16 16 4;");
            }

        } else {
            // ── Apply light theme ──────────────────────────────
            darkBtn.setText("🌙 Dark");
            headerBox.setStyle("-fx-background-color: " + LIGHT_HEADER + ";");
            scrollPane.setStyle("-fx-background: " + LIGHT_BG +
                    "; -fx-background-color: " + LIGHT_BG + ";");
            chatBox.setStyle("-fx-background-color: " + LIGHT_BG + ";");
            inputBox.setStyle("-fx-background-color: " + LIGHT_INPUT_BG +
                    "; -fx-border-color: #DDDDDD; -fx-border-width: 1 0 0 0;");
            inputField.setStyle("-fx-background-color: " + LIGHT_BG + "; " +
                    "-fx-text-fill: #333333; -fx-background-radius: 6;");

            // Update all bot bubbles to light style
            for (Label l : botBubbles) {
                l.setStyle("-fx-background-color: " + LIGHT_BOT_BG + "; " +
                        "-fx-text-fill: " + LIGHT_BOT_TEXT + "; " +
                        "-fx-background-radius: 16 16 16 4; " +
                        "-fx-border-color: #E0E0E0; " +
                        "-fx-border-radius: 16 16 16 4;");
            }
        }
    }

    // ──────────────────────────────────────────────────────────
    // sendMessage() — sends user message and gets AI reply
    // ──────────────────────────────────────────────────────────
    void sendMessage() {
        String text = inputField.getText().trim();
        if (text.isEmpty())
            return;
        inputField.clear();

        addUserMessage(text);
        history.add("You", text);
        inputField.setDisable(true);
        addBotMessage("⏳ Thinking...");

        new Thread(() -> {
            String response = engine.getResponse(text, profile);
            history.add("JavaBot", response);
            Platform.runLater(() -> {
                chatBox.getChildren().remove(chatBox.getChildren().size() - 1);
                botBubbles.remove(botBubbles.size() - 1);
                addBotMessage(response);
                inputField.setDisable(false);
                inputField.requestFocus();
            });
        }).start();
    }

    // ──────────────────────────────────────────────────────────
    // addUserMessage() — blue bubble RIGHT side
    // ──────────────────────────────────────────────────────────
    void addUserMessage(String text) {
        Label msg = new Label(text);
        msg.setWrapText(true);
        msg.setMaxWidth(300);
        msg.setFont(Font.font("Arial", 14));
        msg.setPadding(new Insets(8, 12, 8, 12));
        msg.setStyle("-fx-background-color: #4A90D9; -fx-text-fill: white; " +
                "-fx-background-radius: 16 16 4 16;");
        userBubbles.add(msg);

        HBox row = new HBox(msg);
        row.setAlignment(Pos.CENTER_RIGHT);
        row.setPadding(new Insets(2, 4, 2, 60));
        chatBox.getChildren().add(row);
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    // ──────────────────────────────────────────────────────────
    // addBotMessage() — white/dark bubble LEFT side
    // ──────────────────────────────────────────────────────────
    void addBotMessage(String text) {
        Label msg = new Label(text);
        msg.setWrapText(true);
        msg.setMaxWidth(300);
        msg.setFont(Font.font("Arial", 14));
        msg.setPadding(new Insets(8, 12, 8, 12));

        if (darkMode) {
            msg.setStyle("-fx-background-color: " + DARK_BOT_BG + "; " +
                    "-fx-text-fill: " + DARK_BOT_TEXT + "; " +
                    "-fx-background-radius: 16 16 16 4; " +
                    "-fx-border-color: #444466; " +
                    "-fx-border-radius: 16 16 16 4;");
        } else {
            msg.setStyle("-fx-background-color: " + LIGHT_BOT_BG + "; " +
                    "-fx-text-fill: " + LIGHT_BOT_TEXT + "; " +
                    "-fx-background-radius: 16 16 16 4; " +
                    "-fx-border-color: #E0E0E0; " +
                    "-fx-border-radius: 16 16 16 4;");
        }
        botBubbles.add(msg);

        HBox row = new HBox(msg);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(2, 60, 2, 4));
        chatBox.getChildren().add(row);
        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    public static void main(String[] args) {
        launch(args);
    }
}