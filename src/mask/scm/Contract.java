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
public class Contract implements Serializable {

    int leadTime;
    int buyer;
    int supplier;

    public Contract(int buyer, int supplier, int leadTime) {
        this.buyer = buyer;
        this.supplier = supplier;
        this.leadTime = leadTime;
    }

}
