package com.nealpointer.game.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJoint;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.nealpointer.game.utils.Constants;
import com.nealpointer.game.utils.Utils;

public class GrappleBlock {
    Body myBody;
    Fixture myFixture;

    public GrappleBlock(World world, Rectangle rect){
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(Utils.b2dUnits(rect.getX()) + Utils.b2dUnits(rect.getWidth())/2f,
                Utils.b2dUnits(rect.getY()) + Utils.b2dUnits(rect.getHeight())/2f);

        myBody = world.createBody(bodyDef);

        PolygonShape boundingBox = new PolygonShape();

        boundingBox.setAsBox(Utils.b2dUnits(rect.getWidth())/2f, Utils.b2dUnits(rect.getHeight())/2f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = boundingBox;
        fixtureDef.friction = 0.5f;

        myBody.createFixture(fixtureDef);
        myBody.setUserData(this);
    }



}
