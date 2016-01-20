/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.gol;

import mask.utils.bean.Bean;
import mask.utils.bean.BeanDef;
import mask.agent.SimpleAgent;
import java.io.Serializable;
import java.util.List;
import java.util.Random;

/**
 *
 * @author zj
 */
public class CellAgent extends SimpleAgent implements Serializable {

    private boolean alive;
    private boolean changed=true;
    private int row;
    private int column;
    private List<Bean> neighbsList;
    public static BeanDef aliveBean=new BeanDef(CellAgent.class,"Alive");
    public boolean getAlive() {
        return alive;
    }

    public boolean isChanged() {
        return changed;
    }
    private Random random = new Random();

    public CellAgent() {
        if (random.nextDouble() <= 0.3) {
            alive = true;
        }
    }

    @Override
    public boolean perceive() {
        neighbsList = ((GoL) world()).getGridSpace().getAllNeighbs(row, column);
        return false;
    }

    @Override
    public boolean actuate() {
        int lives = 0;
        changed = false;
        for (Bean bean : neighbsList) {
            if ((boolean)bean.getValue("Alive")) {
                lives++;
            }
        }
        if (alive) {
            if (lives < 2 || lives > 3) {
                alive = false;
                changed = true;
                ((GoL) world()).getGridSpace().setValue(row, column, aliveBean.getBean(this));
                System.out.println(this + " to die");
            }
        }
        if (!alive && lives == 3) {
            alive = true;
            changed = true;
            ((GoL) world()).getGridSpace().setValue(row, column, aliveBean.getBean(this));
            System.out.println(this + " to live");
        }
        return changed;
    }

    /**
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * @param row the row to set
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * @return the column
     */
    public int getColumn() {
        return column;
    }

    /**
     * @param column the column to set
     */
    public void setColumn(int column) {
        this.column = column;
    }

}
