package ru.meatgames.namelassandventure;

/**
 * Created by MeatBoy on 09.01.2016.
 */
public class Map{

    //use Enum

    private MapObject[] mMap_;
    private MapObjectsDB mMapObjectsDB_;
    private final int DEFAULT_MAP_HEIGHT_ = 5;
    private final int DEFAULT_MAP_WIDTH_ = 5;
    private int mMapWidth_;
    private int mMapHeight_;
    private int mSize_;

    public Map() {
        mMapWidth_ = DEFAULT_MAP_WIDTH_;
        mMapHeight_ = DEFAULT_MAP_HEIGHT_;
        init();
    }

    public Map(int width, int height) {
        mMapWidth_ = (width < DEFAULT_MAP_WIDTH_) ? DEFAULT_MAP_WIDTH_ : width;
        mMapHeight_ = (width < DEFAULT_MAP_HEIGHT_) ? DEFAULT_MAP_HEIGHT_ : height;
        init();
    }

    public void init() {
        mSize_ = mMapWidth_ * mMapHeight_;
        mMap_ = new MapObject[mSize_];
        for (MapObject m : mMap_)
            m = null;
        mMapObjectsDB_ = MapObjectsDB.getInstance();
    }

    public int getSize() {
        return mSize_;
    }

    public MapObject[] getMap() {
        return mMap_;
    }

    public int getWidth() {
        return mMapWidth_;
    }

    public int getHeight() {
        return mMapHeight_;
    }

    public MapObject getObject(int position) {
        return mMap_[position];
    }

    private boolean checkDimension(int value, int limit) {
        return (value > -1 && value < limit) ? true : false;
    }

    public void fillArea(int xStart, int yStart, int xLenght, int yLenght, int objIndex) {
        if (checkDimension(xStart, mMapWidth_) && checkDimension(yStart, mMapHeight_) &&
                checkDimension(xLenght, mMapWidth_ - xStart + 1) && checkDimension(yLenght, mMapHeight_ - yStart + 1) &&
                (checkDimension(objIndex, mMapObjectsDB_.size())) || objIndex == mMapObjectsDB_.EMPTY) {

            for (int i=xStart;i<xStart+xLenght;i++)
                for (int j=yStart;j<yStart+yLenght;j++)
                    if (objIndex != mMapObjectsDB_.EMPTY) {
                        mMap_[i + j * mMapWidth_] = mMapObjectsDB_.getNewObject(objIndex);
                    } else {
                        mMap_[i + j * mMapWidth_] = null;
                    }
        } else {
            return;
        }
    }

}
