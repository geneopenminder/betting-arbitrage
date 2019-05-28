package com.isfootball.pool;

import nf.fr.eraasoft.pool.ObjectPool;
import nf.fr.eraasoft.pool.PoolException;
import nf.fr.eraasoft.pool.PoolSettings;
import nf.fr.eraasoft.pool.PoolableObjectBase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.isfootball.pool.ProcessMonitor.ChromedriverProcess;

/**
 * Created by Evgeniy Pshenitsin on 11.08.2015.
 */
public class FuriousPool<T> implements BasePoolInterface<T> {

    private static final Logger logger = LogManager.getLogger("server");

    private BasePoolConfig config;
    private final BaseObjectFactory<T> factory;
    private volatile ObjectPool<T> objectPool;
    private final ConcurrentMap<T, ChromedriverProcess> lifeTime = new ConcurrentHashMap<T, ChromedriverProcess>();

    private final ConcurrentMap<T, ObjectPool<T>> oldObjects = new ConcurrentHashMap<T, ObjectPool<T>>();

    private volatile Boolean isRecreateInProgress = false;
    private volatile Object waitRecreate = new Object();

    public FuriousPool(BaseObjectFactory factory, BasePoolConfig config) {
        this.config = config;
        this.factory = factory;
        objectPool = createNewPool();
    }

    private ObjectPool<T> createNewPool() {
        PoolSettings<T> poolSettings = new PoolSettings<T>(
                new PoolableObjectBase<T>() {
                    @Override
                    public T make() {
                        /*logger.info("make size: " + lifeTime.size());
                        synchronized (FuriousPool.class) {
                            T obj = (T) factory.create();
                            ChromedriverProcess ps = ProcessMonitor.getLastRunningProcess();
                            if (ps != null) {
                                lifeTime.put(obj, ps);
                            }
                            return obj;*/
                        return (T)factory.create();
                    }

                    @Override
                    public void activate(T webDriver) throws PoolException {

                    }

                    @Override
                    public void destroy(T t) {
                        logger.error("FuriousPool destroy!!!!!!!");
                        /*synchronized (FuriousPool.class) {
                            try {
                                WebDriver wd = (WebDriver) t;
                                wd.close();
                                wd.quit();
                            } catch (Exception e) {
                                logger.error("FuriousPool destroy error", e);
                            }

                            ChromedriverProcess ps = lifeTime.remove(t);
                            ProcessMonitor.destroyInstance(ps);
                            try {
                                Thread.sleep(5000);
                            } catch (Exception e) {
                            }
                        }*/
                    }

                    @Override
                    public void passivate(T webDriver) {
                        factory.beforeFree(webDriver);
                    }

                    @Override
                    public boolean validate(T t) {
                        /*try {
                            //5 min
                            if ((System.currentTimeMillis() - lifeTime.get(t).startTime) > 1000 * 60 * 5000) {
                                //lifeTime.remove(t);
                                logger.error("FuriousPool validate recreate object");
                                return false;
                            }
                        } catch (Exception e) {
                            logger.error("FuriousPool validate error!!!!!!!", e);
                            return false;
                        }
                        logger.error("FuriousPool validate true");*/
                        return true;
                    }

                });

// Add some settings
        poolSettings.min(config.maxActiveObjectsNumber).max(config.maxActiveObjectsNumber);
        poolSettings.maxIdle(90000);
        poolSettings.maxWait(90000);
        poolSettings.debug(true);
        poolSettings.validateWhenReturn(true);
        return poolSettings.pool();
    }

    public T take() {
        try {
            if (isRecreateInProgress) {
                logger.info("waitRecreate try to lock");
                synchronized (waitRecreate) {
                    logger.info("waitRecreate enter");
                }
            }
            T obj = objectPool.getObj();
            oldObjects.put(obj, this.objectPool);
            //logger.info("FuriousPool take(" + obj.getClass() + "): ", new Throwable());
            return obj;
        } catch (Exception e) {
            logger.error("FuriousPool take exception", e);
        }
        return null;
    }

    public void free(T obj) {
        //logger.info("FuriousPool free(" + obj.getClass() + "): ", new Throwable());
        logger.info("FuriousPool free(" + obj.getClass() + ")");
        ObjectPool<T> pool = oldObjects.remove(obj);
        if (pool != null) {
            try {
                pool.returnObj(obj);
            } catch (Exception e) {
                logger.info("pool can't return obj", e);
            }
        } else {
            logger.fatal("NO POOL!!!");
        }
    }

    public void destroyAll() {
       logger.info("FuriousPool destroyAll");
       factory.destroyAll();
    }

    public void recreate() {
        isRecreateInProgress = true;
        synchronized (waitRecreate) {
            logger.info("waitRecreate recreate");
            try {
                this.objectPool = createNewPool();
            } catch (Exception e) {
            } finally {
                isRecreateInProgress = false;
            }
        }
        logger.info("waitRecreate recreate complete");
    }

}