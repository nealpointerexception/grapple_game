package com.nealpointer.game.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.nealpointer.game.blocks.GrappleBlock;
import com.nealpointer.game.objects.GrappleGun;
import com.nealpointer.game.objects.Player;

import java.util.HashMap;
import java.util.function.BiConsumer;

public class CollisionHandlers {
    private HashMap<String, BiConsumer<Fixture, Fixture>> beginHandlerMap, endHandlerMap;
    public CollisionHandlers() {
        beginHandlerMap = new HashMap<String, BiConsumer<Fixture, Fixture>>();
        endHandlerMap = new HashMap<String, BiConsumer<Fixture, Fixture>>();

        beginHandlerMap.put("GroundPlayer", (a, b) -> handleGroundPlayer(a, b));
        beginHandlerMap.put("PlayerGround", (a, b) -> handleGroundPlayer(b, a));
        beginHandlerMap.put("GrappleProjGrappleBlock", (a, b) -> createGrapple(a, b));
        beginHandlerMap.put("GrappleBlockGrappleProj", (a, b) -> createGrapple(b, a));
        beginHandlerMap.put("GrappleAOEGrappleBlock", (a,b) -> addGrappleTarget(a, b));
        beginHandlerMap.put("GrappleBlockGrappleAOE", (a,b) -> addGrappleTarget(b, a));

        endHandlerMap.put("GrappleAOEGrappleBlock", (a,b) -> removeGrappleTarget(a, b));
        endHandlerMap.put("GrappleBlockGrappleAOE", (a,b) -> removeGrappleTarget(b, a));
    }

    private void handleGroundPlayer(Fixture ground, Fixture player){
        ((Player)player.getUserData()).jumped = false;

    }

    public void addGrappleTarget(Fixture player, Fixture block){
        if(!(((Player)player.getBody().getUserData()).grappleSurfaces.contains((GrappleBlock)block.getUserData())))
            ((Player)player.getBody().getUserData()).grappleSurfaces.add((GrappleBlock)block.getUserData());
    }

    public void removeGrappleTarget(Fixture player, Fixture block){
        ((Player)player.getBody().getUserData()).grappleSurfaces.remove((GrappleBlock)block.getUserData());
    }

    public void onContactBegin(String key, Fixture a, Fixture b) {
        if(beginHandlerMap.containsKey(key))
            beginHandlerMap.get(key).accept(a, b);
    }

    public void createGrapple(Fixture gun, Fixture block){
        Vector2 colPos = ((GrappleGun)gun.getUserData()).getProjectileBody().getPosition();
        System.out.println(colPos);
        ((GrappleGun)gun.getUserData()).getParent().queGrappleJoint(colPos);
        ((GrappleGun)gun.getUserData()).resetBullet();

    }

    public void onContactEnd(String key, Fixture a, Fixture b) {
        if(endHandlerMap.containsKey(key)) {
            endHandlerMap.get(key).accept(a, b);
        }
    }

}
