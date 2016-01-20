/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.schelling;

import mask.utils.gridSpace.GridSpace;
import mask.world.World;
import mask.utils.gridSpace.GridCell;
import java.io.Serializable;
import java.util.List;
import mask.utils.bean.Bean;

/**
 *
 * @author zj
 */
public class Schelling extends World implements ISchelling, Serializable  {

    private final GridSpace<Bean> gridSpace = new GridSpace<>(100, 100);

    @Override
    public void setup() {
        super.setup();
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                gridSpace.setValue(i, j, null);
            }
        }
    }

    @Override
    public synchronized boolean moveTo(Bean bean, int row, int column) {
        if (gridSpace.isFree(row, column)) {
            gridSpace.setValue(row, column, bean);
            return true;
        }
        return false;
    }

    @Override
    public synchronized boolean moveTo(Bean bean, int oldRow, int oldColumn, int newRow, int newColumn) {
        if (gridSpace.isFree(newRow, newColumn)) {
            gridSpace.setValue(newRow, newColumn, bean);
            gridSpace.setValue(oldRow, oldColumn, null);
            return true;
        }
        return false;
    }

    @Override
    public List<GridCell<Bean>> getAllCellNeighbs(int row, int column) {
        return gridSpace.getAllCellNeighbs(row, column);
    }


}
