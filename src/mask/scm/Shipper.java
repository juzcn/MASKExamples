/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.scm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mask.agent.Agent;
import mask.agent.Behavior;
import mask.agent.EachTimeCondition;
import mask.service.MKMessage;

/**
 *
 * @author zj
 */
public class Shipper extends Agent {

    public static class ShipOrder implements Serializable {

        private final int senderID;
        private final int receiverID;
        private final int quantity;

        public ShipOrder(int senderID, int receiverID, int quantity) {
            this.senderID = senderID;
            this.receiverID = receiverID;
            this.quantity = quantity;
        }

        /**
         * @return the receiverID
         */
        public int getReceiverID() {
            return receiverID;
        }

        /**
         * @return the quantity
         */
        public int getQuantity() {
            return quantity;
        }

        /**
         * @return the senderID
         */
        public int getSenderID() {
            return senderID;
        }
    }

    private int shipOrderChannel;

    @Override
    public void setup() {
        shipOrderChannel = service().newNamedChannel(this.getUniqueID(), "ShipOrder");
        this.globalBehaviors.add(new Behavior(new EachTimeCondition(), () -> periodBegin()));
        this.globalBehaviors.add(new Behavior(() -> service().hasMessage(shipOrderChannel), () -> shipOrderProcessing()));
    }

    public boolean periodBegin() {
        List<ShipOrder> list = shipOrders.get(time());
        if (list == null) {
            return false;
        }
        shipOrders.remove(time());
        for (ShipOrder so : list) {
            service().send(so.getSenderID(), so.getReceiverID(), "Product", so.getQuantity());
        }
        return true;
    }
    private Map<Integer, List<ShipOrder>> shipOrders = Collections.synchronizedMap(new HashMap<>());

    public boolean shipOrderProcessing() {
        MKMessage message = service().receive(shipOrderChannel);
        ShipOrder so = (ShipOrder) message.getContent();
        List<ShipOrder> list = shipOrders.get(time());
        if (list == null) {
            list = Collections.synchronizedList(new ArrayList<>());
            shipOrders.put(time(), list);
        }
        list.add(so);
        return true;
    }
}
