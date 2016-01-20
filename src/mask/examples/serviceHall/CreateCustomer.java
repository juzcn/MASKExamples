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
import java.util.Random;

/**
 *
 * @author zj
 */
//public class CreateCustomer extends Agent {
public class CreateCustomer extends Agent {

    private final Random random = new Random();
    private TimeCondition timerCondition;
    private long maxTime;

    public CreateCustomer(long maxTime) {
        this.maxTime = maxTime;
    }

    @Override
    public void setup() {
        super.setup();
        timerCondition = new TimeCondition(0);
        state = CreateCustomerState.Created;
        setStateBehaviors(CreateCustomerState.Created, new Behavior(Condition.TRUE, () -> mystart()));
        setStateBehaviors(CreateCustomerState.Wait, new Behavior(timerCondition, () -> timer()));
    }

    public boolean timer() {

        Customer customre = new Customer();
        System.out.println("New customer created at :" + time());
        this.getGroup().addTemp(customre);
        long next = time() + random.nextInt(6);
        if (next <= maxTime) {
            timerCondition.setAtTime(next);
        } else {
            this.getGroup().removeTemp(this);
        }
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        service().deRegisterAgent(getName());
    }

    public boolean mystart() {
        int interval = random.nextInt(6);
        timerCondition.setAtTime(time() + interval);
        state=CreateCustomerState.Wait;
        return true;

    }
}
