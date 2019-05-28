package com.isfootball.parser;

import com.isfootball.model.BasicEvent;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by Evgeniy Pshenitsin on 16.06.2015.
 */
public abstract class BaseDownloader implements Callable<List<BasicEvent>> {

    @Override
    public List<BasicEvent> call() {
        ParserCore.ThreadWDInfo info = new ParserCore.ThreadWDInfo();
        info.start = System.currentTimeMillis();
        info.thread = Thread.currentThread();
        ParserCore.putThread(info);
        List<BasicEvent> ret = download();
        ParserCore.removeThread(info.thread.getId());
        return ret;
    }

    public abstract List<BasicEvent> download();

}
