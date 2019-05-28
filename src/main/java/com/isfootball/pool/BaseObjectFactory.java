package com.isfootball.pool;

/**
 * Created by Evgeniy Pshenitsin on 21.03.2015.
 */
public abstract class BaseObjectFactory<T> {

    public abstract T create();

    public abstract void destroy(T obj);

    public void beforeTake(T obj) {};

    public void beforeFree(T obj) {};

    public abstract void destroyAll();

}
