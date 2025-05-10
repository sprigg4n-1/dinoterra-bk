package com.dinoterra.dinosaur;

import com.dinoterra.dinosaur.dino.DinoResponse;
import com.dinoterra.dinosaur.dino.DinoService;
import com.dinoterra.dinosaur.dino.enums.DinoDiet;
import com.dinoterra.dinosaur.dino.enums.DinoPeriod;
import com.dinoterra.dinosaur.dino.enums.DinoType;
import com.dinoterra.dinosaur.image.ImageService;
import com.dinoterra.dinosaur.location.FoundLocationService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DinosourController.class)
public class DinoControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private DinoService dinoService;

        @MockBean
        private ImageService imageService;

        @MockBean
        private FoundLocationService locationService;

        private DinoResponse buildMockDino(Long id, String name) {
                return new DinoResponse(
                                id,
                                name,
                                name + " Latinus",
                                "Test description",
                                DinoType.Theropod,
                                10.0,
                                5000,
                                DinoPeriod.Cretaceous,
                                "66 million years ago",
                                "The final period of the Mesozoic era",
                                DinoDiet.Carnivores,
                                "Carnivore diet",
                                List.of(),
                                List.of());
        }

        @Test
        void testGetFiveRandomDinos() throws Exception {
                when(dinoService.getFiveRandomDinos()).thenReturn(List.of(
                                buildMockDino(1L, "T-Rex"),
                                buildMockDino(2L, "Triceratops")));

                mockMvc.perform(get("/api/v1/dinosour/fiveRandomDinos"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].name").value("T-Rex"))
                                .andExpect(jsonPath("$[1].latinName").value("Triceratops Latinus"));
        }

        @Test
        void testIsFavoriteDino() throws Exception {
                when(dinoService.isFavoriteDino(1L, 2L)).thenReturn(true);

                mockMvc.perform(get("/api/v1/dinosour/isFavoriteDino")
                                .param("userId", "1")
                                .param("dinoId", "2"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("true"));
        }

        @ParameterizedTest
        @ValueSource(longs = { 1, 2, 3 })
        void testGetDinoById(long id) throws Exception {
                when(dinoService.getDino(id)).thenReturn(buildMockDino(id, "Dino-" + id));

                mockMvc.perform(get("/api/v1/dinosour/dinos/{id}", id))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Dino-" + id))
                                .andExpect(jsonPath("$.id").value(id));
        }

        @Test
        void testGetAllDinos() throws Exception {
                var dinoPage = new PageImpl<>(List.of(buildMockDino(1L, "T-Rex")));
                when(dinoService.getAllDinos(PageRequest.of(0, 12), null, null, null, null, null))
                                .thenReturn(dinoPage);

                mockMvc.perform(get("/api/v1/dinosour/dinos"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1))
                                .andExpect(jsonPath("$.content[0].name").value("T-Rex"));
        }
}
