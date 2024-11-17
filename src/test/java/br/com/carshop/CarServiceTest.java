package br.com.carshop;

import br.com.carshop.models.Car;
import br.com.carshop.models.repositories.CarRepository;
import br.com.carshop.services.CarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarServiceTest {

    @Mock
    private CarRepository carRepository;

    @InjectMocks
    private CarService carService;

    private Car testCar;

    @BeforeEach
    void setUp() {
        testCar = new Car();
        testCar.setId(1L);
        testCar.setBrand("Toyota");
        testCar.setModel("Corolla");
        testCar.setPrice(120000.00);
    }

    @Test
    void createCar_WithValidData_ShouldSaveAndReturnCar() {
        when(carRepository.save(any(Car.class))).thenReturn(testCar);

        Car savedCar = carService.createCar(testCar);

        assertThat(savedCar).isNotNull();
        assertThat(savedCar.getBrand()).isEqualTo("Toyota");
        assertThat(savedCar.getModel()).isEqualTo("Corolla");
        verify(carRepository, times(1)).save(any(Car.class));
    }

    @Test
    void createCar_WithExistingId_ShouldRemoveIdAndSave() {
        Car carWithId = new Car();
        carWithId.setId(1L);
        carWithId.setBrand("Honda");
        carWithId.setModel("Civic");

        when(carRepository.save(any(Car.class))).thenReturn(carWithId);

        Car savedCar = carService.createCar(carWithId);

        assertThat(savedCar).isNotNull();
        verify(carRepository, times(1)).save(argThat(car -> car.getId() == null));
    }

    @Test
    void listCars_ShouldReturnAllCars() {
        Car car2 = new Car();
        car2.setId(2L);
        car2.setBrand("Honda");
        car2.setModel("Civic");

        when(carRepository.findAll()).thenReturn(Arrays.asList(testCar, car2));

        List<Car> cars = carService.listCars();

        assertThat(cars).hasSize(2);
        assertThat(cars).extracting("brand")
                .containsExactlyInAnyOrder("Toyota", "Honda");
        verify(carRepository, times(1)).findAll();
    }

    @Test
    void getCar_WithValidId_ShouldReturnCar() {
        when(carRepository.findById(1L)).thenReturn(Optional.of(testCar));

        Car foundCar = carService.getCar(1L);

        assertThat(foundCar).isNotNull();
        assertThat(foundCar.getBrand()).isEqualTo("Toyota");
        verify(carRepository, times(1)).findById(1L);
    }

    @Test
    void getCar_WithInvalidId_ShouldReturnNull() {
        when(carRepository.findById(999L)).thenReturn(Optional.empty());

        Car foundCar = carService.getCar(999L);

        assertThat(foundCar).isNull();
        verify(carRepository, times(1)).findById(999L);
    }

    @Test
    void updateCar_WithValidData_ShouldUpdateAndReturnCar() {
        when(carRepository.save(any(Car.class))).thenReturn(testCar);

        Car updatedCar = carService.updateCar(testCar);

        assertThat(updatedCar).isNotNull();
        assertThat(updatedCar.getBrand()).isEqualTo("Toyota");
        verify(carRepository, times(1)).save(testCar);
    }

    @Test
    void updateCar_WithNullId_ShouldThrowException() {
        Car carWithoutId = new Car();
        carWithoutId.setBrand("Toyota");
        carWithoutId.setModel("Corolla");

        assertThatThrownBy(() -> carService.updateCar(carWithoutId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID não pode ser null");
    }

    @Test
    void deleteCar_WithValidId_ShouldDeleteCar() {
        doNothing().when(carRepository).deleteById(1L);

        carService.deleteCar(1L);

        verify(carRepository, times(1)).deleteById(1L);
    }

    @Test
    void getFeaturedCars_ShouldReturnTopThreeCars() {
        List<Car> featuredCars = Arrays.asList(testCar, new Car(), new Car());
        when(carRepository.findAll()).thenReturn(featuredCars);

        List<Car> result = carService.getFeaturedCars();

        assertThat(result).hasSize(3);
        verify(carRepository, times(1)).findAll();
    }
}