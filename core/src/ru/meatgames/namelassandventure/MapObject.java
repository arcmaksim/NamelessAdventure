package ru.meatgames.namelassandventure;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by MeatBoy on 09.01.2016.
 */
public class MapObject {

    private boolean mIsPassable_;
    private int mType_;

    public MapObject(int type, boolean isPassable) {
        mType_ = type;
        mIsPassable_ = isPassable;
    }

    public int getType() {
        return mType_;
    }

    public boolean isPassable() {
        return mIsPassable_;
    }

}
