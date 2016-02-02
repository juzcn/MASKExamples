/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.scm;

/**
 *
 * @author zj
 */
public interface ISupplier {

    public int getUniqueID();

    public void addBuyerContract(Contract contract);
}
