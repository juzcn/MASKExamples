/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.beergame;

import mask.world.World;
import mask.agent.Agent;
import mask.logging.XLSLogger;

/**
 *
 * @author zj
 */
public class BeerGameXSLLogger extends XLSLogger {

    @Override
    public void start() {
        super.start();
        createSheet("制造商", "时间", "库存");
        createSheet("批发商", "时间", "库存");
    }

    @Override
    public void logAgents(Agent[] agents) {
        System.out.println("Agents Received "+agents.length);
        for (Agent a : agents) {
            if (a instanceof Manufacturer) {
                writeRow("制造商", ((Manufacturer) a).getTime(), ((Manufacturer) a).getInventory());
            }
            if (a instanceof Wholesaler) {
                writeRow("批发商", ((Wholesaler) a).getTime(), ((Wholesaler) a).getInventory());
            }
        }
    }

    @Override
    public void logWorld(World world) {
    }

}
