package com.metagx.foundation.bettergl.model;

import com.metagx.foundation.bettergl.GLGame;
import com.metagx.foundation.bettergl.GLGraphics;
import com.metagx.foundation.bettergl.model.OpenGLObject;

/**
 * Created by Adam on 12/10/13.
 */
public class OpenGLObjectFactory {

    protected GLGame game;
    protected GLGraphics glGraphics;
    protected int glWidth, glHeight;

    protected int screenWidth, screenHeight;

    public OpenGLObjectFactory(GLGame game, GLGraphics glGraphics, int glWidth, int glHeight) {
        this.game = game;
        this.glGraphics = glGraphics;
        this.glWidth = glWidth;
        this.glHeight = glHeight;
    };

    public OpenGLObject createObject(final String assetPath, int width, int height) {
        return new OpenGLObject(game, glGraphics, glWidth, glHeight, width, height) {
            @Override
            public String getAssetPath() {
                return assetPath;
            }

            @Override
            public boolean hasTexture() {
                return true;
            }

            @Override
            public void setBindableVertices() {
                setupTextureVerticies();
            }
        };
    }

    public void setScreenSize(int screenWidth, int screenHeight) {
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }
}
