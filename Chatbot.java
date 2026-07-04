import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;

public class Chatbot {

    static String userName = "";
    static ArrayList<String> chatHistory = new ArrayList<>();
    static Random random = new Random();

    // Multiple replies for variety
    static String[] greetings = {
        "Hey %s! Great to see you! 😊",
        "Hello %s! How's your day going? 🌟",
        "Hi %s! What can I do for you today? 👋"
    };

    static String[] unknownReplies = {
        "Hmm, I didn't get that %s. Type 'help' to see what I can do!",
        "I'm still learning! Try asking something else %s.",
        "Not sure about that one %s. Try 'help' for ideas!"
    };

    static String randomFrom(String[] arr, String name) {
        String picked = arr[random.nextInt(arr.length)];
        return String.format(picked, name.isEmpty() ? "friend" : name);
    }

    static String getResponse(String input) {
        String lower = input.toLowerCase().trim();

        // Name
        if (lower.contains("my name is")) {
            userName = lower.replace("my name is", "").trim();
            userName = userName.substring(0, 1).toUpperCase() + userName.substring(1);
            return "Nice to meet you, " + userName + "! I'll always remember you. 🧠";
        }

        // Greetings
        if (lower.contains("hello") || lower.contains("hi") || lower.contains("hey")) {
            if (userName.isEmpty()) return "Hello! I don't know your name yet. Say: 'my name is ...'";
            return randomFrom(greetings, userName);
        }

        // How are you
        if (lower.contains("how are you") || lower.contains("how r u")) {
            return "I'm running at 100% efficiency! 😄 What about you, " + (userName.isEmpty() ? "friend" : userName) + "?";
        }

        // Compliments
        if (lower.contains("you are great") || lower.contains("good bot") || lower.contains("nice")) {
            return "Aww, thank you " + (userName.isEmpty() ? "" : userName) + "! You made my day! 🌟";
        }

        // Jokes
        if (lower.contains("joke") || lower.contains("funny")) {
            String[] jokes = {
                "Why do Java developers wear glasses? Because they don't C#! 😂",
                "Why was the computer cold? It left its Windows open! 🥶",
                "I told a joke about Java once. It had too many class issues. 😆"
            };
            return jokes[random.nextInt(jokes.length)];
        }

        // Time / Date
        if (lower.contains("time") || lower.contains("date")) {
            java.time.LocalDateTime now = java.time.LocalDateTime.now();
            return "Right now it is: " + now.getDayOfWeek() + ", " +
                   now.getDayOfMonth() + " " + now.getMonth() + " " +
                   now.getYear() + " at " + String.format("%02d:%02d", now.getHour(), now.getMinute());
        }

        // Math
        if (lower.contains("calculate") || lower.contains("what is")) {
            try {
                String expr = lower.replaceAll("[^0-9+\\-*/]", "").trim();
                if (!expr.isEmpty()) {
                    int result = evaluate(expr);
                    return "The answer is: " + result + " 🧮";
                }
            } catch (Exception e) {
                return "I can only do simple math like: 'calculate 5+3'";
            }
        }

        // Name recall
        if (lower.contains("my name") || lower.contains("remember")) {
            if (!userName.isEmpty()) return "Your name is " + userName + ". I never forget! 😎";
            return "I don't know your name yet! Say: 'my name is ...'";
        }

        // History
        if (lower.contains("history")) {
            if (chatHistory.isEmpty()) return "No history yet! Start chatting.";
            StringBuilder sb = new StringBuilder("📜 Chat History:\n");
            for (int i = 0; i < chatHistory.size(); i++) {
                sb.append("  ").append(i + 1).append(". ").append(chatHistory.get(i)).append("\n");
            }
            return sb.toString();
        }

        // Help
        if (lower.contains("help")) {
            return "🤖 I understand:\n" +
                   "  - 'my name is [name]'\n" +
                   "  - 'hello / hi / hey'\n" +
                   "  - 'how are you'\n" +
                   "  - 'tell me a joke'\n" +
                   "  - 'what is the time'\n" +
                   "  - 'calculate 10+5'\n" +
                   "  - 'history'\n" +
                   "  - 'exit'";
        }

        // Bot name
        if (lower.contains("your name")) {
            return "I'm JavaBot — built by you! 🚀";
        }

        return randomFrom(unknownReplies, userName);
    }

    // Simple math evaluator (only + - * /)
    static int evaluate(String expr) {
        if (expr.contains("+")) {
            String[] p = expr.split("\\+");
            return Integer.parseInt(p[0].trim()) + Integer.parseInt(p[1].trim());
        } else if (expr.contains("-")) {
            String[] p = expr.split("-");
            return Integer.parseInt(p[0].trim()) - Integer.parseInt(p[1].trim());
        } else if (expr.contains("*")) {
            String[] p = expr.split("\\*");
            return Integer.parseInt(p[0].trim()) * Integer.parseInt(p[1].trim());
        } else if (expr.contains("/")) {
            String[] p = expr.split("/");
            return Integer.parseInt(p[0].trim()) / Integer.parseInt(p[1].trim());
        }
        throw new RuntimeException("Unknown operation");
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=====================================");
        System.out.println("   Welcome to JavaBot v3.0! 🤖      ");
        System.out.println("   Type 'help' to see commands.      ");
        System.out.println("   Type 'exit' to quit.              ");
        System.out.println("=====================================");

        while (true) {
            System.out.print("\nYou: ");
            String userInput = scanner.nextLine();

            if (userInput.equalsIgnoreCase("exit")) {
                System.out.println("JavaBot: Bye " + (userName.isEmpty() ? "!" : userName + "! See you soon! 👋"));
                break;
            }

            chatHistory.add("You: " + userInput);
            String response = getResponse(userInput);
            System.out.println("JavaBot: " + response);
            chatHistory.add("JavaBot: " + response);
        }

        scanner.close();
    }
}