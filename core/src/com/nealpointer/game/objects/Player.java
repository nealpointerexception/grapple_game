package com.nealpointer.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.nealpointer.game.utils.Constants;
import com.nealpointer.game.utils.Utils;

public class Player extends Sprite{
    BodyDef bodyDef;
    Body body;
    CircleShape circle;
    FixtureDef fixtureDef;
    Fixture fixture;
    Joint distanceJoint;
    DistanceJointDef distanceJointDef;


    boolean alive = true;
    final float DENSITY = 20f, FRICTION = 0.5f, RESTITUTION = 0.2f;
    final float MAX_SPD = 0.6f;

    public Player(World world, float x, float y){
        setCenter(Utils.b2dUnits(3.5f), Utils.b2dUnits(3.5f));
        bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Utils.b2dUnits(x), Utils.b2dUnits(y)));

        body = world.createBody(bodyDef);

        circle = new CircleShape();
        circle.setRadius(Utils.b2dUnits(7));

        fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
        fixtureDef.restitution = RESTITUTION;


        fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        body.setFixedRotation(true);






    }
    public void createGrappleJoint(World world, GrappleSurface surface){
        distanceJointDef = new DistanceJointDef();
        distanceJointDef.bodyA = body;
        distanceJointDef.bodyB = surface.myBody;
        float dist = (float) Math.abs(Math.sqrt(Math.pow(body.getPosition().x - surface.myBody.getPosition().x,2) + Math.pow(body.getPosition().y - surface.myBody.getPosition().y, 2)));
        distanceJointDef.length = dist - 0.1f;
        // distanceJointDef.dampingRatio = 1;
        distanceJointDef.frequencyHz = 3;
        distanceJoint = world.createJoint(distanceJointDef);
    }


    public void disconnectGrapple(World world){
        if(distanceJoint != null) {
            world.destroyJoint(distanceJoint);
            distanceJoint = null;
        }
    }

    public void update(){
        setX(body.getPosition().x);
        setY(body.getPosition().y);
    }
    public void jump(){
        body.applyLinearImpulse(new Vector2(0, .55f), body.getWorldCenter(), true);
    }

    public Body getBody() {
        return body;
    }
}
