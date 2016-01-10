package ru.meatgames.namelassandventure;

/**
 * Created by MeatBoy on 10.01.2016.
 */
public class CreatureInstance extends Creature{

    private int mCurrentHealth;
    private int mPositionX_;
    private int mPositionY_;
    private int mID;
    private int mType;
    private static int ID = 0;

    public CreatureInstance() {
        init();
    }

    public CreatureInstance(int type) {
        mType = type;
        init();
    }

    public CreatureInstance(int type, int posX, int posY) {
        mPositionX_ = posX;
        mPositionY_ = posY;
        mType = type;
        init();
    }

    private void init() {
        mID = ID++;
    }

    public int getPositionX() {
        return mPositionX_;
    }

    public int getPositionY() {
        return mPositionY_;
    }

    public int getID() {
        return mID;
    }

    public int getType() {
        return mType;
    }

    public int getCurrentHealth() {
        return mCurrentHealth;
    }

    public void setCurrentHealth(int health) {
        mCurrentHealth = health;
    }

    public void changeCurrentHealth(int value) {
        mCurrentHealth += value;
    }

}