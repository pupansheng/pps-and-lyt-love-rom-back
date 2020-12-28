package com.pps.back.frame.pupansheng.core.plug.insertplug.strage;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * @author
 * @discription;
 * @time 2020/11/10 11:23
 */
@Component
public class StringObjectCreate implements InitializingBean,ObjectCreate {
    @Override
    public void afterPropertiesSet() throws Exception {
        ObjectCreateFactory.put(String.class,this);
    }

    @Override
    public Object product() {
        return "";
    }
}
