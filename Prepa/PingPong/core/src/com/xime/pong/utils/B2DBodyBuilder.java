package com.xime.pong.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import sun.security.provider.certpath.Vertex;

import static com.xime.pong.utils.B2DConstants.PPM;

public final class B2DBodyBuilder {

    private B2DBodyBuilder() {}

    public static Body createBox(World world, float x, float y, float width, float height, boolean isStatic, boolean isSensor) {
        Body body;

        BodyDef bDef = new BodyDef();
        bDef.type = isStatic? BodyDef.BodyType.StaticBody : BodyDef.BodyType.DynamicBody;
        bDef.fixedRotation = true;
        bDef.position.set(x / PPM, y / PPM);
        body = world.createBody(bDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1.0f;
        fDef.isSensor = isSensor;
        body.createFixture(fDef);
        shape.dispose();
        return body;
    }

    public static Body createBall(World world, float x, float y, float radius) {
        Body body;
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.DynamicBody;
        bDef.fixedRotation = true;
        bDef.position.set(x / PPM, y / PPM);
        body = world.createBody(bDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = shape;
        fDef.density = 1.0f;
        body.createFixture(fDef);
        shape.dispose();
        return body;
    }

    public static Body createChainShape(World world, Vector2[] verts) {
        Body body;
        BodyDef bDef = new BodyDef();
        bDef.type = BodyDef.BodyType.StaticBody;
        body = world.createBody(bDef);

        ChainShape shape = new ChainShape();
        shape.createChain(verts);

        body.createFixture(shape, 1.0f);
        shape.dispose();
        return body;
    }
}
