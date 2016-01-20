/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.schelling;

import mask.world.IWorld;
import mask.utils.gridSpace.GridCell;
import java.util.List;
import mask.utils.bean.Bean;

/**
 *
 * @author zj
 */
public interface ISchelling extends IWorld {

    public boolean moveTo(Bean bean, int row, int column);

    public boolean moveTo(Bean bean, int oldRow, int oldColumn, int newRow, int newColumn);

    public List<GridCell<Bean>> getAllCellNeighbs(int row, int column);

}
