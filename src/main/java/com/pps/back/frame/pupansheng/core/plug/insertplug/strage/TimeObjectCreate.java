package com.pps.back.frame.pupansheng.core.plug.insertplug.strage;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @author
 * @discription;
 * @time 2020/11/10 11:23
 */
@Component
public class TimeObjectCreate implements InitializingBean,ObjectCreate {
    @Override
    public void afterPropertiesSet() throws Exception {
        ObjectCreateFactory.put(Date.class,this);
    }

    @Override
    public Object product() {
        return new Date();
    }
}
