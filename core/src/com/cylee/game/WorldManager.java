package com.cylee.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.JointDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.cylee.game.entity.BodyUserData;
import com.cylee.game.util.Config;

public class WorldManager implements ContactListener {
	private DroppingBrickContactListener mListener;
	private World mWorld;
	public WorldManager() {
		float gy = -15;
		mWorld = new World(new Vector2(0, gy), true);
		mWorld.setContactListener(this);
	}
	
	public void step(float timeStep) {
		mWorld.step(timeStep, 10, 10);
	}
	
	public void setDroppingBrickContactListener(DroppingBrickContactListener listener) {
		mListener = listener;
	}
	
	public Body createBrick(float x, float y, float w, float h) {
		BodyDef bd = new BodyDef();
		bd.position.set(x,y);
		bd.type = BodyType.DynamicBody;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(w / 2, h / 2);
		
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.friction = 0.5f;
		fd.restitution = 0.4f;
		fd.density = 0.5f;
		Body body = mWorld.createBody(bd);
		body.createFixture(fd);
		return body;
	}
	
	public Body createGround(float x, float y, float w, float h) {
		BodyDef bd = new BodyDef();
		bd.position.set(x,y);
		bd.type = BodyType.StaticBody;
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(w / 2, h / 2);
		
		FixtureDef fd = new FixtureDef();
		fd.shape = shape;
		fd.friction = 0.5f;
		fd.restitution = 0.2f;
		Body body = mWorld.createBody(bd);
		body.createFixture(fd);
		return body;
	}
	
	public Joint createJoint(JointDef def) {
		return mWorld.createJoint(def);
	}
	
	public void destroyJoint(Joint joint) {
		mWorld.destroyJoint(joint);
	}
	
	public void dispose() {
		if (mWorld != null) {
			mWorld.dispose();
			mWorld = null;
		}
	}
	
	public World debugWorld() {
		return mWorld;
	}

	
	@Override
	public void beginContact(Contact concat) {
		if (mListener != null) {
			Body bodyA = concat.getFixtureA().getBody();
			Body bodyB = concat.getFixtureB().getBody();
			Object uda = bodyA.getUserData();
			Object udb = bodyB.getUserData();
			if (uda instanceof BodyUserData && udb instanceof BodyUserData) {
				String identifyA = ((BodyUserData)(uda)).mIdentify;
				String identifyB = ((BodyUserData)(udb)).mIdentify;
				((BodyUserData)(uda)).mDropping = false;
				((BodyUserData)(udb)).mDropping = false;
				if (Config.USER_DATA_DROPPING_BRICK_IDENDITY.equals(identifyA)) {
					if (Config.USER_DATA_GROUND_IDENDITY.equals(identifyB)) {
						mListener.contactGround(bodyA, bodyB);
					} else {
						mListener.contactBrick(bodyA, bodyB);
					}
				} else {
					if (Config.USER_DATA_DROPPING_BRICK_IDENDITY.equals(identifyB)) {
						mListener.contactGround(bodyA, bodyB);
					}
				}
			}
		}
	}

	@Override
	public void endContact(Contact concat) {
	}

	@Override
	public void postSolve(Contact concat, ContactImpulse concatImpulse) {
	}

	@Override
	public void preSolve(Contact concat, Manifold manifold) {
	}
	
	public interface DroppingBrickContactListener {
		void contactBrick(Body droppingBrick, Body other);
		void contactGround(Body droppingBrick, Body ground);
	}
}
