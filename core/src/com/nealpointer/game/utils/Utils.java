package com.nealpointer.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Utils {
    public static float b2dUnits(float value){
        return value / Constants.PPM;
    }
    public static float calcBodyDistance(Body a, Body b){
        return (float) Math.abs(Math.sqrt(Math.pow(a.getPosition().x - b.getPosition().x,2) + Math.pow(a.getPosition().y - b.getPosition().y, 2)));

    }
}
