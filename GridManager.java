/*
 * Decompiled with CFR 0_115.
 */
package com.emeraldSword.game;

import com.emeraldSword.game.Grid;
import com.emeraldSword.game.VillageResources;
import java.util.List;

public class GridManager {
    public static void populateGridRandomly(Grid grid) {
        int j = 0;
        while (j < grid.getNbLine()) {
            int i = 0;
            while (i < grid.getNbCol()) {
                int type = (int)(Math.random() * 10.0 + 1.0);
                VillageResources res = new VillageResources(i, j, type);
                while (i >= 2 && grid.getResourcesAt(i - 1, j).getType() == type && grid.getResourcesAt(i - 2, j).getType() == type || j >= 2 && grid.getResourcesAt(i, j - 1).getType() == type && grid.getResourcesAt(i, j - 2).getType() == type) {
                    type = (int)(Math.random() * 10.0 + 1.0);
                    res = new VillageResources(i, j, type);
                }
                grid.getAllRes().add(res);
                ++i;
            }
            ++j;
        }
    }
}
