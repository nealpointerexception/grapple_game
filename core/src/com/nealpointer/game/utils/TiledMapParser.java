package com.nealpointer.game.utils;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.nealpointer.game.objects.Ground;

public class TiledMapParser {
    private static final int GROUND_INDEX = 2;
    private static final int GRAPPLE_INDEX = 4;

    public static void parseMap(TiledMap map, World world){
        //ground
        for(MapObject object : map.getLayers().get(GROUND_INDEX).getObjects().getByType(RectangleMapObject.class)){
            Rectangle rectangle = ((RectangleMapObject)object).getRectangle();
            new Ground(world, rectangle);
        }

        //grapple blocks
//        for(MapObject object : map.getLayers().get(GRAPPLE_INDEX).getObjects().getByType(RectangleMapObject.class)) {
//            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
//            new GrappleBlock(world, rectangle);
//        }

    }

}
