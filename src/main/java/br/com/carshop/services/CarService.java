package br.com.carshop.services;

import br.com.carshop.models.Car;
import br.com.carshop.models.repositories.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;


    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<Car> listCars(){
        return carRepository.findAll();
    }

    public Car createCar(Car car){
        return carRepository.save(car);
    }
    public Car getCar(Long id){
        return carRepository.findById(id).orElse(null);
    }

    public Car updateCar(Car car){
        return carRepository.save(car);
    }

    public void deleteCar(Long id){
        carRepository.deleteById(id);
    }
}
