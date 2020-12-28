package com.pps.back.frame.pupansheng.core.transaction.isolevel;

public enum IsolationLevel {

   TRANSACTION_NONE(0),
   TRANSACTION_DEFAULT(-1),
   TRANSACTION_READ_UNCOMMITTED(1),
   TRANSACTION_READ_COMMITTED(2),
   TRANSACTION_REPEATABLE_READ(4),
   TRANSACTION_SERIALIZABLE(8);

    private int level;

    IsolationLevel(int level){
        this.level=level;
    }

    public int getLevel(){
        return  this.level;
    }


}
