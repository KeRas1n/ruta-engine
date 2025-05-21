package com.keras1n.core;

import com.keras1n.core.entity.Player;

public interface IGameLogic {


    void init() throws Exception;

    void input();

    void update(float interval, MouseInput mouseInput);

    void render();

    void cleanup();

    Player getPlayer();
}
