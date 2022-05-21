package com.mpt.platform;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.mpt.modules.InterfaceModule;

public class CreditsScreen extends InterfaceModule {

    public CreditsScreen() {
        setup();
    }

    @Override
    protected void setup() {
        Table main = new Table();
        main.setFillParent(true);

        Label title = new Label("Game Credits", subTitleStyle);
        Label subtitle = new Label("Tales of Flefaria", smallTextStyle);
        main.add(title).row();
        main.add(subtitle).row();

        Table credits = new Table();
        credits.add(createCreditLine("Programmer", "Zaiden")).row();
        credits.add(createCreditLine("Programmer", "Niz")).row();
        credits.add(createCreditLine("Programmer", "MattiaSwaga")).row();
        credits.add(createCreditLine("Programmer", "MasterK")).row();
        credits.padTop(50);

        main.add(credits);

        stage.addActor(main);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
    }

    private Table createCreditLine(String key, String value) {
        Table table = new Table();
        table.add(new Label(key + "  ", textStyle));
        table.add(new Label("  " + value, smallTextStyle));
        return table;
    }

}
