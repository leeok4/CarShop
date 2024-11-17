package br.com.carshop.services;

import br.com.carshop.models.Car;
import br.com.carshop.models.repositories.CarRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;

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
        if (car.getId() != null) {
            throw new IllegalArgumentException("ID deve ser null para novo carro");
        }
        return carRepository.save(car);
    }

    public Car updateCar(Car car) {
        if (car.getId() == null) {
            throw new IllegalArgumentException("ID não pode ser null para atualização");
        }
        return carRepository.save(car);
    }

    public Car getCar(Long id) {
        return carRepository.findById(id).orElse(null);
    }

    public void deleteCar(Long id) {
        carRepository.deleteById(id);
    }
}