package com.battleship.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum BattleshipsStatus {
    SHIPUP(1), SHIPDOWN(2);

    private Integer value;
}
