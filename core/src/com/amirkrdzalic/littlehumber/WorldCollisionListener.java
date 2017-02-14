package com.amirkrdzalic.littlehumber;

import com.amirkrdzalic.littlehumber.Sprites.Enemy;
import com.amirkrdzalic.littlehumber.Sprites.LittleGuy;
import com.amirkrdzalic.littlehumber.Sprites.TileObject;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by User on 12/23/2016.
 */
//CLASS that BOX2D CALLS FOR COLLISION
public class WorldCollisionListener implements ContactListener{


    //CONTACT LISTENER HAS A IMPLEMENT ALL METHODS.
    @Override
    public void beginContact(Contact contact) {
        //when collisions happen, get the 2 fixtures that we have defined each and all
        Fixture fixture1 = contact.getFixtureA();
        Fixture fixture2 = contact.getFixtureB();

        //collision definition is done BITWISE and OR operator for what has collided
        int collisionDef = fixture1.getFilterData().categoryBits | fixture2.getFilterData().categoryBits;

        //collision with bricks from LitteGuy Head
        if (fixture1.getUserData() == "head" || fixture2.getUserData() == "head")
        {
            //conditional check
            //first fixture is the one moving towards the position of collision, otherwise make it the other fixture
            Fixture head = fixture1.getUserData() == "head" ? fixture1 : fixture2;
            //make the object the other fixture opposite to the head
            Fixture object = head == fixture1 ? fixture2 : fixture1;
            //first get what the object data is, if the object is extending TILEOBJECT than call the following method
            if (object.getUserData() instanceof TileObject)
            {
                ((TileObject) object.getUserData()).onHeadHit();
            }
        }

        //mario death and enemy death
        switch (collisionDef)
        {
            //collision check for the fixtures between 2 objects = the BIT | OR  operator
            case LittleHumber.LITTLEGUY_HEADBIT | LittleHumber.MONEY_BIT:
                //getting a money score added when hit coin brick
                //what is it equal to...
                if(fixture1.getFilterData().categoryBits == LittleHumber.MONEY_BIT)
                {
                    ((TileObject) fixture1.getUserData()).onHeadHit();
                }
                //then it has to be this...
                else
                {
                    ((TileObject) fixture2.getUserData()).onHeadHit();
                }
                break;
            case LittleHumber.ENEMY_HEADBIT | LittleHumber.LITTLEGUY_BIT:
                //killing enemies
                if(fixture1.getFilterData().categoryBits == LittleHumber.ENEMY_HEADBIT)
                {
                    ((Enemy)fixture1.getUserData()).hitOnHead();
                }
                else
                {
                    ((Enemy)fixture2.getUserData()).hitOnHead();
                }
                break;
            case LittleHumber.ENEMY_BIT | LittleHumber.OBJECT_BIT:
                //collision with walls
                if(fixture1.getFilterData().categoryBits == LittleHumber.ENEMY_BIT)
                {
                    ((Enemy) fixture1.getUserData()).reverseVel(true, false);
                }
                else //if(fixture2.getFilterData().categoryBits == LittleHumber.ENEMY_BIT)
                {
                    ((Enemy) fixture2.getUserData()).reverseVel(true, false);
                }
                break;
            //little guy collides with enemy
            case LittleHumber.LITTLEGUY_BIT | LittleHumber.ENEMY_BIT:
                if (fixture1.getFilterData().categoryBits == LittleHumber.LITTLEGUY_BIT) {
                    //exception was thrown FIX IT ASAP
                    ((LittleGuy) fixture1.getUserData()).hit((Enemy)fixture2.getUserData());
                }
                else
                {
                    ((LittleGuy) fixture2.getUserData()).hit((Enemy) fixture1.getUserData());
                }
                break;
            //enemy vs enemy coll.
            case LittleHumber.ENEMY_BIT | LittleHumber.ENEMY_BIT:
                ((Enemy) fixture1.getUserData()).reverseVel(true, false);
                ((Enemy) fixture2.getUserData()).reverseVel(true, false);
                break;
            //collision with CASTLE DOOR
            case LittleHumber.LITTLEGUY_BIT | LittleHumber.ITEM_BIT:
                if (fixture1.getFilterData().categoryBits == LittleHumber.LITTLEGUY_BIT)
                {
                    ((LittleGuy) fixture1.getUserData()).win();
                }
                else
                {
                    ((LittleGuy) fixture2.getUserData()).win();
                }
        }
    }

    //dont need
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
