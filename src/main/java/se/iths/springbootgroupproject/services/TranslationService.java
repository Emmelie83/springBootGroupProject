package se.iths.springbootgroupproject.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;


@Service
public class TranslationService {

    private final WebClient webClient;

    public TranslationService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:5000").build();
    }

    public String detectMessageLanguage(String text) {

        String result = webClient.post()
                .uri("/detect")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("q", text))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        if (result.contains("\"en\"")) return "en";
        else return "sv";
    }

    public String translateText(String text) {
        String sourceLanguage = detectMessageLanguage(text);
        String targetLanguage = sourceLanguage.equals("en") ? "sv" : "en";
        String jsonString = String.format("{\"q\":\"%s\",\"source\":\"%s\",\"target\":\"%s\"}", text, sourceLanguage, targetLanguage);

        try {
            String result = webClient.post()
                    .uri("/translate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(jsonString)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return extractTranslatedText(result);
        } catch (Exception e) {
            // Handle error
            return "";
        }
    }

    private String extractTranslatedText(String response) {
        try {

            ObjectMapper mapper = new ObjectMapper();

            JsonNode rootNode = mapper.readTree(response);

            String translatedText = rootNode.get("translatedText").asText();

            return translatedText;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}