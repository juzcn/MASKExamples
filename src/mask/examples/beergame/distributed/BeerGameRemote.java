/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.beergame.distributed;

import mask.world.IWorld;
import javax.ejb.Remote;

/**
 *
 * @author zj
 */
@Remote
public interface BeerGameRemote extends IWorld {
    
}
