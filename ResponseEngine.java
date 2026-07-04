import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ResponseEngine {

    public String getResponse(String input, UserProfile profile) {
        String lower = input.toLowerCase().trim();

        // Name
        if (lower.contains("my name is")) {
            String name = lower.replace("my name is", "").trim();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            profile.setName(name);
            return "Nice to meet you, " + name + "! I'll remember you. 🧠";
        }

        // Name recall
        if (lower.contains("my name") || lower.contains("remember")) {
            return profile.getName().isEmpty()
                ? "I don't know your name yet! Say: 'my name is ...'"
                : "Your name is " + profile.getName() + "! 😎";
        }

        // Time / Date
        if (lower.contains("what time") || lower.contains("what date")) {
            return "It's " + LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy HH:mm"));
        }

        // Math
        if (lower.startsWith("calculate ")) {
            try {
                String expr = lower.replace("calculate", "")
                    .replaceAll("[^0-9+\\-*/]", "").trim();
                return "Answer: " + evaluate(expr) + " 🧮";
            } catch (Exception e) {
                return "Try: 'calculate 5+3'";
            }
        }

        // Everything else → Gemini AI
        String context = profile.getName().isEmpty()
            ? input
            : "My name is " + profile.getName() + ". " + input;

        return GroqAI.chat(context);
    }

    private int evaluate(String expr) {
        if (expr.contains("+")) { String[] p = expr.split("\\+"); return Integer.parseInt(p[0].trim()) + Integer.parseInt(p[1].trim()); }
        if (expr.contains("-")) { String[] p = expr.split("-");   return Integer.parseInt(p[0].trim()) - Integer.parseInt(p[1].trim()); }
        if (expr.contains("*")) { String[] p = expr.split("\\*"); return Integer.parseInt(p[0].trim()) * Integer.parseInt(p[1].trim()); }
        if (expr.contains("/")) { String[] p = expr.split("/");   return Integer.parseInt(p[0].trim()) / Integer.parseInt(p[1].trim()); }
        throw new RuntimeException("Unknown");
    }
}