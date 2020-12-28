package com.pps.back.frame.pupansheng.core.plug.filterplug;

import com.github.pagehelper.util.MetaObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.reflection.MetaObject;

import java.util.*;


/**
 * @author
 * @discription;
 * @time 2020/10/9 10:13
 */
@Slf4j
public class Condition {
    private Map<String,Object> filterData=new HashMap<>();
    private Set<String> keys=new LinkedHashSet<>();
    private SqlResove sqlResove=new DefaultSqlResove();
    private String flagString;
    private String sqlStart;
    private String lastParamname;
    private FlagStringMode flagStringMode;
    private FilterSqlPostion filterSqlPostion;



    public Object processParameterObject(MappedStatement ms, Object parameterObject, BoundSql boundSql, CacheKey pageKey) {
            Map<String, Object> paramMap = null;
            if (parameterObject == null) {
                paramMap = new HashMap();
            } else if (parameterObject instanceof Map) {
                paramMap = new HashMap();
                paramMap.putAll((Map)parameterObject);
            } else {
                paramMap = new HashMap();
                boolean hasTypeHandler = ms.getConfiguration().getTypeHandlerRegistry().hasTypeHandler(parameterObject.getClass());
                MetaObject metaObject = MetaObjectUtil.forObject(parameterObject);
                if (!hasTypeHandler) {
                    String[] var9 = metaObject.getGetterNames();
                    int var10 = var9.length;

                    for(int var11 = 0; var11 < var10; ++var11) {
                        String name = var9[var11];
                        paramMap.put(name, metaObject.getValue(name));
                    }
                }

                if (boundSql.getParameterMappings() != null && boundSql.getParameterMappings().size() > 0) {
                    Iterator var13 = boundSql.getParameterMappings().iterator();

                    ParameterMapping parameterMapping;
                    String name;
                    do {
                        do {
                            do {
                                do {
                                    if (!var13.hasNext()) {
                                        return   getParam(ms,boundSql,paramMap);
                                    }

                                    parameterMapping = (ParameterMapping)var13.next();
                                    name = parameterMapping.getProperty();
                                } while(false);
                            } while(false);
                        } while(paramMap.get(name) != null);
                    } while(!hasTypeHandler && !parameterMapping.getJavaType().equals(parameterObject.getClass()));

                    paramMap.put(name, parameterObject);
                }
            }

          return   getParam(ms,boundSql,paramMap);

    }

    public Object getParam(MappedStatement ms, BoundSql boundSql, Map paramMap){


        paramMap.putAll(filterData);

        List<ParameterMapping> newParameterMappings =new ArrayList<>();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {

            //过滤条件是插入其中的
            if(filterSqlPostion==FilterSqlPostion.meddle){

                for (int i = 0; i < parameterMappings.size() ; i++) {
                    ParameterMapping p=parameterMappings.get(i);
                    if(p.getProperty().equals(lastParamname)){
                        newParameterMappings.add(p);
                        List<ParameterMapping> finalNewParameterMappings1 = newParameterMappings;
                        filterData.forEach((k, v)->{
                            ParameterMapping build = new ParameterMapping.Builder(ms.getConfiguration(), k, v != null ? v.getClass() : null).build();
                            finalNewParameterMappings1.add(build);
                        });
                    }else {
                        newParameterMappings.add(p);
                    }
                }

            }else if(filterSqlPostion==FilterSqlPostion.end){
                newParameterMappings=new ArrayList<>(parameterMappings);
                List<ParameterMapping> finalNewParameterMappings = newParameterMappings;
                filterData.forEach((k, v) -> {
                    String newK = k;
                    finalNewParameterMappings.add((new ParameterMapping.Builder(ms.getConfiguration(), newK, v != null ? v.getClass() : null)).build());
                });
            }else {//

                List<ParameterMapping> finalNewParameterMappings1 = newParameterMappings;
                filterData.forEach((k, v)->{
                    ParameterMapping build = new ParameterMapping.Builder(ms.getConfiguration(), k, v != null ? v.getClass() : null).build();
                    finalNewParameterMappings1.add(build);
                });

                finalNewParameterMappings1.addAll(parameterMappings);
            }
        }

        MetaObject metaObject = MetaObjectUtil.forObject(boundSql);
        metaObject.setValue("parameterMappings", newParameterMappings);

        return paramMap;


    }


    public void addQueryFilter(String columnName,String valueName,Object value){
        keys.add(columnName);
        filterData.put(valueName,value);
    }

    public Set<String> getKeys() {
        return keys;
    }

    public void setKeys(Set<String> keys) {
        this.keys = keys;
    }

    public Map<String, Object> getFilterData(){
        return filterData;
    }


    public SqlResove getSqlResove() {
        return sqlResove;
    }

    public void setSqlResove(SqlResove sqlResove) {
        this.sqlResove = sqlResove;
    }

    public String filterSql(BoundSql boundSql, Object parameter) {
     return       sqlResove.filterSql(this,boundSql,parameter);
    }
    public String getFlagString() {
        return flagString;
    }

    void setFlagString(String flagString) {
        this.flagString = flagString;
    }

    public String getSqlStart() {
        return sqlStart;
    }

    public FlagStringMode getFlagStringMode() {
        return flagStringMode;
    }

     void setFlagStringMode(FlagStringMode flagStringMode) {
        this.flagStringMode = flagStringMode;
    }
     void setSqlStart(String sqlStart) {
        this.sqlStart = sqlStart;
    }

    public String getLastParamname() {
        return lastParamname;
    }

    void setLastParamname(String lastParamname) {
        this.lastParamname = lastParamname;
    }

    public FilterSqlPostion getFilterSqlPostion() {
        return filterSqlPostion;
    }

    void setFilterSqlPostion(FilterSqlPostion filterSqlPostion) {
        this.filterSqlPostion = filterSqlPostion;
    }
}
