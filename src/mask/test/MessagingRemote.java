/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mask.test;

import mask.utils.Utils;
import mask.service.IService;

/**
 *
 * @author zj
 */
public class MessagingRemote {

    public static void main(String args[]) {
        IService service = (IService) Utils.getBean("java:global.MASKBeans.MASKBeans-ejb.DSServiceBean");
        int id=service.newChannel();
        System.out.println();
        service.remoteJoinExecutorGroup(id);
        service.sendToExecutor("Hello !");
        System.out.println((String)service.waitReceive(id));
    }
}
