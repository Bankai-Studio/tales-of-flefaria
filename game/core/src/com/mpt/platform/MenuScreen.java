package com.mpt.platform;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mpt.modules.InterfaceModule;

public class MenuScreen extends InterfaceModule {

    private GameScreen gameScreen;

    public MenuScreen() {
        gameScreen = new GameScreen();
        setBackground();
        setup();
    }

    @Override
    protected void setup() {
        Table table = new Table();
        table.setFillParent(true);

        Cell titleCell = table.add(new Label("(tales.of.flefaria)", titleStyle));
        titleCell.row();
        Cell subTitleCell =  table.add(new Label("Welcome to a world of new adventures", smallTextStyle));
        subTitleCell.row();
        subTitleCell.padBottom(50);

        Label playButton = new Label("PLAY", subTitleStyle);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stage.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.run(() -> {
                    ((Game) Gdx.app.getApplicationListener()).setScreen(gameScreen);
                })));
            }
        });

        table.add(playButton).row();
        stage.addActor(table);
    }

    @Override
    public void show() {
        super.show();
        stage.addAction(Actions.sequence(Actions.fadeOut(0f), Actions.fadeIn(1f)));
    }

    @Override
    public void dispose() {
        super.dispose();
        gameScreen.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gameScreen.resize(width, height);
    }
}
