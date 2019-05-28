package com.isfootball.pool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Evgeniy Pshenitsin on 21.03.2015.
 */
public class BasePool<T> implements BasePoolInterface<T> {

    private static final Logger logger = LogManager.getLogger("server");

    private BasePoolConfig config;
    private BaseObjectFactory<T> factory;
    private ScheduledExecutorService scheduler;

    private AtomicBoolean isFull = new AtomicBoolean(false);
    private AtomicInteger createdCount = new AtomicInteger(0);
    private AtomicInteger activeCount = new AtomicInteger(0);

    private ReentrantLock lockForCreate = new ReentrantLock();
    private ReentrantLock lockForFree = new ReentrantLock();
    private final Semaphore waitFree = new Semaphore(1);
    private static final Object monitor = new Object();

    private long timeAllFree = System.currentTimeMillis();

    private ConcurrentHashMap<T, Long> releasedObjects  = new ConcurrentHashMap<T, Long>();
    private ConcurrentHashMap<T, Long> busyObjects      = new ConcurrentHashMap<T, Long>();

    public BasePool(BaseObjectFactory factory) {
        this.config = new BasePoolConfig();
        this.factory = factory;
        initialize();
    }

    public BasePool(BaseObjectFactory factory, BasePoolConfig config) {
        this.config = config;
        this.factory = factory;
        initialize();
    }

    private void initialize() {
        scheduler = Executors.newScheduledThreadPool(1);
        final ScheduledFuture<?> periodicFuture = scheduler.scheduleAtFixedRate(new Runnable() {
            public void run() {
                checkBusy();
            }
        }, 0, 5, TimeUnit.SECONDS);

        //HttpParamsSetter.setSoTimeout(30 * 1000);
    }

    private void checkBusy() {
        logger.info("checkBusy{" + factory.getClass().getName() + "}: isFull [" + (isFull.get() == true ? "true" : "false") + "]" );
        logger.info("checkBusy{" + factory.getClass().getName() + "}: busy [" + activeCount.get() + "]" );
        logger.info("checkBusy{" + factory.getClass().getName() + "}: release [" + releasedObjects.size() + "]" );
    }

    private void evict() {
        logger.info("evict enter");
        lockForCreate.lock();
        try {
            synchronized (monitor) {
                if (activeCount.get() < createdCount.get()) {
                    logger.info("evict - time to work");
                    List<T> objectsForDestroy = new ArrayList<T>();
                    final long currTime = System.currentTimeMillis();
                    for (T obj : releasedObjects.keySet()) {
                        Long liveTime = releasedObjects.get(obj);
                        if ((currTime - liveTime) / 1000 > config.delayForDestroy) {
                            objectsForDestroy.add(obj);
                            releasedObjects.remove(obj);
                            createdCount.decrementAndGet();
                        }
                    }
                    if (createdCount.get() == 0) {
                        timeAllFree = System.currentTimeMillis();
                    }
                    for (T obj : objectsForDestroy) {
                        logger.info("evict - recreate obj");
                        factory.destroy(obj);
                    }
                    return;
                }
                if (createdCount.get() == 0 && (System.currentTimeMillis() - timeAllFree)/1000 > 300) {
                    logger.info("evict - recreate all");
                    factory.destroyAll();
                    timeAllFree = System.currentTimeMillis();
                } else if (createdCount.get() > 0) {
                    timeAllFree = System.currentTimeMillis();
                }
            }
        } catch (Exception e) {
            logger.fatal("evict bad news :(", e);
        } finally {
            lockForCreate.unlock();
        }
    }

    public void destroyAll() {
        synchronized (monitor) {
            factory.destroyAll();
            busyObjects.clear();
            releasedObjects.clear();
            activeCount.set(0);
            createdCount.set(0);
        }
    }

