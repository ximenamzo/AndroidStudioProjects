package com.xime.pong.managers;

import com.xime.pong.App;
import com.xime.pong.screens.AbstractScreen;
import com.xime.pong.screens.GameScreen;

import java.util.HashMap;
import java.util.Map;

public class GameScreenManager {

    public final App app;
    private HashMap<STATE, AbstractScreen> gameScreens;

    public enum STATE {
        MAIN_MENU,
        PLAY,
        SETTINGS
    }

    public GameScreenManager(final App app){
        this.app = app;

        initGameScreens();
        setScreen(STATE.PLAY);
    }

    private void initGameScreens() {
        this.gameScreens = new HashMap<STATE, AbstractScreen>();
        this.gameScreens.put(STATE.PLAY, new GameScreen(app));
    }

    public void setScreen(STATE nextScreen) {
        app.setScreen(gameScreens.get(nextScreen));
    }

    public void dispose() {
        for (AbstractScreen screen : gameScreens.values()) {
            if (screen != null) {
                screen.dispose();
            }
        }
    }
}
