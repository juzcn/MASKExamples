/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.gol;

import mask.utils.bean.Bean;
import mask.utils.gridSpace.GridSpace;
import mask.world.IWorld;
import mask.world.World;

/**
 *
 * @author zj
 */
public class GoL extends World implements IWorld {

    private final GridSpace<Bean> gridSpace = new GridSpace<>(100, 100);

    @Override
    public void setup() {
        super.setup();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                gridSpace.setValue(j, j, null);
            }
        }
    }

    /**
     * @return the gridSpace
     */
    public GridSpace<Bean> getGridSpace() {
        return gridSpace;
    }
}
