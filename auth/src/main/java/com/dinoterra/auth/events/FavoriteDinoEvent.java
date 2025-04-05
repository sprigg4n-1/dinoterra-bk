package com.dinoterra.auth.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDinoEvent {
    private Long userId;
    private Long dinoId;
}
