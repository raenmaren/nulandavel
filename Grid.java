/*
 * Decompiled with CFR 0_115.
 */
package com.emeraldSword.game;

import com.emeraldSword.game.VillageResources;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grid {
    private List<VillageResources> allRes;
    private int nbLine;
    private int nbCol;

    public Grid(int nbLine, int nbCol) {
        this.setNbLine(nbLine);
        this.setNbCol(nbCol);
        this.allRes = new ArrayList<VillageResources>();
    }

    public VillageResources getSpriteClickOn(int xScreen, int yScreen) {
        yScreen = 800 - yScreen;
        for (VillageResources res : this.allRes) {
            if (xScreen < res.getxPosition() * 40 + 55 || xScreen > res.getxPosition() * 40 + 94 || yScreen < res.getyPosition() * 40 + 55 || yScreen > res.getyPosition() * 40 + 94) continue;
            return res;
        }
        return null;
    }

    public void switchResources(VillageResources first, VillageResources second) {
        int firstXpos = first.getxPosition();
        int firstYpos = first.getyPosition();
        int secondXpos = second.getxPosition();
        int secondYpos = second.getyPosition();
        first.setxPosition(secondXpos);
        first.setyPosition(secondYpos);
        second.setxPosition(firstXpos);
        second.setyPosition(firstYpos);
    }

    public VillageResources getResourcesAt(int j, int i) {
        for (VillageResources res : this.allRes) {
            if (res.getxPosition() != j || res.getyPosition() != i) continue;
            return res;
        }
        return null;
    }

    public boolean hasChain(VillageResources res) {
        int type = res.getType();
        boolean chain = true;
        int horzlenght = 0;
        int i = 0;
        while (chain && this.getResourcesAt(res.getxPosition() + 1 + i, res.getyPosition()) != null) {
            if (this.getResourcesAt(res.getxPosition() + 1 + i, res.getyPosition()).getType() == type) {
                ++horzlenght;
                ++i;
                continue;
            }
            chain = false;
        }
        chain = true;
        i = 0;
        while (chain && this.getResourcesAt(res.getxPosition() - 1 - i, res.getyPosition()) != null) {
            if (this.getResourcesAt(res.getxPosition() - 1 - i, res.getyPosition()).getType() == type) {
                ++horzlenght;
                ++i;
                continue;
            }
            chain = false;
        }
        chain = true;
        int vertlength = 0;
        int j = 0;
        while (chain && this.getResourcesAt(res.getxPosition(), res.getyPosition() + 1 + j) != null) {
            if (this.getResourcesAt(res.getxPosition(), res.getyPosition() + 1 + j).getType() == type) {
                ++vertlength;
                ++j;
                continue;
            }
            chain = false;
        }
        chain = true;
        j = 0;
        while (chain && this.getResourcesAt(res.getxPosition(), res.getyPosition() - 1 - j) != null) {
            if (this.getResourcesAt(res.getxPosition(), res.getyPosition() - 1 - j).getType() == type) {
                ++vertlength;
                ++j;
                continue;
            }
            chain = false;
        }
        if (vertlength > 1 || horzlenght > 1) {
            return true;
        }
        return false;
    }

    public Map<String, List<VillageResources>> detectChain(VillageResources res) {
        HashMap<String, List<VillageResources>> chains = new HashMap<String, List<VillageResources>>();
        ArrayList<VillageResources> horChain = new ArrayList<VillageResources>();
        horChain.add(res);
        ArrayList<VillageResources> vertChain = new ArrayList<VillageResources>();
        vertChain.add(res);
        int type = res.getType();
        boolean chain = true;
        int i = 0;
        while (chain && this.getResourcesAt(res.getxPosition() + 1 + i, res.getyPosition()) != null) {
            if (this.getResourcesAt(res.getxPosition() + 1 + i, res.getyPosition()).getType() == type) {
                horChain.add(this.getResourcesAt(res.getxPosition() + 1 + i, res.getyPosition()));
                ++i;
                continue;
            }
            chain = false;
        }
        chain = true;
        i = 0;
        while (chain && this.getResourcesAt(res.getxPosition() - 1 - i, res.getyPosition()) != null) {
            if (this.getResourcesAt(res.getxPosition() - 1 - i, res.getyPosition()).getType() == type) {
                horChain.add(this.getResourcesAt(res.getxPosition() - 1 - i, res.getyPosition()));
                ++i;
                continue;
            }
            chain = false;
        }
        chain = true;
        int j = 0;
        while (chain && this.getResourcesAt(res.getxPosition(), res.getyPosition() + 1 + j) != null) {
            if (this.getResourcesAt(res.getxPosition(), res.getyPosition() + 1 + j).getType() == type) {
                vertChain.add(this.getResourcesAt(res.getxPosition(), res.getyPosition() + 1 + j));
                ++j;
                continue;
            }
            chain = false;
        }
        chain = true;
        j = 0;
        while (chain && this.getResourcesAt(res.getxPosition(), res.getyPosition() - 1 - j) != null) {
            if (this.getResourcesAt(res.getxPosition(), res.getyPosition() - 1 - j).getType() == type) {
                vertChain.add(this.getResourcesAt(res.getxPosition(), res.getyPosition() - 1 - j));
                ++j;
                continue;
            }
            chain = false;
        }
        if (vertChain.size() > 2) {
            chains.put("vertical", vertChain);
        }
        if (horChain.size() > 2) {
            chains.put("horizontal", horChain);
        }
        return chains;
    }

    public List<VillageResources> getAllRes() {
        return this.allRes;
    }

    public void setAllRes(List<VillageResources> allRes) {
        this.allRes = allRes;
    }

    public int getNbLine() {
        return this.nbLine;
    }

    public void setNbLine(int nbLine) {
        this.nbLine = nbLine;
    }

    public int getNbCol() {
        return this.nbCol;
    }

    public void setNbCol(int nbCol) {
        this.nbCol = nbCol;
    }
}
