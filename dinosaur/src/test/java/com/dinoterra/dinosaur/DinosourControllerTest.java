package com.dinoterra.dinosaur;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.dinoterra.dinosaur.dino.DinoRequest;
import com.dinoterra.dinosaur.dino.DinoResponse;
import com.dinoterra.dinosaur.dino.DinoService;
import com.dinoterra.dinosaur.dino.enums.DinoDiet;
import com.dinoterra.dinosaur.dino.enums.DinoPeriod;
import com.dinoterra.dinosaur.dino.enums.DinoType;
import com.dinoterra.dinosaur.image.ImageResponse;
import com.dinoterra.dinosaur.image.ImageService;
import com.dinoterra.dinosaur.location.FoundLocationResponse;
import com.dinoterra.dinosaur.location.FoundLocationService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class DinosourControllerTest {
    @Mock
    private DinoService dinoService;

    @Mock
    private ImageService imageService;

    @Mock
    private FoundLocationService locationService;

    @InjectMocks
    private DinosourController dinosourController;

    private DinoRequest dinoRequest;
    private DinoResponse dinoResponse;

    @BeforeEach
    void setUp() {

        System.out.println("=================== Controller ===================");

        dinoRequest = new DinoRequest(
                "T-Rex",
                "Tyrannosaurus rex",
                "A large carnivorous dinosaur",
                DinoType.Theropod,
                12.3,
                8000,
                DinoPeriod.Jurassic,
                "66 million years ago",
                "The Cretaceous period lasted from about 145 to 66 million years ago.",
                DinoDiet.Carnivores,
                "The T-Rex was a meat-eating predator.");

        dinoResponse = new DinoResponse(
                1L,
                "T-Rex",
                "Tyrannosaurus rex",
                "A large carnivorous dinosaur",
                DinoType.Theropod,
                12.3,
                8000,
                DinoPeriod.Jurassic,
                "66 million years ago",
                "The Cretaceous period lasted from about 145 to 66 million years ago.",
                DinoDiet.Carnivores,
                "The T-Rex was a meat-eating predator.",
                List.of(),
                List.of());

        System.out.println("DinoRequest and DinoResponse objects initialized.");
    }

    @Test
    void shouldAddDino() {
        // Arrange
        System.out.println("Executing shouldAddDino test...");
        when(dinoService.createDino(dinoRequest)).thenReturn(dinoResponse);
        System.out.println("Mocked dinoService.createDino() to return dinoResponse.");

        // Act
        DinoResponse response = dinosourController.addDino(dinoRequest);
        System.out.println("Called dinosourController.addDino()");

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("T-Rex");
        assertThat(response.latinName()).isEqualTo("Tyrannosaurus rex");
        assertThat(response.typeOfDino()).isEqualTo(DinoType.Theropod);
        assertThat(response.length()).isEqualTo(12.3);
        assertThat(response.weight()).isEqualTo(8000);
        assertThat(response.period()).isEqualTo(DinoPeriod.Jurassic);
        assertThat(response.diet()).isEqualTo(DinoDiet.Carnivores);
        assertThat(response.images()).isEmpty();
        assertThat(response.foundLocations()).isEmpty();

        System.out.println("Assertions completed successfully for addDino.");
        System.out.println("ID: " + response.id());
        System.out.println("Name: " + response.name());
        System.out.println("Latin Name: " + response.latinName());
        System.out.println("Description: " + response.description());
        System.out.println("Diet: " + response.diet());
        System.out.println("Period: " + response.period());
        System.out.println("Length: " + response.length());
        System.out.println("Weight: " + response.weight());

        verify(dinoService).createDino(dinoRequest);
        System.out.println("Verified that dinoService.createDino() was called.");
    }

    @Test
    void shouldChangeDino() {
        // Arrange
        Long dinoId = 1L;

        System.out.println("Executing shouldChangeDino test...");
        System.out.println("Mocking dinoService.changeDino() for dinoId: " + dinoId);
        doNothing().when(dinoService).changeDino(eq(dinoId), any(DinoRequest.class));
        System.out.println("Mocked dinoService.changeDino() to do nothing.");

        // Act
        String response = dinosourController.changeDino(dinoId, dinoRequest);
        System.out.println("Called dinosourController.changeDino()");

        // Assert
        assertThat(response).isEqualTo("dino successfully changed");
        System.out.println("Assertion completed successfully for changeDino.");

        verify(dinoService).changeDino(dinoId, dinoRequest);
        System.out.println("Verified that dinoService.changeDino() was called.");
    }

    @Test
    void shouldGetAllDinos() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 12);
        Page<DinoResponse> page = new PageImpl<>(List.of(dinoResponse));
        when(dinoService.getAllDinos(pageable, null, null, null, null, null)).thenReturn(page);
        System.out.println("Mocked dinoService.getAllDinos() to return a page of dinos.");

        // Act
        Page<DinoResponse> response = dinosourController.getAllDinos(0, 12, null, null, null, null, null);
        System.out.println("Called dinosourController.getAllDinos()");

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.getContent()).isNotEmpty();
        assertThat(response.getContent().get(0).name()).isEqualTo("T-Rex");

        DinoResponse firstDino = response.getContent().get(0);
        System.out.println("First Dino in the page:");
        System.out.println("ID: " + firstDino.id());
        System.out.println("Name: " + firstDino.name());
        System.out.println("Latin Name: " + firstDino.latinName());
        System.out.println("Description: " + firstDino.description());
        System.out.println("Diet: " + firstDino.diet());
        System.out.println("Period: " + firstDino.period());
        System.out.println("Length: " + firstDino.length());
        System.out.println("Weight: " + firstDino.weight());

        System.out.println("Assertions completed successfully for getAllDinos.");
        verify(dinoService).getAllDinos(pageable, null, null, null, null, null);
        System.out.println("Verified that dinoService.getAllDinos() was called.");
    }
}
