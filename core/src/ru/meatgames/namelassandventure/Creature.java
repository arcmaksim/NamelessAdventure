package ru.meatgames.namelassandventure;

/**
 * Created by MeatBoy on 10.01.2016.
 */
public class Creature {

    private int mMaxHealth;
    private String mName;
    private int mDefence;
    private int mAttack;

    public Creature() {}

    public Creature(String name, int health, int attack, int defence) {
        mName = name;
        mMaxHealth = health;
        mAttack = attack;
        mDefence = defence;
    }

    public String getName() {
        return mName;
    }

    public int getMaxHealth() {
        return mMaxHealth;
    }

    public int getAttack() {
        return mAttack;
    }

    public int getDefence() {
        return mDefence;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setMaxHealth(int health) {
        mMaxHealth = health;
    }

    public void setDefence(int defence) {
        mDefence = defence;
    }

    public void setAttack(int attack) {
        mAttack = attack;
    }

}
