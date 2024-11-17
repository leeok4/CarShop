package br.com.carshop.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CarAIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private static final String OPENAI_API_URL = "https://api.openai.com/v1/chat/completions";
    private final RestTemplate restTemplate;

    public CarAIService() {
        this.restTemplate = new RestTemplate();
    }


    public String getSuggestedPrice(String brand, String model) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        String prompt = String.format(
                "Baseado no mercado atual, qual seria uma faixa de preço justa para um %s %s? " +
                        "Considere o mercado brasileiro e dê uma resposta curta com uma faixa de valores.",
                brand, model
        );

        ChatRequest request = new ChatRequest();
        request.setModel("gpt-3.5-turbo");
        request.setMessages(new Message[]{
                new Message("system", "Você é um especialista em avaliação de carros usados."),
                new Message("user", prompt)
        });
        request.setTemperature(0.7);

        HttpEntity<ChatRequest> entity = new HttpEntity<>(request, headers);
        ChatResponse response = restTemplate.postForObject(OPENAI_API_URL, entity, ChatResponse.class);

        return response != null && response.getChoices() != null && response.getChoices().length > 0
                ? response.getChoices()[0].getMessage().getContent()
                : "Avaliação não disponível";
    }

    @Data
    private static class ChatRequest {
        private String model;
        private Message[] messages;
        private double temperature;
    }

    @Data
    private static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }

    @Data
    private static class ChatResponse {
        private Choice[] choices;
    }

    @Data
    private static class Choice {
        private Message message;
        @JsonProperty("finish_reason")
        private String finishReason;
    }
}