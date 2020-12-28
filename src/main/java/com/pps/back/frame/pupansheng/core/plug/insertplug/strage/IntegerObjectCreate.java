package com.pps.back.frame.pupansheng.core.plug.insertplug.strage;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author
 * @discription;
 * @time 2020/11/10 11:23
 */
@Component
public class IntegerObjectCreate implements InitializingBean,ObjectCreate {
    @Override
    public void afterPropertiesSet() throws Exception {
        ObjectCreateFactory.put(Integer.class,this);
    }

    @Override
    public Object product() {
        return 1;
    }
}
