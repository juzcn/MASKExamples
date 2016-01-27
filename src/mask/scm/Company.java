/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.scm;

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
public abstract class Company extends Agent {

    protected int inventory;
    protected int productsReceived;
    protected int totalOrderQuantity;
    protected int replenishmentOrderQuantity;
    protected int totalStockout;
    protected Contract withSupplier;
    protected List<Contract> withBuyers;
    private int shipper;
    protected int inventoryPeriods;

    protected static enum State {
        WaitNewPeriod, WaitingOrders, WaitingReplenishmentFeedback
    };
    private int productChannel;
    private int orderChannel;
    private int replenishmnetChannel;

    @Override
    public void setup() {
        super.setup();
        state = State.WaitNewPeriod;
        productChannel = service().newNamedChannel(this.getUniqueID(), "Product");
        orderChannel = service().newNamedChannel(this.getUniqueID(), "Order");
        replenishmnetChannel = service().newNamedChannel(this.getUniqueID(), "Replenishment");
        setStateBehaviors(State.WaitNewPeriod, new Behavior(new EachTimeCondition(), () -> periodBegin()));
        setStateBehaviors(State.WaitingReplenishmentFeedback,
                new Behavior(() -> service().hasMessage(replenishmnetChannel), () -> replenishmentFeedBack()));
        setStateBehaviors(State.WaitingOrders, new Behavior(() -> service().hasMessage(orderChannel), () -> ordersProcessing()));

    }

    public void setShipper(Shipper shipper) {
        this.shipper = shipper.getUniqueID();
    }

    public boolean periodBegin() {
        productsReception();
        state = State.WaitingOrders;
        return true;
    }

    public void productsReception() {
        if (service().hasMessage(productChannel)) {
            MKMessage message = service().receive(productChannel);
            productsReceived = (int) message.getContent();
            inventory += productsReceived;
        }
    }

    public boolean replenishmentFeedBack() {
        MKMessage message = service().receive(replenishmnetChannel);
        if ((int) message.getContent() < replenishmentOrderQuantity) {
            System.out.println("Supplier stockout");
        }
        state = State.WaitNewPeriod;
        return true;
    }

    private int orderCount = 0;
    private int totalShipQuantity;

    public boolean ordersProcessing() {

        orderCount++;
        MKMessage message = service().receive(orderChannel);
        int orderQuantity = (int) message.getContent();
        if (orderQuantity != 0) {
            totalOrderQuantity += orderQuantity;
            int shipQuantity = 0;
            if (inventory >= orderQuantity) {
                shipQuantity = orderQuantity;
            } else if (inventory > 0) {
                System.out.println("Stockout");
                shipQuantity = inventory;
                service().send(this.getUniqueID(), shipper, "ShipOrder",
                        new Shipper.ShipOrder(this.getUniqueID(), message.getSenderID(), shipQuantity));
            }
            service().send(this.getUniqueID(), message.getSenderID(), "Replenishment", shipQuantity);
            totalShipQuantity += shipQuantity;
        }
        if (orderCount == withBuyers.size()) {
            replenishment();
        }
        return true;
    }

    protected abstract int[] forecast(int periods);

    public void replenishment() {
        int[] f = forecast(withSupplier.leadTime + inventoryPeriods);
        int rop = 0;
        for (int i = 0; i < withSupplier.leadTime; i++) {
            rop += f[i];
        }
        if (inventory < rop) {
            int total = 0;
            for (int i = 0; i < f.length; i++) {
                total += f[i];
            }
            replenishmentOrderQuantity = total - inventory;
            service().send(getUniqueID(), withSupplier.supplier, "Order", replenishmentOrderQuantity);
            state = State.WaitingReplenishmentFeedback;
        } else {
            service().send(getUniqueID(), withSupplier.supplier, "Order", 0);
            state = State.WaitNewPeriod;
        }
    }

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

}
