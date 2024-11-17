package br.com.carshop.controllers;

import br.com.carshop.services.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

    private final CarService carService;

    public HomeController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/")
    public String home(Model model) {
        logger.info("Acessando página inicial");

        try {
            var featuredCars = carService.getFeaturedCars();
            logger.info("Carros em destaque encontrados: {}", featuredCars.size());
            model.addAttribute("featuredCars", featuredCars);

            return "home/index"; // Mudamos para index.html
        } catch (Exception e) {
            logger.error("Erro ao carregar página inicial", e);
            model.addAttribute("error", "Erro ao carregar os carros em destaque");
            return "error";
        }
    }
}