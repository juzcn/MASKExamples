/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.scm;

import java.io.Serializable;

/**
 *
 * @author zj
 */
public class ShipOrder implements Serializable {

    private final int senderID;
    private final int receiverID;
    private final int quantity;
    private final int shipTime;

    public ShipOrder(int senderID, int receiverID, int quantity, int shipTime) {
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.quantity = quantity;
        this.shipTime = shipTime;
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

    /**
     * @return the shipTime
     */
    public int getShipTime() {
        return shipTime;
    }
}
