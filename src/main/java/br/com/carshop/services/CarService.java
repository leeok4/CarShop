package br.com.carshop.services;

import br.com.carshop.models.Car;
import br.com.carshop.models.repositories.CarRepository;
import org.slf4j.Logger;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(CarService.class);

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> listCars() {
        return carRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
    }

    public List<Car> getFeaturedCars() {
        return carRepository.findAll(
                PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "id"))
        ).getContent();
    }

    public Car createCar(Car car) {
        try {
            if (car.getId() != null) {
                logger.warn("Tentativa de criar carro com ID já existente. Removendo ID.");
                car.setId(null);
            }
            return carRepository.save(car);
        } catch (Exception e) {
            logger.error("Erro ao criar carro: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao salvar o carro", e);
        }
    }

    public Car getCar(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    public Car updateCar(Car car) {
        try {
            if (car.getId() == null) {
                throw new RuntimeException("ID do carro não informado");
            }
            return carRepository.save(car);
        } catch (Exception e) {
            logger.error("Erro ao atualizar carro: {}", e.getMessage(), e);
            throw new RuntimeException("Erro ao atualizar o carro", e);
        }
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}