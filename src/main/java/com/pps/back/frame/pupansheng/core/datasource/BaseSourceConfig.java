package com.pps.back.frame.pupansheng.core.datasource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author
 * @discription;
 * @time 2020/9/6 12:38
 */
@Slf4j
public class BaseSourceConfig {


    private  String type;

    private   MybatisConfig mybatisConfig=new MybatisConfig();

    private   DataBaseProperty database=new DataBaseProperty();

    private  TransationProperty transationProperty=new TransationProperty();


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public DataBaseProperty getDatabase() {
        return database;
    }

    public void setDatabase(DataBaseProperty database) {
        this.database = database;
    }

    public TransationProperty getTransationProperty() {
        return transationProperty;
    }

    public void setTransationProperty(TransationProperty transationProperty) {
        this.transationProperty = transationProperty;
    }

    public  static class  TransationProperty{

        private int transactionSynchronization = 0;
        private int defaultTimeout = -1;
        private boolean nestedTransactionAllowed = false;
        private boolean validateExistingTransaction = false;
        private boolean globalRollbackOnParticipationFailure = true;
        private boolean failEarlyOnGlobalRollbackOnly = false;
        private boolean rollbackOnCommitFailure = false;
        private boolean enforceReadOnly;

        public int getTransactionSynchronization() {
            return transactionSynchronization;
        }

        public void setTransactionSynchronization(int transactionSynchronization) {
            this.transactionSynchronization = transactionSynchronization;
        }

        public int getDefaultTimeout() {
            return defaultTimeout;
        }

        public void setDefaultTimeout(int defaultTimeout) {
            this.defaultTimeout = defaultTimeout;
        }

        public boolean isNestedTransactionAllowed() {
            return nestedTransactionAllowed;
        }

        public void setNestedTransactionAllowed(boolean nestedTransactionAllowed) {
            this.nestedTransactionAllowed = nestedTransactionAllowed;
        }

        public boolean isValidateExistingTransaction() {
            return validateExistingTransaction;
        }

        public void setValidateExistingTransaction(boolean validateExistingTransaction) {
            this.validateExistingTransaction = validateExistingTransaction;
        }

        public boolean isGlobalRollbackOnParticipationFailure() {
            return globalRollbackOnParticipationFailure;
        }

        public void setGlobalRollbackOnParticipationFailure(boolean globalRollbackOnParticipationFailure) {
            this.globalRollbackOnParticipationFailure = globalRollbackOnParticipationFailure;
        }

        public boolean isFailEarlyOnGlobalRollbackOnly() {
            return failEarlyOnGlobalRollbackOnly;
        }

        public void setFailEarlyOnGlobalRollbackOnly(boolean failEarlyOnGlobalRollbackOnly) {
            this.failEarlyOnGlobalRollbackOnly = failEarlyOnGlobalRollbackOnly;
        }

        public boolean isRollbackOnCommitFailure() {
            return rollbackOnCommitFailure;
        }

        public void setRollbackOnCommitFailure(boolean rollbackOnCommitFailure) {
            this.rollbackOnCommitFailure = rollbackOnCommitFailure;
        }

        public boolean isEnforceReadOnly() {
            return enforceReadOnly;
        }

        public void setEnforceReadOnly(boolean enforceReadOnly) {
            this.enforceReadOnly = enforceReadOnly;
        }
    }
    public static  class DataBaseProperty{


        private String filters;
        private String url;
        private String username;
        private String password;
        private String driverClassName;
        private int initialSize;
        private int minIdle;
        private int maxActive;
        private long maxWait;
        private long timeBetweenEvictionRunsMillis;
        private long minEvictableIdleTimeMillis;
        private String validationQuery;
        private boolean testWhileIdle;
        private boolean testOnBorrow;
        private boolean testOnReturn;
        private boolean poolPreparedStatements;
        private int maxPoolPreparedStatementPerConnectionSize;

        public String getFilters() {
            return filters;
        }

        public void setFilters(String filters) {
            this.filters = filters;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public int getInitialSize() {
            return initialSize;
        }

        public void setInitialSize(int initialSize) {
            this.initialSize = initialSize;
        }

        public int getMinIdle() {
            return minIdle;
        }

        public void setMinIdle(int minIdle) {
            this.minIdle = minIdle;
        }

        public int getMaxActive() {
            return maxActive;
        }

        public void setMaxActive(int maxActive) {
            this.maxActive = maxActive;
        }

        public long getMaxWait() {
            return maxWait;
        }

        public void setMaxWait(long maxWait) {
            this.maxWait = maxWait;
        }

        public long getTimeBetweenEvictionRunsMillis() {
            return timeBetweenEvictionRunsMillis;
        }

        public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
            this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
        }

        public long getMinEvictableIdleTimeMillis() {
            return minEvictableIdleTimeMillis;
        }

        public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
            this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
        }

        public String getValidationQuery() {
            return validationQuery;
        }

        public void setValidationQuery(String validationQuery) {
            this.validationQuery = validationQuery;
        }

        public boolean isTestWhileIdle() {
            return testWhileIdle;
        }

        public void setTestWhileIdle(boolean testWhileIdle) {
            this.testWhileIdle = testWhileIdle;
        }

        public boolean isTestOnBorrow() {
            return testOnBorrow;
        }

        public void setTestOnBorrow(boolean testOnBorrow) {
            this.testOnBorrow = testOnBorrow;
        }

        public boolean isTestOnReturn() {
            return testOnReturn;
        }

        public void setTestOnReturn(boolean testOnReturn) {
            this.testOnReturn = testOnReturn;
        }

        public boolean isPoolPreparedStatements() {
            return poolPreparedStatements;
        }

        public void setPoolPreparedStatements(boolean poolPreparedStatements) {
            this.poolPreparedStatements = poolPreparedStatements;
        }

        public int getMaxPoolPreparedStatementPerConnectionSize() {
            return maxPoolPreparedStatementPerConnectionSize;
        }

        public void setMaxPoolPreparedStatementPerConnectionSize(int maxPoolPreparedStatementPerConnectionSize) {
            this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
        }
    }

    public MybatisConfig getMybatisConfig() {
        return mybatisConfig;
    }

    public void setMybatisConfig(MybatisConfig mybatisConfig) {
        this.mybatisConfig = mybatisConfig;
    }

    public static  class  MybatisConfig{

        private String [] mapperXmlLocation;

        private  String [] mapperLocation;

        private  String [] typeAliasesPackageList;

        public String[] getMapperXmlLocation() {
            return mapperXmlLocation;
        }

        public void setMapperXmlLocation(String[] mapperXmlLocation) {
            this.mapperXmlLocation = mapperXmlLocation;
        }

        public String[] getMapperLocation() {
            return mapperLocation;
        }

        public void setMapperLocation(String[] mapperLocation) {
            this.mapperLocation = mapperLocation;
        }

        public String[] getTypeAliasesPackageList() {
            return typeAliasesPackageList;
        }

        public void setTypeAliasesPackageList(String[] typeAliasesPackageList) {
            this.typeAliasesPackageList = typeAliasesPackageList;
        }
    }


}