    @Override
    public T take() {
        logger.info("try to take; " + factory.getClass());
        lockForCreate.lock();
        logger.info("take enter; " + factory.getClass());
        try {
            synchronized (monitor) {
                final int active = activeCount.get();
                final int created = createdCount.get();
                if (created > 0 && active < config.maxActiveObjectsNumber) {
                        if (created > active) {
                            logger.info("get from released; " + factory.getClass());
                            T obj = (T) releasedObjects.keySet().toArray()[0];
                            releasedObjects.remove(obj);
                            busyObjects.put(obj, System.currentTimeMillis());
                            activeCount.incrementAndGet();
                            if (createdCount.get() >= config.maxActiveObjectsNumber) {
                                logger.info("is full set true all pool used;" + factory.getClass());
                                //waitFree.acquire();
                                isFull.set(true);
                            }
                            return obj;
                        } else {
                            logger.info("create new; " + factory.getClass());
                            return createNewObj();
                        }
                } else if (created == 0) {
                    logger.info("create new first; " + factory.getClass());
                    return createNewObj();
                }
            }
            //create new
            synchronized (monitor) {
                if (isFull.get()) {
                    logger.info("wait for release object; " + factory.getClass());
                    //waitFree.acquire();
                    monitor.wait();
                }
            }
            synchronized (monitor) {
                T obj = (T) releasedObjects.keySet().toArray()[0];
                releasedObjects.remove(obj);
                busyObjects.put(obj, System.currentTimeMillis());
                activeCount.incrementAndGet();
                if (activeCount.get() >= config.maxActiveObjectsNumber) {
                    logger.info("again set full");
                    //waitFree.acquire();
                    isFull.set(true);
                }
                return obj;
            }
        } catch (Exception e) {
            logger.fatal("take() exception; " + factory.getClass(), e);
        } finally {
            logger.info("take exit; " + factory.getClass());
            lockForCreate.unlock();
        }
        return null;
    }


    private T createNewObj() throws Exception {
        T obj = factory.create();
        if (obj == null) {
            logger.fatal("can't take object - creating issue");
            return null;
        }
        busyObjects.put(obj, System.currentTimeMillis());
        createdCount.incrementAndGet();
        activeCount.incrementAndGet();
        if (createdCount.get() >= config.maxActiveObjectsNumber) {
            logger.info("is full set true");
            //waitFree.acquire();
            isFull.set(true);
        }
        return obj;
    }

    @Override
    public void free(T obj) {
        logger.info("try to free; " + factory.getClass());
        lockForFree.lock();
        final long currTime = System.currentTimeMillis();
        logger.info("free enter; " + factory.getClass());
        try {
            synchronized (monitor) {
                long time = (currTime - busyObjects.remove(obj)) / 1000;
                    logger.info("object taken time - " + time + " sec.;" + Thread.currentThread().getName());
                releasedObjects.put(obj, currTime);
                logger.info("put obj as released; " + releasedObjects.size() + " ;" + factory.getClass());
                activeCount.decrementAndGet();
                if (isFull.get()) {
                        isFull.set(false);
                    logger.info("release wait monitor; " + factory.getClass());
                        //waitFree.release();
                        monitor.notify();
                }
            }
        } catch (Exception e) {
            logger.fatal("pool free error; " + factory.getClass(), e);
        } finally {
            logger.info("free exit; " + factory.getClass());
            if (activeCount.get() == 0) {
                logger.info("all objects free!!!");
            }
            lockForFree.unlock();
        }
    }

    public void recreate(T obj) {
        lockForFree.lock();
        logger.info("recreate enter");
        try {
            synchronized (monitor) {
                busyObjects.remove(obj);
                activeCount.decrementAndGet();
                T objNew = factory.create();
                if (objNew == null) {
                    logger.fatal("can't create object - creating issue");
                    return;
                }
                releasedObjects.put(objNew, System.currentTimeMillis());
                if (isFull.get()) {
                    isFull.set(false);
                    logger.info("release wait");
                    //waitFree.release();
                    monitor.notify();
                }
            }
        } catch (Exception e) {
            logger.fatal("pool recreate error", e);
        } finally {
            logger.info("recreate exit");
            lockForFree.unlock();
        }
    }

    public void recreate() {

    }

}
