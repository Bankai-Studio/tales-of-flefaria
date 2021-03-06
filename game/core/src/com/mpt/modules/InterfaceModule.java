package com.mpt.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public abstract class InterfaceModule extends ScreenAdapter implements InputProcessor {
    protected final Stage stage = new Stage(new FitViewport(1600, 900));

    protected final FreeTypeFontGenerator textFreeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("menuAssets/fonts/Mephisto.ttf"));
    protected final FreeTypeFontGenerator titleFreeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("menuAssets/fonts/Barbarian.ttf"));
    protected final Label.LabelStyle titleStyle, subTitleStyle, textStyle, smallTextStyle;
    protected final TextButton.TextButtonStyle textButtonStyle;
    protected final Texture menuBackground = new Texture("menuAssets/backgrounds/MenuBackground.png");

    protected final InputMultiplexer inputMultiplexer;

    public InterfaceModule() {
        titleStyle = setupFont(72, titleFreeTypeFontGenerator);
        subTitleStyle = setupFont(36, textFreeTypeFontGenerator);
        textStyle = setupFont(18, textFreeTypeFontGenerator);
        smallTextStyle = setupFont(12, textFreeTypeFontGenerator);

        textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = subTitleStyle.font;
        textButtonStyle.fontColor = Color.WHITE;

        inputMultiplexer = new InputMultiplexer(stage, this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.getViewport().apply();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {
        super.show();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    @Override
    public void hide() {
        super.hide();
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        super.dispose();
        titleStyle.font.dispose();
        subTitleStyle.font.dispose();
        textStyle.font.dispose();
        smallTextStyle.font.dispose();
        menuBackground.dispose();
        titleFreeTypeFontGenerator.dispose();
        textFreeTypeFontGenerator.dispose();
    }

    protected abstract void setup();

    protected void setBackground() {
        Table table = new Table();
        table.setFillParent(true);
        table.setBackground(new TextureRegionDrawable(menuBackground));
        stage.addActor(table);
    }

    private Label.LabelStyle setupFont(int fontSize, FreeTypeFontGenerator freeTypeFontGenerator) {
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = fontSize;
        fontParameter.incremental = true;

        Label.LabelStyle fontStyle = new Label.LabelStyle();
        fontStyle.font = freeTypeFontGenerator.generateFont(fontParameter);
        fontStyle.font.setUseIntegerPositions(true);
        fontStyle.fontColor = Color.WHITE;

        return fontStyle;
    }

    public static Label.LabelStyle setupFont(int fontSize, Color fontColor) {
        FreeTypeFontGenerator freeTypeFontGenerator = new FreeTypeFontGenerator(Gdx.files.internal("menuAssets/fonts/Mephisto.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        fontParameter.size = fontSize;
        fontParameter.incremental = true;

        Label.LabelStyle fontStyle = new Label.LabelStyle();
        fontStyle.font = freeTypeFontGenerator.generateFont(fontParameter);
        fontStyle.font.setUseIntegerPositions(true);
        fontStyle.fontColor = fontColor;

        return fontStyle;
    }

}
