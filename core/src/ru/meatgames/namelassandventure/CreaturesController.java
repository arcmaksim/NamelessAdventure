package ru.meatgames.namelassandventure;

import java.util.ArrayList;

/**
 * Created by MeatBoy on 10.01.2016.
 */
public class CreaturesController {

    private ArrayList<CreatureInstance> mCreatures_;
    private static CreaturesDB mCreatureDB_ = CreaturesDB.getInstance();

    public CreaturesController() {
        mCreatures_ = new ArrayList<CreatureInstance>();
    }

    public CreatureInstance newCreatureInstance(int type) {
        CreatureInstance creature = new CreatureInstance(type);
        Creature temp = mCreatureDB_.getCreature(type);
        creature.setName(temp.getName());
        creature.setMaxHealth(temp.getMaxHealth());
        creature.setAttack(temp.getAttack());
        creature.setDefence(temp.getDefence());
        creature.setCurrentHealth(creature.getMaxHealth());
        return creature;
    }

    public CreatureInstance getCreatureInstance(int posx, int posy) {
        for (CreatureInstance c : mCreatures_)
            if (c.getPositionX() == posx && c.getPositionY() == posy)
                return c;
        return null;
    }

}
