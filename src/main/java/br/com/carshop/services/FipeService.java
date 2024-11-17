package br.com.carshop.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.cache.annotation.Cacheable;
import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Service
public class FipeService {
    private static final String FIPE_API = "https://parallelum.com.br/fipe/api/v1/carros";
    private final RestTemplate restTemplate;

    public FipeService() {
        this.restTemplate = new RestTemplate();
    }

    @Cacheable("marcas")
    public List<Marca> getBrands() {
        return List.of(restTemplate.getForObject(FIPE_API + "/marcas", Marca[].class));
    }

    @Cacheable(value = "modelos", key = "#brandId")
    public List<Modelo> getModels(String brandId) {
        ModelsResponse response = restTemplate.getForObject(
                FIPE_API + "/marcas/" + brandId + "/modelos",
                ModelsResponse.class
        );
        return response != null ? response.getModelos() : new ArrayList<>();
    }

    @Data
    public static class Marca {
        private String codigo;
        private String nome;
    }

    @Data
    public static class Modelo {
        private String codigo;
        private String nome;
    }

    @Data
    public static class ModelsResponse {
        private List<Modelo> modelos;
    }
}