package com.nealpointer.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.nealpointer.game.blocks.GrappleSurface;
import com.nealpointer.game.utils.Utils;

public class Player extends Sprite{
    BodyDef bodyDef;
    Body body;
    CircleShape circle;
    FixtureDef fixtureDef;
    Fixture fixture;
    Joint distanceJoint;
    DistanceJointDef distanceJointDef;
    GrappleSurface surface;
    GrappleGun grappleGun;


    public boolean alive = true, jumped = false;
    char facing = 'r';

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
        System.out.println(body.getPosition());
        grappleGun = new GrappleGun(world, this);

    }

    public GrappleGun getGrappleGun() {
        return grappleGun;
    }

    Vector2 tempPos;
    public void queGrappleJoint(Vector2 pos){
        tempPos = pos;
    }
    public void createGrappleJoint(Vector2 pos){
        disconnectGrapple();
        int mod = facing == 'r' ? 1 : -1;
        surface = new GrappleSurface(body.getWorld(), pos.x*100, pos.y*100 , 10, 10);

        distanceJointDef = new DistanceJointDef();
        distanceJointDef.bodyA = body;
        distanceJointDef.bodyB = surface.getBody();
        float dist = (float) Math.abs(Math.sqrt(Math.pow(body.getPosition().x - surface.getBody().getPosition().x,2) + Math.pow(body.getPosition().y - surface.getBody().getPosition().y, 2)));
        distanceJointDef.length = dist - 0.3f;
        // distanceJointDef.dampingRatio = 1;
        distanceJointDef.frequencyHz = 3;
        if (distanceJoint == null)
            distanceJoint = body.getWorld().createJoint(distanceJointDef);
    }


    public void disconnectGrapple(){
        if(distanceJoint != null) {
            body.getWorld().destroyJoint(distanceJoint);
            body.getWorld().destroyBody(surface.getBody());
            distanceJoint = null;
        }
    }

    public void move(char dir){
        facing = dir;
        if (dir == 'r'){
            if(body.getLinearVelocity().x < 0.5f){

                body.applyLinearImpulse(new Vector2(0.15f, 0), body.getWorldCenter(), true);
            }
        }
        else if(dir == 'l'){
            if(body.getLinearVelocity().x > -0.5f){

                body.applyLinearImpulse(new Vector2(-0.15f, 0), body.getWorldCenter(), true);
            }
        }
        else{
            System.out.println("Ooopsie doopsie");
        }
    }

    public void update(){
        setX(body.getPosition().x);
        setY(body.getPosition().y);
        if(tempPos != null){
            System.out.println(tempPos);
            createGrappleJoint(tempPos);
            tempPos = null;
        }
        grappleGun.update();
    }
    public void jump(){
        if(!jumped) {
            body.applyLinearImpulse(new Vector2(0, .55f), body.getWorldCenter(), true);
            jumped = true;
        }

    }

    @Override
    public String toString() {
        return "Player";
    }

    public Body getBody() {
        return body;
    }

    public char getFacing() {
        return facing;
    }
}
