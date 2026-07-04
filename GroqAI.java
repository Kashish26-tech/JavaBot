import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import org.json.JSONArray;

public class GroqAI {
    
    private static final String API_KEY = "YOUR_GROQ_API_KEY_HERE";

    private static final String API_URL =
        "https://api.groq.com/openai/v1/chat/completions";

    public static String chat(String userMessage) {
        try {
            String requestBody = "{"
                + "\"model\": \"llama-3.1-8b-instant\","
                + "\"messages\": [{"
                + "  \"role\": \"system\","
                + "  \"content\": \"You are JavaBot, a helpful and friendly assistant.\""
                + "},{"
                + "  \"role\": \"user\","
                + "  \"content\": \"" + escapeJson(userMessage) + "\""
                + "}],"
                + "\"max_tokens\": 512,"
                + "\"temperature\": 0.7"
                + "}";

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

            HttpResponse<String> response =
                client.send(request, HttpResponse.BodyHandlers.ofString());

            // System.out.println("STATUS: " + response.statusCode());
            // System.out.println("BODY: " + response.body());

            if (response.statusCode() != 200) {
                return "⚠️ API error " + response.statusCode() + ". Try again shortly.";
            }

            JSONObject json = new JSONObject(response.body());
            return json
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .getString("content");

        } catch (Exception e) {
            System.out.println("EXCEPTION: " + e.getMessage());
            return "Sorry, something went wrong: " + e.getMessage();
        }
    }

    private static String escapeJson(String text) {
        return text
            .replace("\\", "\\\\")
            .replace("\"", "\\\"")
            .replace("\n", "\\n")
            .replace("\r", "\\r");
    }
}