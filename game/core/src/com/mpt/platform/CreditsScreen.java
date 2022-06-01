package com.mpt.platform;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mpt.modules.InterfaceModule;

public class CreditsScreen extends InterfaceModule {
    MenuScreen menuScreen;
    public CreditsScreen(MenuScreen menuScreen) {
        this.menuScreen = menuScreen;
        setup();
    }

    @Override
    protected void setup() {
        Table root = new Table();
        root.setFillParent(true);

        Table top = new Table();
        Label exitLabel = new Label("BACK", subTitleStyle);
        exitLabel.setColor(new Color(80f/255f, 80f/255f, 80f/255f, 255f/255f));
        top.add(exitLabel);
        top.padTop(-250f);
        top.padBottom(250f);
        top.padRight(1400f);

        exitLabel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                exitLabel.addAction(Actions.fadeOut(1f));
                stage.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.run(() -> {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(menuScreen);
                })));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                exitLabel.setText("< BACK >");
                exitLabel.setColor(Color.WHITE);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if(pointer == -1) {
                    exitLabel.setText("BACK");
                    exitLabel.setColor(new Color(80f/255f, 80f/255f, 80f/255f, 255f/255f));
                }
            }
        });

        Table main = new Table();

        Label title = new Label("Game Credits", subTitleStyle);
        Label subtitle = new Label("Tales of Flefaria", smallTextStyle);
        main.add(title).row();
        main.add(subtitle).row();

        Table programmers = new Table();
        programmers.add(createCreditLine("Programmer", "Zaiden")).row();
        programmers.add(createCreditLine("Programmer", "Niz")).row();
        programmers.add(createCreditLine("Programmer", "MattiaSwaga")).row();
        programmers.add(createCreditLine("Programmer", "MasterK")).row();
        programmers.padTop(50f);


        Table mapDesigners = new Table();
        mapDesigners.add(createCreditLine("Map Designer", "MattiaSwaga")).row();

        Table uiDesigners = new Table();
        uiDesigners.add(createCreditLine("Interface Design", "Zaiden")).row();

        Table gameSfx = new Table();
        gameSfx.add(createCreditLine("Interface SFX", "Zaiden")).row();
        gameSfx.add(createCreditLine("Game SFX", "Niz")).row();

        Table animators = new Table();
        animators.add(createCreditLine("Animations and Assets", "MasterK")).row();

        main.add(programmers).row();
        main.add(mapDesigners).row();
        main.add(uiDesigners).row();
        main.add(gameSfx).row();
        main.add(animators).row();

        root.add(top).row();
        root.add(main).row();
        stage.addActor(root);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void show() {
        super.show();
        stage.addAction(Actions.sequence(Actions.alpha(0.0f), Actions.fadeIn(1f)));
    }

    @Override
    public void hide() {
        super.hide();
        stage.addAction(Actions.fadeOut(1f));
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

    private Table createCreditLine(String key, String value) {
        Table table = new Table();
        table.add(new Label(key + "  ", textStyle));
        table.add(new Label("  " + value, smallTextStyle));
        return table;
    }
}
