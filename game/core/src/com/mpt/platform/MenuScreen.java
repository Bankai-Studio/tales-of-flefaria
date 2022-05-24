package com.mpt.platform;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mpt.modules.InterfaceModule;

public class MenuScreen extends InterfaceModule {

    private GameScreen gameScreen;
    private CreditsScreen creditsScreen;

    public MenuScreen() {
        setBackground();
        setup();
    }

    @Override
    protected void setup() {
        Table main = new Table();
        main.setFillParent(true);
        Table title = new Table();
        Table options = new Table();

        Label titleLabel = new Label("(tales.of.flefaria)", titleStyle);
        Label subtitleLabel = new Label("Welcome to a world of new adventures", smallTextStyle);
        subtitleLabel.setColor(new Color(180/255f, 180/255f, 180/255f, 255f/255f));
        title.add(titleLabel).row();
        title.add(subtitleLabel).row();
        Cell titleCell = main.add(title);
        titleCell.row();
        titleCell.padBottom(80);

        Label playButton = new Label("PLAY", subTitleStyle);
        playButton.setColor(new Color(80f/255f, 80f/255f, 80f/255f, 255f/255f));
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playButton.addAction(Actions.fadeOut(1f));
                stage.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.run(() -> {
                    gameScreen = new GameScreen();
                    ((Game) Gdx.app.getApplicationListener()).setScreen(gameScreen);
                })));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                playButton.setText("< PLAY >");
                playButton.setColor(Color.WHITE);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                playButton.setText("PLAY");
                playButton.setColor(new Color(80f/255f, 80f/255f, 80f/255f, 255f/255f));
            }
        });

        Label settingsButton = new Label("SETTINGS", subTitleStyle);
        settingsButton.setColor(new Color(80f/255f, 80f/255f, 80f/255f, 255f/255f));
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                settingsButton.addAction(Actions.fadeOut(1f));
                System.out.println("Clicked settings");
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                settingsButton.setText("< SETTINGS >");
                settingsButton.setColor(Color.WHITE);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                settingsButton.setText("SETTINGS");
                settingsButton.setColor(new Color(80f/255f, 80f/255f, 80f/255f, 255f/255f));
            }
        });

        Label creditsButton = new Label("CREDITS", subTitleStyle);
        creditsButton.setColor(new Color(80f/255f, 80f/255f, 80f/255f, 255f/255f));
        creditsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                creditsButton.addAction(Actions.fadeOut(1f));
                stage.addAction(Actions.sequence(Actions.fadeOut(1f), Actions.run(() -> {
                    creditsScreen = new CreditsScreen(getMenuScreen());
                    ((Game) Gdx.app.getApplicationListener()).setScreen(creditsScreen);
                })));
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                creditsButton.setText("< CREDITS >");
                creditsButton.setColor(Color.WHITE);
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                creditsButton.setText("CREDITS");
                creditsButton.setColor(new Color(80f/255f, 80f/255f, 80f/255f, 255f/255f));
            }
        });

        options.add(playButton).row();
        options.add(settingsButton).row();
        options.add(creditsButton).row();
        main.add(options).row();

        stage.addActor(main);
    }

    @Override
    public void show() {
        super.show();
        stage.addAction(Actions.sequence(Actions.fadeOut(0f), Actions.fadeIn(1f)));
    }

    @Override
    public void hide() {
        super.hide();
    }

    @Override
    public void dispose() {
        super.dispose();
        if(gameScreen != null)
            gameScreen.dispose();
        if(creditsScreen != null)
            creditsScreen.dispose();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if(gameScreen != null)
            gameScreen.resize(width, height);
        if(creditsScreen != null)
            creditsScreen.resize(width, height);
    }

    public MenuScreen getMenuScreen() {
        return this;
    }
}
