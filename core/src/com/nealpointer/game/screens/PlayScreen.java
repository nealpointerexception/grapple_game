package com.nealpointer.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.nealpointer.game.MainTester;
import com.nealpointer.game.objects.GrappleBlock;
import com.nealpointer.game.objects.GrappleSurface;
import com.nealpointer.game.objects.Player;
import com.nealpointer.game.utils.Constants;
import com.nealpointer.game.utils.TiledMapParser;

public class PlayScreen implements Screen {
    Texture img;
    MainTester game;
    boolean spritesLoaded = false;
    OrthographicCamera gameCam;
    FitViewport screenViewport;


    TiledMap tiledMap;
    OrthogonalTiledMapRenderer mapRenderer;
    TmxMapLoader mapLoader;

    World gameWorld;
    Box2DDebugRenderer dDebugRenderer;

    GrappleSurface tempHook;

    Player player;
    public PlayScreen(MainTester game){
        this.game = game;
        gameCam = new OrthographicCamera();
        screenViewport = new FitViewport(Constants.WIDTH/2f/Constants.PPM, Constants.HEIGHT/2f/Constants.PPM, gameCam);

        gameWorld = new World(new Vector2(0, Constants.GRAVITY), true);
        dDebugRenderer = new Box2DDebugRenderer();


        mapLoader = new TmxMapLoader();
        tiledMap = mapLoader.load("gamemap.tmx");
        mapRenderer = new OrthogonalTiledMapRenderer(tiledMap, 1/Constants.PPM);


        TiledMapParser.parseMap(tiledMap, gameWorld);

        tempHook = new GrappleSurface(gameWorld, 120, 300, 10, 10);


        gameCam.position.set(screenViewport.getWorldWidth()/2f/Constants.PPM+1.2f, screenViewport.getWorldHeight()/2f/Constants.PPM + 2, 1);

        player = new Player(gameWorld, 120, 400);
    }


    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClearColor(75f/255f, 155f/255f, 140f/255f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        if(game.getAssetManager().update()){
            img = game.getAssetManager().get("badlogic.jpg", Texture.class);
            spritesLoaded = true;
        }

        game.getBatch().setProjectionMatrix(gameCam.combined);

        game.getBatch().begin();
        mapRenderer.render();
        if(spritesLoaded) {
            //game.getBatch().draw(img, 0, 0);
        }
        game.getBatch().end();

        dDebugRenderer.render(gameWorld, gameCam.combined);


        update(delta);

    }

    public void update(float delta){


        handleInput();

        gameWorld.step(1/30f, 6, 2);
        player.update();
        mapRenderer.setView(gameCam);




        float lerp = 0.55f;
        Vector3 position = gameCam.position;
        position.x += (player.getX() - position.x) * lerp * delta;
        position.y += (player.getY() - position.y) * lerp * delta;

        gameCam.update();


    }

    public void handleInput(){
        System.out.println(player.getBody().getLinearVelocity());
//        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)){
//            gameCam.position.x += 2/Constants.PPM;
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)){
//            gameCam.position.x -= 2/Constants.PPM;
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
//            gameCam.position.y += 2/Constants.PPM;
//        }
//        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)){
//            gameCam.position.y -= 2/Constants.PPM;
//        }


        if(Gdx.input.isKeyPressed(Input.Keys.D)){
            if(player.getBody().getLinearVelocity().x < 0.5f){

                player.getBody().applyLinearImpulse(new Vector2(0.15f, 0), player.getBody().getWorldCenter(), true);
            }

        }
        else if(Gdx.input.isKeyPressed(Input.Keys.A)){
            if(player.getBody().getLinearVelocity().x > -0.5f){

                player.getBody().applyLinearImpulse(new Vector2(-0.15f, 0), player.getBody().getWorldCenter(), true);
            }
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.W)){
            player.jump();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
            player.disconnectGrapple(gameWorld);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            player.createGrappleJoint(gameWorld, tempHook);
        }

    }

    @Override
    public void resize(int width, int height) {
        screenViewport.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        img.dispose();
    }
}
