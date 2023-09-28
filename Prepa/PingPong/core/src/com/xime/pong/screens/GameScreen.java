package com.xime.pong.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.xime.pong.App;
import com.xime.pong.utils.B2DBodyBuilder;
import com.xime.pong.utils.B2DJointBuilder;

import static com.xime.pong.utils.B2DConstants.PPM;

public class GameScreen extends AbstractScreen{

    //Camara - Camera
    OrthographicCamera camera;

    // Box2D
    World world;
    Box2DDebugRenderer b2dr;

    //Game Bodies
    Body ball;
    Body paddleLeft, goalLeft;
    Body paddleRight, goalRight;

    public GameScreen(final App app) {
        super(app);

        this.camera = new OrthographicCamera();
        this.camera.setToOrtho(false, App.V_WIDTH, App.V_HEIGHT);
    }

    @Override
    public void show() {
        this.b2dr = new Box2DDebugRenderer();
        this.world = new World(new Vector2(0f, 0f), false);

        initArena();
    }

    public void update(float delta) {
        world.step(1f / App.APP_FPS, 6, 2);

        // Get Mouse Position - Move Paddle
        //TODO: Revise
        float mousePosToWorld = -(Gdx.input.getY() - camera.viewportHeight) / PPM;
        float mouseLerp = paddleLeft.getPosition().y + (mousePosToWorld - paddleLeft.getPosition().y) * .2f;

        if (mouseLerp * PPM > camera.viewportHeight - 20) {
            mouseLerp = (camera.viewportHeight - 20f) / PPM;
        } else if (mouseLerp * PPM < 20) {
            mouseLerp = 20f / PPM;
        }
        paddleLeft.setTransform(paddleLeft.getPosition().x, mouseLerp, paddleLeft.getAngle());

        stage.act(delta);
        camera. update();
        app.batch.setProjectionMatrix(camera.combined);
        app.shapeBatch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        b2dr.render(world, camera.combined.cpy().scl(PPM));
        stage.draw();
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
        super.dispose();
        world.dispose();
    }

    private void initArena() {
        createWalls();
        ball = B2DBodyBuilder.createBall(world, camera.viewportWidth / 2, camera.viewportHeight / 2, 6f);
        //ball.setLinearVelocity(1,1);

        //Setup Paddles
        paddleLeft = B2DBodyBuilder.createBox(world, 40, camera.viewportHeight / 2, 10, 40, false, false);
        paddleRight = B2DBodyBuilder.createBox(world, camera.viewportWidth - 40, camera.viewportHeight / 2, 10, 40, false, false);

        //Setup Goals Sensors
        goalLeft = B2DBodyBuilder.createBox(world, 5, camera.viewportHeight / 2, 10, camera.viewportHeight, true, true);
        goalRight = B2DBodyBuilder.createBox(world, camera.viewportWidth - 5, camera.viewportHeight / 2, 10, camera.viewportHeight, true, true);

        //Create Goal To Paddle Joints
        B2DJointBuilder.createPrismaticJoint(world, goalLeft, paddleLeft, camera.viewportHeight / 2,
                -camera.viewportHeight / 2, new Vector2(35 / PPM, 0), new Vector2(0, 0));

        B2DJointBuilder.createPrismaticJoint(world, goalRight, paddleRight, camera.viewportHeight / 2,
                -camera.viewportHeight / 2, new Vector2(-35 / PPM, 0), new Vector2(0, 0));
    }

    private void createWalls() {
        Vector2[] verts = new Vector2[5];
        verts[0] = new Vector2(1f / PPM, 0);
        verts[1] = new Vector2(camera.viewportWidth / PPM, 0);
        verts[2] = new Vector2(camera.viewportWidth / PPM, (camera.viewportHeight - 1) / PPM);
        verts[3] = new Vector2(1f / PPM, (camera.viewportHeight - 1) / PPM);
        verts[4] = new Vector2(1f / PPM, 0);

        B2DBodyBuilder.createChainShape(world, verts);
    }
}
