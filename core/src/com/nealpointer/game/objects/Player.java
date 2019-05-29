package com.nealpointer.game.objects;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.nealpointer.game.blocks.GrappleBlock;
import com.nealpointer.game.blocks.GrappleSurface;
import com.nealpointer.game.utils.Utils;

import java.util.ArrayList;

public class Player extends Sprite{
    Body body, grappleSensor;
    private Joint distanceJoint = null;
    private GrappleSurface surface;
    //private GrappleGun grappleGun;


    public boolean alive = true, jumped = false, connected = false;
    public ArrayList<GrappleBlock> grappleSurfaces;
    char facing = 'r';

    final float DENSITY = 20f, FRICTION = 0.5f, RESTITUTION = 0.2f;
    final float MAX_SPD = 0.6f;

    public Player(World world, float x, float y){
        setCenter(Utils.b2dUnits(3.5f), Utils.b2dUnits(3.5f));
        BodyDef bodyDef = new BodyDef();

        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(Utils.b2dUnits(x), Utils.b2dUnits(y)));

        body = world.createBody(bodyDef);

        CircleShape circle = new CircleShape();
        circle.setRadius(Utils.b2dUnits(7));

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = DENSITY;
        fixtureDef.friction = FRICTION;
        fixtureDef.restitution = RESTITUTION;


        Fixture fixture = body.createFixture(fixtureDef);
        fixture.setUserData(this);

        body.setFixedRotation(true);



        CircleShape aoeCirc = new CircleShape();
        aoeCirc.setRadius(Utils.b2dUnits(80));
        aoeCirc.setPosition(new Vector2(0, 0.6f));

        FixtureDef grappleSensorFixDef = new FixtureDef();
        grappleSensorFixDef.shape = aoeCirc;
        grappleSensorFixDef.isSensor = true;

        Fixture grappleAoeFixture = body.createFixture(grappleSensorFixDef);
        grappleAoeFixture.setUserData("GrappleAOE");

        //grappleGun = new GrappleGun(world, this);

        grappleSurfaces = new ArrayList<>();
        body.setUserData(this);

    }

//    public GrappleGun getGrappleGun() {
//        return grappleGun;
//    }

    Vector2 tempPos;
    public void queGrappleJoint(Vector2 pos){
        tempPos = pos;
    }

    public void createGrappleJoint(){
        //disconnectGrapple();

        // int mod = facing == 'r' ? 1 : -1;
        // surface = new GrappleSurface(body.getWorld(), pos.x*100, pos.y*100 , 10, 10);
        GrappleBlock closestBlock = null;
        float lowestDist = 99999;
        for(int i = 0; i < grappleSurfaces.size(); i++){
            float dist = Utils.calcBodyDistance(body, grappleSurfaces.get(i).getBody());
            if( dist < lowestDist){
                lowestDist = dist;
                closestBlock = grappleSurfaces.get(i);
            }
        }
        if (closestBlock != null) {

            DistanceJointDef distanceJointDef = new DistanceJointDef();
            distanceJointDef.bodyA = body;
            distanceJointDef.bodyB = closestBlock.getBody();
            float dist = Utils.calcBodyDistance(body, closestBlock.getBody());
            distanceJointDef.length = dist - 0.3f;
            // distanceJointDef.dampingRatio = 1;
            distanceJointDef.frequencyHz = 3;
            if (distanceJoint == null)
                distanceJoint = body.getWorld().createJoint(distanceJointDef);

            grappleSurfaces.remove(closestBlock);
            connected = true;
        }
    }


    public void disconnectGrapple(){
        if(distanceJoint != null) {
            body.getWorld().destroyJoint(distanceJoint);
            distanceJoint = null;
            connected = false;
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
//        if(tempPos != null){
//            System.out.println(tempPos);
//            createGrappleJoint(tempPos);
//            tempPos = null;
//        }
        System.out.println(grappleSurfaces);
        //grappleGun.update();
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
