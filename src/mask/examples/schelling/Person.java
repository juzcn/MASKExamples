/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.schelling;

import mask.utils.gridSpace.GridCell;
import mask.agent.SimpleAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import mask.utils.bean.Bean;
import mask.utils.bean.BeanDef;

/**
 *
 * @author zj
 */
public class Person extends SimpleAgent {

    private static final int RED = 0;
    private static final int YELLOW = 1;
    private int row, column, oldRow = -1, oldColumn = -1;
    private final int color;
    private boolean moved = false;
    private final Random random = new Random();

    private static BeanDef colorBean = new BeanDef(Person.class, "Color");

    public boolean moveTo(int row, int column) {
        boolean success = ((ISchelling) world()).moveTo(colorBean.getBean(this), row, column);
        if (success) {
            this.row = row;
            this.column = column;
            moved = true;
        }
        return success;
    }

    public Person() {
        if (random.nextBoolean()) {
            color = RED;
        } else {
            color = YELLOW;
        }
    }

    private List<GridCell<Bean>> neighbsList;

    @Override
    public boolean perceive() {
        moved = false;
        neighbsList = ((ISchelling) world()).getAllCellNeighbs(row, column);
        return false;
    }

    @Override
    public boolean actuate() {
        List<GridCell<Bean>> freeList = new ArrayList<>();
        int nNeighbs = 0, nSameColor = 0;
        for (GridCell<Bean> neighb : neighbsList) {
            if (neighb.getValue() == null) {
                freeList.add(neighb);
            } else {
                nNeighbs++;
                if ((int) neighb.getValue().getValue("Color") == color) {
                    nSameColor++;
                }
            }
        }
        double percent;
        if (nNeighbs == 0) {
            percent = 1.0;
        } else {
            percent = (double) nSameColor / (double) nNeighbs;
        }
        if (percent < 0.6 && freeList.size() > 0) {
            System.out.println(this + " choouse to move at time " + time());
            int i = random.nextInt(freeList.size());
            boolean success = ((ISchelling) world()).moveTo(colorBean.getBean(this), row, column, freeList.get(i).getRow(), freeList.get(i).getColumn());
            if (success) {
                moved = true;
                oldRow = row;
                row = freeList.get(i).getRow();
                oldColumn = column;
                column = freeList.get(i).getColumn();
                System.out.println(this + " move sucessfull");
            } else {
                System.out.println(this + " move failed");
            }
        }
        return false;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * @return the color
     */
    public int getColor() {
        return color;
    }

    /**
     * @return the moved
     */
    public boolean getMoved() {
        return moved;
    }

    /**
     * @return the oldRow
     */
    public int getOldRow() {
        return oldRow;
    }

    /**
     * @return the oldColumn
     */
    public int getOldColumn() {
        return oldColumn;
    }

}
