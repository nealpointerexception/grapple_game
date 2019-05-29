package com.nealpointer.game.blocks;

import com.badlogic.gdx.physics.box2d.*;
import com.nealpointer.game.utils.Utils;

public class GrappleSurface {
    Body myBody;
    public GrappleSurface(World world, float x, float y, float w, float h){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(Utils.b2dUnits(x) + Utils.b2dUnits(w)/2f,
                Utils.b2dUnits(y) + Utils.b2dUnits(h)/2f);

        myBody = world.createBody(bodyDef);

        PolygonShape boundingBox = new PolygonShape();

        boundingBox.setAsBox(Utils.b2dUnits(w)/2f, Utils.b2dUnits(h)/2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boundingBox;
        fixtureDef.friction = 0.5f;
        fixtureDef.isSensor = true;

        myBody.createFixture(fixtureDef);
        myBody.setUserData(this);
    }
    public Body getBody() {
        return myBody;
    }
}
