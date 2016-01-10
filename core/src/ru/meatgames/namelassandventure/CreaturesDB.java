package ru.meatgames.namelassandventure;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Created by MeatBoy on 10.01.2016.
 */
public class CreaturesDB {

    private static CreaturesDB mDB;
    private ArrayList<Creature> mCreatures_;
    private ArrayList<Texture> mTextures_; //use TextureAtlas

    public static final int BAT = 0;
    public static final int OOZE = 1;
    public static final int SKELETON = 2;

    private CreaturesDB() {
        mCreatures_ = new ArrayList<Creature>();
        mTextures_ = new ArrayList<Texture>();
    }

    public static CreaturesDB getInstance() {
        if (mDB == null)
            mDB = new CreaturesDB();
        return mDB;
    }

    public Creature getCreature(int type) {
        return mCreatures_.get(type);
    }

}
