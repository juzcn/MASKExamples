/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.serviceHall;

import mask.world.IWorld;

/**
 *
 * @author zj
 */
public interface IServiceHall extends IWorld {

    public void enterInQueue(Integer c);

    public boolean isFirst(Integer c);

    public void leave(Integer c);

}
