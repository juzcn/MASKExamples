/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.robby;

import mask.world.IWorld;
import mask.world.World;
import mask.utils.gridSpace.GridSpace;
import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author zj
 */
public class RobbyWorld extends World implements IWorld {

    private final GridSpace<CellState> gridSpace = new GridSpace(10, 10);
    private final Random random = new Random();
    private int robbyRow;
    private int robbyColumn;

    public Situation getSituation(int row, int column) {
        robbyRow = row;
        robbyColumn = column;
        Situation situation = new Situation();
        situation.setSite(gridSpace.getValue(row, column));
        if (row > 0) {
            situation.setAround(Direction.North, gridSpace.getValue(row - 1, column));
        }
        if (row < gridSpace.getRowSize() - 1) {
            situation.setAround(Direction.South, gridSpace.getValue(row + 1, column));
        }
        if (column > 0) {
            situation.setAround(Direction.West, gridSpace.getValue(row, column - 1));
        }
        if (column < gridSpace.getColumnSize() - 1) {
            situation.setAround(Direction.East, gridSpace.getValue(row, column + 1));
        }
        return situation;
    }

    public boolean pickup() {
        CellState state = gridSpace.getValue(robbyRow, robbyColumn);
        if (state.equals(CellState.Empty)) {
            return false;
        } else {
            System.out.println("Row :" + robbyRow + "Column:" + robbyColumn + " Pick up OK !");
            gridSpace.setValue(robbyRow, robbyColumn, CellState.Empty);
            return true;
        }
    }

    public boolean move(Direction direction) {
        switch (direction) {
            case East:
                if (robbyColumn < gridSpace.getColumnSize() - 1) {
                    robbyColumn++;
                    return true;
                }
                break;
            case South:
                if (robbyRow < gridSpace.getRowSize() - 1) {
                    robbyRow++;
                    return true;
                }
                break;
            case West:
                if (robbyColumn > 0) {
                    robbyColumn--;
                    return true;
                }
                break;
            case North:
                if (robbyRow > 0) {
                    robbyRow--;
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * @return the gridSpace
     */
    public GridSpace<CellState> getGridSpace() {
        return gridSpace;
    }

    public static enum CellState {
        Soda, Empty
    }

    public static enum Direction {
        East, South, West, North
    }

    public static class Situation implements Serializable {

        private CellState site;
        private final CellState[] arounds = new CellState[4];

        public void setSite(CellState s) {
            site = s;
        }

        @Override
        public boolean equals(Object ano) {
            if (ano == null) {
                return false;
            }
            return (toString()).equals(ano.toString());
        }

        @Override
        public int hashCode() {
            return toString().hashCode();
        }

        public void setAround(Direction direction, CellState s) {
            arounds[direction.ordinal()] = s;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append('(');
            sb.append(site.toString());
            for (int i = 0; i < 4; i++) {
                sb.append(',');
                if (arounds[i] == null) {
                    sb.append("Wall");
                } else {
                    sb.append(arounds[i].toString());
                }
            }
            sb.append(')');
            return sb.toString();
        }
    }

    @Override
    public void setup() {
        super.setup();
        for (int i = 0; i < gridSpace.getRowSize(); i++) {
            for (int j = 0; j < gridSpace.getColumnSize(); j++) {
                if (random.nextDouble() < 0.5) {
                    gridSpace.setValue(i, j, CellState.Soda);
                } else {
                    gridSpace.setValue(i, j, CellState.Empty);
                }
            }
        }
    }
}
