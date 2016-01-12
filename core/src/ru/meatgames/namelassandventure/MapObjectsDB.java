package ru.meatgames.namelassandventure;

import com.badlogic.gdx.graphics.Texture;

import java.util.ArrayList;

/**
 * Created by MeatBoy on 09.01.2016.
 */
public class MapObjectsDB {

    //use SpriteBatch

    private static MapObjectsDB mDB_;
    private ArrayList<MapObject> mMapObjects_;
    private ArrayList<Texture> mTextures_;

    public static final int WALL = 0;
    public static final int STONEWALL = 1;
    public static final int BOOK_SHELF = 2;
    public static final int BOOK_SHELF_EMPTY = 3;
    public static final int EMPTY = -1;

    private MapObjectsDB() {
        mMapObjects_ = new ArrayList<MapObject>();
        mTextures_ = new ArrayList<Texture>();
    }

    public static MapObjectsDB getInstance() {
        if (mDB_ == null)
            mDB_ = new MapObjectsDB();
        return mDB_;
    }

    public void setObjects(ArrayList<MapObject> objects) {
        mMapObjects_ = objects;
    }

    public void addObject(MapObject object, Texture texture) {
        mMapObjects_.add(object);
        mTextures_.add(texture);
    }

    public MapObject getObject(int index) {
        return mMapObjects_.get(index);
    }

    public Texture getTexture(int index) {
        return mTextures_.get(index);
    }

    public ArrayList<Texture> getTextures() {
        return mTextures_;
    }

    public MapObject getNewObject(int index) {
        return new MapObject(mMapObjects_.get(index).getType(), mMapObjects_.get(index).isPassable());
    }

    public int size() {
        return mMapObjects_.size();
    }

}
