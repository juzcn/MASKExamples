/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.serviceHall;

import mask.executor.MKExecutor;
import mask.agent.Behavior;
import mask.agent.Condition;
import mask.agent.Agent;
import mask.agent.TimeCondition;
import java.io.Serializable;
import java.util.Random;

/**
 *
 * @author zj
 */
//public class Customer extends Agent {
public class Customer extends Agent implements Serializable {

    private final Random random = new Random();
    private TimeCondition timerCondition;

    @Override
    public void setup() {
        super.setup();
        timerCondition = new TimeCondition(0);
        state = CustomerState.Idle;
        setStateBehaviors(CustomerState.Idle, new Behavior(Condition.TRUE, () -> mystart()));
        setStateBehaviors(CustomerState.WaitingInQueue, new Behavior(() -> ((IServiceHall) world()).isFirst(getUniqueID()), () -> beingServiced()));
        setStateBehaviors(CustomerState.BeingServiced, new Behavior(timerCondition, () -> serviceFinished()));
    }

    private long queueIn;
    private long serviceIn;
    private long leave;

    public boolean mystart() {
        queueIn = time();
        ((IServiceHall) world()).enterInQueue(getUniqueID());
        System.out.println(this + " enters in Queue");
        state = CustomerState.WaitingInQueue;
        return true;
    }

    public boolean beingServiced() {
        serviceIn = time();
        System.out.println(this + " being serviced");
        int interval = random.nextInt(6);
        timerCondition.setAtTime(time() + interval);
        state = CustomerState.BeingServiced;
        return true;
    }

    public boolean serviceFinished() {
        leave = time();
        System.out.println(this + " leaving");
        ((IServiceHall) world()).leave(getUniqueID());
        this.getGroup().removeTemp(this);
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        service().deRegisterAgent(getName());
    }

    /**
     * @return the queueIn
     */
    public long getQueueIn() {
        return queueIn;
    }

    /**
     * @return the serviceIn
     */
    public long getServiceIn() {
        return serviceIn;
    }

    /**
     * @return the leave
     */
    public long getLeave() {
        return leave;
    }
}
