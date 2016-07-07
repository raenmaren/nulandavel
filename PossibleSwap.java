/*
 * Decompiled with CFR 0_115.
 */
package com.emeraldSword.game;

import com.emeraldSword.game.VillageResources;

public class PossibleSwap {
    private VillageResources res1;
    private VillageResources res2;

    public PossibleSwap(VillageResources res1, VillageResources res2) {
        this.res1 = res1;
        this.res2 = res2;
    }

    public VillageResources getRes1() {
        return this.res1;
    }

    public void setRes1(VillageResources res1) {
        this.res1 = res1;
    }

    public VillageResources getRes2() {
        return this.res2;
    }

    public void setRes2(VillageResources res2) {
        this.res2 = res2;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (((PossibleSwap)obj).getRes1().getxPosition() == this.getRes1().getxPosition() && ((PossibleSwap)obj).getRes1().getyPosition() == this.getRes1().getyPosition() && ((PossibleSwap)obj).getRes2().getxPosition() == this.getRes2().getxPosition() && ((PossibleSwap)obj).getRes2().getyPosition() == this.getRes2().getyPosition()) {
            return true;
        }
        if (((PossibleSwap)obj).getRes1().getxPosition() == this.getRes2().getxPosition() && ((PossibleSwap)obj).getRes1().getyPosition() == this.getRes2().getyPosition() && ((PossibleSwap)obj).getRes2().getxPosition() == this.getRes1().getxPosition() && ((PossibleSwap)obj).getRes2().getyPosition() == this.getRes1().getyPosition()) {
            return true;
        }
        return false;
    }
}
