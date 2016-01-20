/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.examples.beergame;

import mask.agent.Behavior;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.Queue;
import mask.agent.Agent;
import mask.agent.TimeCondition;
import mask.logging.LabelStr;
import mask.service.MKMessage;

/**
 *
 * @author zj
 */
public class Company extends Agent {
//  public class Company extends ThreadAgent {

    protected int inventory = 30;
    protected int toOrder;
    protected int orderCounter;
    protected int totalOrderQuantity = 10;
    private Integer[] customers;
    protected int wholesaler;
    protected final Queue<DeliveryNotice> deliveryNotices = new LinkedList<>();
    protected TimeCondition timerCondition;
    protected int orderMailBox;
    protected int noticeMailBox;

    @Override
    public void setup() {
        super.setup();
        timerCondition = new TimeCondition(1);
        state = CompanyState.Ready;
        orderMailBox = service().newMailBox(getUniqueID(), "Order");
        noticeMailBox = service().newMailBox(getUniqueID(), "Notice");
        setStateBehaviors(CompanyState.Ready, new Behavior(timerCondition, () -> periodBegin()));
        setStateBehaviors(CompanyState.WaitOrder, new Behavior(() -> service().hasMail(orderMailBox), () -> processOrder()));
        setStateBehaviors(CompanyState.WaitDeliveryNotice, new Behavior(() -> service().hasMail(noticeMailBox), () -> processDeliveryNotice()));

    }

    public void setCustomers(Company... customers) {
        if (customers != null) {
            this.customers = new Integer[customers.length];

            for (int i = 0; i < this.customers.length; i++) {
                this.customers[i] = customers[i].getUniqueID();

            }
        }
    }

    public void setCustomers(Integer... customers) {
        this.customers = customers;
    }

    public void setWholesaler(Company wholesaler) {
        if (wholesaler != null) {
            this.wholesaler = wholesaler.getUniqueID();
        }
    }

    public void setWholesaler(Integer wholesaler) {
        this.wholesaler = wholesaler;
    }

    /**
     * @return the inventory
     */
    @LabelStr("库存")
    public int getInventory() {
        return inventory;
    }

    protected static class DeliveryNotice implements Serializable {

        protected long deliveryTime;
        protected int quantity;

        public DeliveryNotice(long t, int q) {
            deliveryTime = t;
            quantity = q;
        }
    }

    protected void processDeliveries() {
        DeliveryNotice dn = deliveryNotices.peek();
        if (dn != null && dn.deliveryTime == time()) {
            dn = deliveryNotices.poll();
            inventory = dn.quantity + inventory;
//            System.out.println(this + "  products received = " + dn.quantity + " at period " + time());
        }
    }

    protected boolean replenishment() {
        toOrder = 4 * (totalOrderQuantity) - inventory;
        service().send(getUniqueID(), wholesaler, "Order", toOrder);
//        System.out.println(this + "  products to order = " + toOrder + " at period " + time());
        state = CompanyState.WaitDeliveryNotice;
        return true;

    }

    protected int processOrder(int order) {
        totalOrderQuantity += order;
        orderCounter++;
        int sold;
        if (inventory >= order) {
            sold = order;
        } else {
            sold = inventory;
        }
        inventory -= sold;
        return sold;
    }

    protected boolean processOrder() {
        MKMessage m = service().receive(orderMailBox);
        Integer companyID = m.getSenderID();
        int order = (int) m.getContent();
        int sold = processOrder(order);
        service().send(getUniqueID(), companyID, "Notice", new DeliveryNotice(time() + 4, sold));
        if (orderCounter == customers.length) {
//            System.out.println(this + "  total products ordered = " + totalOrderQuantity + " at period " + time());
//            System.out.println(this + "  inventory = " + inventory + " at period " + time());
            return replenishment();
        }
        state = CompanyState.WaitOrder;
        return true;
    }

    protected boolean processDeliveryNotice() {
        MKMessage m = service().receive(noticeMailBox);
        deliveryNotices.add((DeliveryNotice) (m.getContent()));
        timerCondition.setAtTime(time() + 1);
        state = CompanyState.Ready;
        return true;
    }

    public boolean periodBegin() {
        totalOrderQuantity = 0;
        orderCounter = 0;
//        System.out.println(this + " begin...");
        processDeliveries();
        state = CompanyState.WaitOrder;
        return true;
    }
}
