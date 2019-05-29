package com.nealpointer.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.nealpointer.game.utils.Utils;

public class GrappleGun {
    //main weapon item for the player. allows him to grapple to grapple blocks and shoot enemies
     //shoots out a grapple projectile (b2d sensor) that will see what it has hit.
    Body projectileBody, gunBody;
    Fixture projectileFixture, gunFixture;
    Player parent;
    private float d, angle = 90f, angleSpeed = 0.7f, maxShootDuration = 0.5f, curShootDuration = 0, speed = 2; //dimensions
    private boolean isShot = false; // have i already fired?

    public GrappleGun(World world, Player parent){
        d = 5f;
        this.parent = parent;
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(parent.body.getPosition().x+ Utils.b2dUnits(d)/2f,
                parent.body.getPosition().y + Utils.b2dUnits(d)/2f);

        projectileBody = world.createBody(bodyDef);

        PolygonShape boundingBox = new PolygonShape();

        boundingBox.setAsBox(Utils.b2dUnits(d)/2f, Utils.b2dUnits(d)/2f);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = boundingBox;
        fixtureDef.friction = 0.5f;
        fixtureDef.isSensor = true;

        projectileFixture = projectileBody.createFixture(fixtureDef);
        projectileFixture.setUserData(this);
        projectileBody.setUserData(this);
        projectileBody.setGravityScale(0);
        projectileBody.setFixedRotation(true);


        BodyDef gunBodyDef = new BodyDef();
        gunBodyDef.type = BodyDef.BodyType.DynamicBody;
        gunBodyDef.position.set(parent.body.getPosition().x+ Utils.b2dUnits(d)/2f,
                parent.body.getPosition().y + Utils.b2dUnits(d)/2f);

        gunBody = world.createBody(gunBodyDef);

        PolygonShape gunBoundingBox = new PolygonShape();

        gunBoundingBox.setAsBox(Utils.b2dUnits(d)/2f, Utils.b2dUnits(d*5f)/2f);

        FixtureDef gunFixtureDef = new FixtureDef();

        gunFixtureDef.shape = gunBoundingBox;
        gunFixtureDef.friction = 0.5f;
        gunFixtureDef.isSensor = true;

        gunFixture = gunBody.createFixture(gunFixtureDef);
        //gunFixture.setUserData(this);
        //projectileBody.setUserData(this);
        gunBody.setGravityScale(0);
        //projectileBody.setFixedRotation(true);

    }

    public void increaseAngle(){
        if(angle <= 90 && angle > 0)
            angle -= angleSpeed;
    }
    public void decreaseAngle(){
        if(angle < 90 && angle >= 0)
            angle += angleSpeed;

    }
    public void shoot(){
        if(!isShot) {
            projectileBody.setTransform(parent.getBody().getPosition(), 0);
            double rad = Math.toRadians(angle);
            float xSpeed = speed*(float) Math.sin(rad);
            float ySpeed = speed*(float) Math.cos(rad);
            projectileBody.setLinearVelocity(new Vector2(xSpeed, ySpeed));
            isShot = true;
            curShootDuration = 0f;
        }

    }
    public void update(){
        if(isShot){
            curShootDuration += Gdx.graphics.getDeltaTime();
            // System.out.println(curShootDuration);
            if(curShootDuration > maxShootDuration){
                isShot = false;
                projectileBody.setTransform(parent.getBody().getPosition(), 0);
                projectileBody.setLinearVelocity(new Vector2(0, 0));
            }

        }
        gunBody.setTransform(parent.getBody().getPosition(), (float)Math.toRadians(angle)*-1);
    }

    public void resetBullet(){
        //projectileBody.setTransform(parent.getBody().getPosition(), 0);
        isShot = false;
        curShootDuration = 0f;

    }

    public Player getParent() {
        return parent;
    }

    public float getAngle() {
        return angle;
    }

    public Body getProjectileBody() {
        return projectileBody;
    }

    @Override
    public String toString() {
        return "GrappleProj";
    }
}
