package com.unlucky.ui.smove;

import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Align;
import com.unlucky.battle.SpecialMove;

/**
 * Tooltip for when the user clicks on their special moveset
 *
 * @author Ming Li
 */
public class SMoveTooltip extends Window {

    private Label.LabelStyle ls;
    private Label desc;

    public SMoveTooltip(Skin skin, Label.LabelStyle ls) {
        super("", skin);
        this.ls = ls;

        desc = new Label("", skin);
        desc.setFontScale(0.5f);
        this.getTitleLabel().setFontScale(0.5f);
        left();
        // fix padding because of scaling
        this.padTop(12);
        this.padLeft(2);
        this.padBottom(4);
        add(desc);
        pack();
        this.setTouchable(Touchable.disabled);
        this.setVisible(false);
        this.setMovable(false);
        this.setOrigin(Align.bottomLeft);
    }

    public void show(SpecialMove smove, float x, float y) {
        this.setPosition(x, y);
        this.setVisible(true);

        this.getTitleLabel().setText(smove.name);
        this.getTitleLabel().setStyle(ls);
        desc.setText(smove.desc);
        pack();
    }

}
