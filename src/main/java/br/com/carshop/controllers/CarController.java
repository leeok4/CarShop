package br.com.carshop.controllers;

import br.com.carshop.dto.CarFormDTO;
import br.com.carshop.models.Car;
import br.com.carshop.services.CarAIService;
import br.com.carshop.services.CarService;
import br.com.carshop.services.FileStorageService;
import br.com.carshop.services.FipeService;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/cars")
public class CarController {

    private final Logger logger = LoggerFactory.getLogger(CarController.class);
    private final CarService carService;
    private final FileStorageService fileStorageService;
    private final FipeService fipeService;
    private final CarAIService carAIService;


    public CarController(CarService carService, FileStorageService fileStorageService, FipeService fipeService, CarAIService carAIService) {
        this.carService = carService;
        this.fileStorageService = fileStorageService;
        this.fipeService = fipeService;
        this.carAIService = carAIService;
    }

    @GetMapping
    public String listCars(Model model) {
        try {
            model.addAttribute("cars", carService.listCars());
            return "cars/list";
        } catch (Exception e) {
            logger.error("Erro ao listar carros", e);
            model.addAttribute("erro", "Erro ao carregar lista de carros");
            return "error";
        }
    }

    @GetMapping("/new")
    public String showNewCarForm(Model model) {
        try {
            model.addAttribute("car", new Car());
            model.addAttribute("brands", fipeService.getBrands());
            return "cars/form";
        } catch (Exception e) {
            logger.error("Erro ao mostrar formulário de novo carro", e);
            return "redirect:/cars";
        }
    }

    @PostMapping("/save")
    public String saveCar(@ModelAttribute Car car,
                          @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                          RedirectAttributes redirectAttributes) {
        try {
            car.setId(null);

            if (imageFile != null && !imageFile.isEmpty()) {
                String imagePath = fileStorageService.save(imageFile);
                car.setPicture(imagePath);
            }

            Car savedCar = carService.createCar(car);
            logger.info("Carro salvo com sucesso: {}", savedCar);

            redirectAttributes.addFlashAttribute("sucesso", "Carro cadastrado com sucesso!");
            return "redirect:/cars";

        } catch (Exception e) {
            logger.error("Erro ao salvar carro: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("erro", "Erro ao salvar o carro: " + e.getMessage());
            return "redirect:/cars/new";
        }
    }


    @GetMapping("/{id}")
    public String showCarDetails(@PathVariable Long id, Model model) {
        try {
            Car car = carService.getCar(id);
            if (car == null) {
                model.addAttribute("erro", "Carro não encontrado");
                return "redirect:/cars";
            }
            model.addAttribute("car", car);
            return "cars/details";
        } catch (Exception e) {
            logger.error("Erro ao mostrar detalhes do carro", e);
            return "redirect:/cars";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        try {
            Car car = carService.getCar(id);
            if (car == null) {
                model.addAttribute("erro", "Carro não encontrado");
                return "redirect:/cars";
            }
            model.addAttribute("car", car);
            model.addAttribute("brands", fipeService.getBrands());
            return "cars/form";
        } catch (Exception e) {
            logger.error("Erro ao mostrar formulário de edição", e);
            return "redirect:/cars";
        }
    }

    @PostMapping("/{id}/update")
    public String updateCar(@PathVariable Long id,
                            @ModelAttribute Car car,
                            @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                            RedirectAttributes redirectAttributes) {
        try {
            Car existingCar = carService.getCar(id);
            if (existingCar == null) {
                redirectAttributes.addFlashAttribute("erro", "Carro não encontrado");
                return "redirect:/cars";
            }

            car.setId(id);

            if (imageFile != null && !imageFile.isEmpty()) {
                if (existingCar.getPicture() != null) {
                    fileStorageService.delete(existingCar.getPicture());
                }
                String imagePath = fileStorageService.save(imageFile);
                car.setPicture(imagePath);
            } else {
                car.setPicture(existingCar.getPicture());
            }

            carService.updateCar(car);
            redirectAttributes.addFlashAttribute("sucesso", "Carro atualizado com sucesso!");
            return "redirect:/cars";

        } catch (Exception e) {
            logger.error("Erro ao atualizar carro {}: {}", id, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("erro", "Erro ao atualizar o carro: " + e.getMessage());
            return "redirect:/cars/" + id + "/edit";
        }
    }

    @GetMapping("/{id}/delete")
    public String deleteCar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Car car = carService.getCar(id);
            if (car != null) {
                if (car.getPicture() != null) {
                    fileStorageService.delete(car.getPicture());
                }
                carService.deleteCar(id);
                redirectAttributes.addFlashAttribute("sucesso", "Carro excluído com sucesso!");
            } else {
                redirectAttributes.addFlashAttribute("erro", "Carro não encontrado");
            }
        } catch (Exception e) {
            logger.error("Erro ao deletar carro", e);
            redirectAttributes.addFlashAttribute("erro", "Erro ao excluir o carro: " + e.getMessage());
        }
        return "redirect:/cars";
    }

    @GetMapping("/models/{brandCode}")
    @ResponseBody
    public List<FipeService.Modelo> getModels(@PathVariable String brandCode) {
        return fipeService.getModels(brandCode);
    }

    @PostMapping("/suggest-price")
    @ResponseBody
    public String suggestPrice(@RequestBody PriceSuggestionRequest request) {
        try {
            logger.info("Solicitando sugestão de preço para: {} {}",
                    request.getBrand(), request.getModel());

            return carAIService.getSuggestedPrice(
                    request.getBrand(),
                    request.getModel());
        } catch (Exception e) {
            logger.error("Erro ao obter sugestão de preço", e);
            return "Não foi possível gerar uma sugestão de preço no momento.";
        }
    }

    @Data
    public static class PriceSuggestionRequest {
        private String brand;
        private String model;
    }
}