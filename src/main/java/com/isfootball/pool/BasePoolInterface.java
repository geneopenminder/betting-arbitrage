package com.isfootball.pool;

/**
 * Created by Evgeniy Pshenitsin on 11.08.2015.
 */
public interface BasePoolInterface<T> {

    public T take();

    public void free(T obj);

    public void destroyAll();

    public void recreate();

}