package me.jaks.jdns.webapp.rest;

import me.jaks.jdns.webapp.RecordDao;
import me.jaks.jdns.webapp.RecordDaoInt;

import com.google.inject.AbstractModule;

public class RecordModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(RecordDaoInt.class).to(RecordDao.class);
    }
}