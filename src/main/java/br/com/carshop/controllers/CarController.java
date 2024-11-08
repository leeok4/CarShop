package br.com.carshop.controllers;

import br.com.carshop.models.Car;
import br.com.carshop.services.CarService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

@Controller
@RequestMapping("/car")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping
    public String listCars(Model model) {
        model.addAttribute("cars", carService.listCars());
        return "cars/list";
    }

    @GetMapping("/new")
    public String showNewCarForm(Model model) {
        model.addAttribute("car", new Car());
        return "cars/form";
    }

    @PostMapping("/save")
    public String salvarCarro(@ModelAttribute("car") Car car, RedirectAttributes redirectAttributes) {
        Car savedCar = carService.createCar(car);
        redirectAttributes.addFlashAttribute("sucesso", "Carro salvo com sucesso!");
        return "redirect:/list";
    }


    @GetMapping("/{id}/edit")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        Car car = carService.getCar(id);
        model.addAttribute("cars", car);
        return "cars/form";
    }

    @PostMapping("/{id}/update")
    public String updateCar(@PathVariable("id") Long id, @ModelAttribute("car") Car car, RedirectAttributes redirectAttributes) {
        car.setId(id);
        carService.updateCar(car);
        redirectAttributes.addFlashAttribute("Sucesso", "Carro atualizado com sucesso");
        return "redirect:/list";
    }

    @GetMapping("/{id}/delete")
    public String deleteCar(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        carService.deleteCar(id);
        redirectAttributes.addFlashAttribute("sucesso", "Carro excluído com sucesso");
        return "redirect:/list";
    }
}
