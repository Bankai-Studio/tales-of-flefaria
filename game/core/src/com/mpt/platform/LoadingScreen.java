package com.mpt.platform;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mpt.modules.InterfaceModule;
import com.mpt.modules.MusicModule;

public class LoadingScreen extends InterfaceModule {

    GameScreen gameScreen;

    public LoadingScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        setup();
    }

    @Override
    protected void setup() {
        Table main = new Table();
        main.setFillParent(true);
        main.add(new Label("< LOADING >", textStyle));
        main.addAction(Actions.fadeOut(0f));
        main.addAction(Actions.sequence(Actions.fadeIn(3f), Actions.fadeOut(3f), Actions.run(() ->  {
            gameScreen.clearBodies();
            gameScreen.loadMap(gameScreen.selectNextMap(), gameScreen.selectNexCharacter());
            ((Game) Gdx.app.getApplicationListener()).setScreen(gameScreen);
        })));

        stage.addActor(main);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void show() {
        super.show();
        stage.addAction(Actions.fadeIn(1f));
    }

    @Override
    public void hide() {
        super.hide();
        stage.addAction(Actions.fadeOut(1f));
        dispose();
    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}
