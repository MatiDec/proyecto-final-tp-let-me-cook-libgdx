package com.hebergames.letmecook;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.hebergames.letmecook.pantallas.PantallaMenu;
import com.hebergames.letmecook.utiles.Recursos;
import com.hebergames.letmecook.utiles.Render;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class LetMeCookPrincipal extends Game {

    @Override
    public void create() {
        Render.batch = new SpriteBatch();
        this.setScreen(new PantallaMenu());
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        Render.batch.dispose();
    }
}
