/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.scm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import mask.agent.Agent;
import mask.agent.Behavior;
import mask.agent.EachTimeCondition;
import mask.service.MKMessage;

/**
 *
 * @author zj
 */
public abstract class Company extends Agent implements Serializable {

    protected int inventory;
    protected int received;
    protected int ordered;
    protected int replenishments;
    protected int stockout;
    protected Contract withSupplier;
    protected List<Contract> withBuyers;
    protected int inventoryPeriods;

    /**
     * @return the inventory
     */
    public int getInventory() {
        return inventory;
    }

    /**
     * @return the replenishments
     */
    public int getReplenishments() {
        return replenishments;
    }

    /**
     * @return the received
     */
    public int getReceived() {
        return received;
    }

    /**
     * @return the stockout
     */
    public int getStockout() {
        return stockout;
    }

    /**
     * @return the ordered
     */
    public int getOrdered() {
        return ordered;
    }

    protected static enum State {
        WaitNewPeriod, WaitOrders, WaitOrderFeedback
    };
    private int orderChannel;
    protected int replenishmnetChannel;

    @Override
    public void setup() {
        super.setup();
        state = State.WaitNewPeriod;
        orderChannel = service().newNamedChannel(this.getUniqueID(), "Order");
        replenishmnetChannel = service().newNamedChannel(this.getUniqueID(), "Replenishment");
        setStateBehaviors(State.WaitNewPeriod, new Behavior(new EachTimeCondition(), () -> periodBegin()));
        setStateBehaviors(State.WaitOrderFeedback,
                new Behavior(() -> service().hasMessage(replenishmnetChannel), () -> replenishmentFeedBack()));
        setStateBehaviors(State.WaitOrders, new Behavior(() -> service().hasMessage(orderChannel), () -> ordersProcessing()));

    }

    public boolean periodBegin() {
        System.out.println(this + " Period begin : " + time());
        orderCount = 0;
        stockout = 0;
        ordered = 0;
        replenishment();
        productsReception();
        return true;
    }

    public void productsReception() {
        received = 0;
        if (shipOrder != null && shipOrder.getShipTime() == time()) {
            received = (int) shipOrder.getQuantity();
            inventory += received;
            shipOrder = null;
        }
    }

    public void replenishment() {
        replenishments = 0;
        if (shipOrder == null) {
            int[] f = forecast(withSupplier.leadTime + inventoryPeriods);
            int rop = 0;
            for (int i = 0; i < withSupplier.leadTime; i++) {
                rop += f[i];
            }
            if (inventory <= rop) {
                int total = 0;
                for (int i = 0; i < f.length; i++) {
                    total += f[i];
                }
                replenishments = total - inventory;
            }
        }
        service().send(getUniqueID(), withSupplier.supplier, "Order", replenishments);
        System.out.println(this + " replenishment order quantity = " + replenishments);
        if (replenishments == 0) {
            state = State.WaitOrders;
        } else {
            state = State.WaitOrderFeedback;
        }
    }

    protected ShipOrder shipOrder = null;

    protected int orderCount;
    private int totalShipQuantity;

    public boolean ordersProcessing() {
        orderCount++;
        MKMessage message = service().receive(orderChannel);
        System.out.println(this + " order count = " + orderCount + " orderQuantity = " + message.getContent());
        int orderQuantity = (int) message.getContent();
        if (orderQuantity != 0) {
            ordered += orderQuantity;
            int shipQuantity = 0;
            if (inventory >= orderQuantity) {
                shipQuantity = orderQuantity;
            } else {
                stockout += (orderQuantity - inventory);
                shipQuantity = inventory;
            }
            int shipTime = 0;
            for (Contract c : withBuyers) {
                if (c.buyer == message.getSenderID()) {
                    shipTime = time() + c.leadTime;
                    break;
                }
            }
            service().send(this.getUniqueID(), message.getSenderID(), "Replenishment",
                    new ShipOrder(this.getUniqueID(), message.getSenderID(), shipQuantity, shipTime));
            inventory -= shipQuantity;
            totalShipQuantity += shipQuantity;
        }
        if (orderCount == withBuyers.size()) {
            state = State.WaitNewPeriod;
        }
        return true;
    }

    protected abstract int[] forecast(int periods);

    public void addBuyerContract(Contract contract) {
        if (withBuyers == null) {
            withBuyers = new ArrayList<>();
        }
        withBuyers.add(contract);
    }

    public void setSupplierContract(Contract contract) {
        withSupplier = contract;
    }

    public static void contract(IBuyer buyer, ISupplier supplier, int leadTime) {
        Contract contract = new Contract(buyer.getUniqueID(), supplier.getUniqueID(), leadTime);
        buyer.setSupplierContract(contract);
        supplier.addBuyerContract(contract);
    }

    public boolean replenishmentFeedBack() {
        MKMessage message = service().receive(replenishmnetChannel);
        shipOrder = (ShipOrder) message.getContent();
        if (shipOrder.getQuantity() < replenishments) {
            System.out.println("Supplier stockout");
        }
        shipOrder = (ShipOrder) message.getContent();
        state = State.WaitOrders;
        return true;
    }

}
