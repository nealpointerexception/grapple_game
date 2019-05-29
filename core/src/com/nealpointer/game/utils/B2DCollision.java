package com.nealpointer.game.utils;

import com.badlogic.gdx.physics.box2d.*;

public class B2DCollision implements ContactListener {
    CollisionHandlers handlers;

    public B2DCollision(){
        handlers = new CollisionHandlers();

    }

    @Override
    public void beginContact(Contact contact) {
        Fixture fA = contact.getFixtureA();
        Fixture fB = contact.getFixtureB();

        if(fA.getUserData() != null && fB.getUserData() != null) {
            System.out.println(fA.getUserData().toString() + "" + fB.getUserData().toString());
            handlers.onContactBegin(fA.getUserData().toString() + "" + fB.getUserData().toString(), fA, fB);
        }

    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
