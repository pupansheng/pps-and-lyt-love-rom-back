package com.pps.back.frame.pupansheng.custom.db;

import java.util.Collection;
import java.util.List;

public interface GlobalDb {

   Object getValue(Object key);


   Object putValue(Object key,Object value);

   String putValue(Object value);


   Collection<Object> getAllValues();


   Object deleteValue(Object key);


}
