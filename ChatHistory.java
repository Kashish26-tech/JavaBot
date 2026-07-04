// ─────────────────────────────────────────────────────────────
//  ChatHistory.java — Stores and SAVES chat to a text file
//  Every message is saved automatically to chat_history.txt
// ─────────────────────────────────────────────────────────────

import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class ChatHistory {

    private ArrayList<String> messages = new ArrayList<>();

    // File where chat history is saved
    private static final String FILE_PATH = "E:\\ChatbotApp\\chat_history.txt";

    private DateTimeFormatter timeFormatter =
        DateTimeFormatter.ofPattern("HH:mm");

    private DateTimeFormatter dateFormatter =
        DateTimeFormatter.ofPattern("dd MMM yyyy");

    // ──────────────────────────────────────────────────────────
    //  add() — Saves one message to memory AND to file
    // ──────────────────────────────────────────────────────────
    public void add(String sender, String text) {
        String timestamp = LocalDateTime.now().format(timeFormatter);
        String entry = "[" + timestamp + "] " + sender + ": " + text;
        messages.add(entry);
        saveToFile(entry);  // Save immediately after each message
    }

    // ──────────────────────────────────────────────────────────
    //  saveToFile() — Appends one message to the text file
    //  Creates the file if it doesn't exist yet
    // ──────────────────────────────────────────────────────────
    private void saveToFile(String entry) {
        try {
            // true = append mode (don't overwrite existing content)
            FileWriter writer = new FileWriter(FILE_PATH, true);

            // Add session header if file is new/empty
            File file = new File(FILE_PATH);
            if (file.length() == 0 || messages.size() == 1) {
                writer.write("\n═══════════════════════════════════\n");
                writer.write("  Session: " +
                    LocalDateTime.now().format(dateFormatter) + "\n");
                writer.write("═══════════════════════════════════\n");
            }

            writer.write(entry + "\n");
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not save chat: " + e.getMessage());
        }
    }

    // ──────────────────────────────────────────────────────────
    //  getAll() — Returns full history as a string
    // ──────────────────────────────────────────────────────────
    public String getAll() {
        if (messages.isEmpty()) return "No chat history yet!";
        StringBuilder sb = new StringBuilder("📜 Chat History:\n");
        for (int i = 0; i < messages.size(); i++) {
            sb.append("  ").append(i + 1).append(". ")
              .append(messages.get(i)).append("\n");
        }
        return sb.toString();
    }

    // ──────────────────────────────────────────────────────────
    //  clear() — Clears memory (file history is kept)
    // ──────────────────────────────────────────────────────────
    public void clear() {
        messages.clear();
    }

    public int size() {
        return messages.size();
    }
}