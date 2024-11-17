package br.com.carshop;

import br.com.carshop.controllers.CarController;
import br.com.carshop.models.Car;
import br.com.carshop.services.CarService;
import br.com.carshop.services.FileStorageService;
import br.com.carshop.services.FipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarControllerTest {

    @Mock
    private CarService carService;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private FipeService fipeService;

    @Mock
    private Model model;

    @Mock
    private RedirectAttributes redirectAttributes;

    @InjectMocks
    private CarController carController;

    private Car testCar;
    private List<FipeService.Marca> testBrands;

    @BeforeEach
    void setUp() {
        testCar = new Car();
        testCar.setId(1L);
        testCar.setBrand("Toyota");
        testCar.setModel("Corolla");
        testCar.setPrice(120000.00);

        FipeService.Marca marca1 = new FipeService.Marca();
        marca1.setCodigo("1");
        marca1.setNome("Toyota");

        FipeService.Marca marca2 = new FipeService.Marca();
        marca2.setCodigo("2");
        marca2.setNome("Honda");

        testBrands = Arrays.asList(marca1, marca2);
    }

    @Test
    void listCars_ShouldReturnListView() {
        List<Car> cars = Arrays.asList(testCar, new Car());
        when(carService.listCars()).thenReturn(cars);

        String viewName = carController.listCars(model);

        assertThat(viewName).isEqualTo("cars/list");
        verify(model).addAttribute("cars", cars);
    }

    @Test
    void showNewCarForm_ShouldReturnFormView() {
        when(fipeService.getBrands()).thenReturn(testBrands);

        String viewName = carController.showNewCarForm(model);

        assertThat(viewName).isEqualTo("cars/form");
        verify(model).addAttribute(eq("car"), any(Car.class));
        verify(model).addAttribute("brands", testBrands);
    }

    @Test
    void saveCar_WithValidData_ShouldRedirectToList() {
        MockMultipartFile imageFile = new MockMultipartFile(
                "imageFile", "test.jpg", "image/jpeg", "test image content".getBytes()
        );
        when(fileStorageService.save(any())).thenReturn("/uploads/test.jpg");
        when(carService.createCar(any())).thenReturn(testCar);

        String viewName = carController.saveCar(testCar, imageFile, redirectAttributes);

        assertThat(viewName).isEqualTo("redirect:/cars");
        verify(redirectAttributes).addFlashAttribute(eq("sucesso"), anyString());
    }

    @Test
    void saveCar_WithError_ShouldRedirectToForm() {
        when(carService.createCar(any())).thenThrow(new RuntimeException("Error"));

        String viewName = carController.saveCar(testCar, null, redirectAttributes);

        assertThat(viewName).isEqualTo("redirect:/cars/new");
        verify(redirectAttributes).addFlashAttribute(eq("erro"), anyString());
    }

    @Test
    void showCarDetails_WithValidId_ShouldReturnDetailsView() {
        when(carService.getCar(1L)).thenReturn(testCar);

        String viewName = carController.showCarDetails(1L, model);

        assertThat(viewName).isEqualTo("cars/details");
        verify(model).addAttribute("car", testCar);
    }

    @Test
    void showCarDetails_WithInvalidId_ShouldRedirectToList() {
        when(carService.getCar(999L)).thenReturn(null);

        String viewName = carController.showCarDetails(999L, model);

        assertThat(viewName).isEqualTo("redirect:/cars");
    }

    @Test
    void updateCar_WithValidData_ShouldRedirectToList() {
        when(carService.getCar(1L)).thenReturn(testCar);
        when(carService.updateCar(any())).thenReturn(testCar);

        String viewName = carController.updateCar(1L, testCar, null, redirectAttributes);

        assertThat(viewName).isEqualTo("redirect:/cars");
        verify(redirectAttributes).addFlashAttribute(eq("sucesso"), anyString());
    }

    @Test
    void deleteCar_WithValidId_ShouldRedirectToList() {
        when(carService.getCar(1L)).thenReturn(testCar);
        doNothing().when(carService).deleteCar(1L);

        String viewName = carController.deleteCar(1L, redirectAttributes);

        assertThat(viewName).isEqualTo("redirect:/cars");
        verify(redirectAttributes).addFlashAttribute(eq("sucesso"), anyString());
    }
}