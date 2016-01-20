/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.beergame;

import mask.world.World;
import mask.agent.Agent;
import mask.logging.CSVLogger;

/**
 *
 * @author zj
 */
public class BeerGameCSVLogger extends CSVLogger {

    @Override
    public void start() {
        super.start();
        writeHeader("UniqueID", "Time", "Inventory");
    }

    @Override
    public void process(Agent[] agents) {
        for (Agent a : agents) {
            if (a instanceof Retailer) {
                writeRow(a.getUniqueID(), a.getTime(), ((Retailer) a).getInventory());
            }
        }

    }

    @Override
    public void process(World world) {
    }

}
