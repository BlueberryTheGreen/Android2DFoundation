package com.metagx.foundation.bettergl.model;

import com.metagx.foundation.bettergl.BindableVertices;
import com.metagx.foundation.bettergl.GLGame;
import com.metagx.foundation.bettergl.GLGraphics;
import com.metagx.foundation.bettergl.Texture;
import com.metagx.foundation.bettergl.model.MotionModel;
import com.metagx.foundation.exception.SuperClassDidNotImplementException;

import java.util.ArrayList;
import java.util.List;

import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Adam on 12/10/13.
 */
public abstract class OpenGLObject {

    protected BindableVertices bindableVertices;
    protected final Texture texture;

    protected final List<MotionModel> motionModelList;

    protected final GLGraphics glGraphics;

    protected int glWorldWidth, glWorldHeight, width, height;

    private final String assetPath;

    public OpenGLObject(GLGame glGame, GLGraphics glGraphics, int glWorldWidth, int glWorldHeight, int width, int height) {
        this(glGame, glGraphics, glWorldWidth, glWorldHeight, width, height, null);
    }

    public OpenGLObject(GLGame glGame, GLGraphics glGraphics, int glWorldWidth, int glWorldHeight, int width, int height, String assetPath) {
        this.assetPath = assetPath;

        if(hasTexture()) {
            this.texture = new Texture(glGame, getAssetPath());
        } else {
            this.texture = null;
        }

        this.glWorldWidth = glWorldWidth;
        this.glWorldHeight = glWorldHeight;
        this.width = width;
        this.height = height;

        this.glGraphics = glGraphics;

        setBindableVertices();

        motionModelList = new ArrayList<MotionModel>();
    }

    public Texture getTexture() {
        return texture;
    }

    public BindableVertices getBindableVertices() {
        return bindableVertices;
    }

    /**
     * Asset path for the Texture
     * @return
     */
    public String getAssetPath() {
        return assetPath;
    }

    public boolean hasTexture() {
        return assetPath != null;
    }

    public void setBindableVertices() {
        if(hasTexture()) {
            setupTextureVerticies();
        } else {
            setupCustomVerticies();
        }
    }

    protected void setupCustomVerticies() throws SuperClassDidNotImplementException {
        throw new SuperClassDidNotImplementException("Must implement this in super class if you need it");
    }

    protected void setupTextureVerticies() {
        bindableVertices = new BindableVertices(glGraphics, 4, 12, false, hasTexture());
        bindableVertices.setVertices(new float[] {
                -width/2, -height/2, 0, 1,
                width/2, -height/2, 1, 1,
                width/2,  height/2, 1, 0,
                -width/2, height/2, 0, 0, }, 0, 16);
        bindableVertices.setIndices(new short[] {0, 1, 2, 2, 3, 0}, 0, 6);
    }

    public MotionModel addObject() {
        MotionModel motionModel = new MotionModel(glWorldWidth, glWorldHeight, width, height);
        motionModelList.add(motionModel);
        return motionModel;
    }

    public List<MotionModel> getMotionModelList() {
        return motionModelList;
    }

    public void draw() {
        GL10 gl = glGraphics.getGL();

        getBindableVertices().bind();
        if(hasTexture()) {
            gl.glEnable(GL10.GL_TEXTURE_2D);
            getTexture().bind();
        }
        for(MotionModel motionModel : getMotionModelList()) {
            gl.glLoadIdentity();

            //+width/2 , +height/2
            gl.glTranslatef(motionModel.position.x, motionModel.position.y, 0);
            gl.glScalef(motionModel.scaleX, motionModel.scaleY, 0);
            getBindableVertices().draw(GL10.GL_TRIANGLES, 0, 6);
        }
        getBindableVertices().unbind();
    }
}