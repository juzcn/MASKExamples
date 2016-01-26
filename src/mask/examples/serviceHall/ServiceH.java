/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.serviceHall;

import mask.world.World;
import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author zj
 */
public class ServiceH extends World implements IServiceHall {

    private Queue<Integer> queue = new LinkedList<>();

    @Override
    public void setup() {
        super.setup();
//        ServiceH sh = (ServiceH) getCopy();
//        System.out.println("ServiceH created !!!");
        queue.clear();
    }

    @Override
    protected World copyTo(World w) {
        ((ServiceH) w).queue = this.queue;
        return w;
    }

    @Override
    public synchronized void enterInQueue(Integer c) {
        queue.offer(c);
    }

    @Override
    public boolean isFirst(Integer c) {
        return (queue.peek().equals(c));
    }

    @Override
    public synchronized void leave(Integer c) {
        queue.poll();
    }


}
