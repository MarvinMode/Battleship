package com.battleship.controller.dto;

import com.battleship.model.Player;
import lombok.Data;

@Data
public class ConnectRequest {
    private Player player;
    private String gameId;
}
