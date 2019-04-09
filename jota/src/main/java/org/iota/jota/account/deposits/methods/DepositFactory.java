package org.iota.jota.account.deposits.methods;

import java.util.ArrayList;
import java.util.List;

import org.iota.jota.account.deposits.ConditionalDepositAddress;

@SuppressWarnings({"rawtypes", "unchecked"})
public class DepositFactory {
    
    private static DepositFactory instance = null;
    
    List<DepositMethod> methods;

    public static DepositFactory get() {
        if (null == instance) {
            instance = new DepositFactory();
        }
        return instance;
    }

    private DepositFactory() {
        methods = new ArrayList<>();
        methods.add(new MagnetMethod());
        methods.add(new QRMethod());
    }

    public void addMethod(DepositMethod method) {
        methods.add(method);
    }
    
    /**
     * Returns null if the method doesnt exist
     * 
     * @param conditions
     * @param methodType
     * @return
     */
    public <T extends DepositMethod> Object build(ConditionalDepositAddress conditions, Class<T> methodType) {
        T method = getMethod(methodType);
        if (method == null) {
            return null;
        }
        
        return method.build(conditions);
    }
    
    /**
     * Returns null if the method doesnt exist
     * 
     * @param conditions
     * @param methodType
     * @return
     */
    public <T extends DepositMethod> ConditionalDepositAddress parse(Object conditions, Class<T> methodType) {
        T method = getMethod(methodType);
        if (method == null) {
            return null;
        }
        
        return method.parse(conditions);
    }
    
    private <T extends DepositMethod> T getMethod(Class<T> clazz) {
        for (DepositMethod m : methods) {
            if (m.getClass().equals(clazz)) {
                return (T) m;
            }
        }
        
        return null;
    }
}
