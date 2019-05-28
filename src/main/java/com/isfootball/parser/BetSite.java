package com.isfootball.parser;

/**
 * Created by Evgeniy Pshenitsin on 02.06.2015.
 */
public enum BetSite {
    NONE("NONE"),

    BET_10("ru.10bet.com"),
    BET_188("sb.188bet.com"),
    BETCITY("www.betsbc.com"),
    BETDAQ("www.betdaq.com"),
    BETFAIR("www.betfair.com"),
    BETIN("sports18.betin.com"),
    BETVICTOR("www.betvictor.mobi"),
    BETWAY("sports.betway.com"),
    CASHPOINT("www.cashpoint.com"),
    CORAL("sports.coral.co.uk"),
    EXPEKT("en.expekt.com"),
    GOLPAS("www.golpas.com"),
    INTERWETTEN("www.interwetten.com"),
    LIGASTAVOK("www.liga-stavok.com"),
    MARATHONBET("www.marathonbet.com"),
    MATCHBOOK("www.matchbook.com"),
    OLIMPIKZ("olimpkz.com"),
    ONEXBET("1-x-bet.com"),
    PARIMATCH("parimatchru.com"),
    PINNACLE("www.pin1111.com", 4 * 60 * 1000),
    PLANETWIN365("planetwin365.com"),
    RUUNIBET("ru1.unibet.com"),
    SBOBET("www.sbobet.com"),
    SMARKETS("smarkets.com"),
    TENNISI("www.tennisi.com"),
    TIPICO("www.tipico.com"),
    WINLINEBET("winlinebet.com"),
    ZENITHBET("zenitbet.com"),
    BALTBET("www.baltbet.com"),
    BET365("mobile.bet365.com"),
    WILLIAM_HILL("http://sports.williamhill.com"),
    FONEBET("www.bkfon.ru/ru/line/");

    private String name;

    private int ttl; //millis


    private BetSite(String name, int ttl) {
        this.name = name;
        this.ttl = ttl;
    }

    BetSite(String name) {
         this.name = name;
         this.ttl = 20 * 60 * 1000;
    }

    public String getName() {
        return this.name;
    }

    public int getTtl() {
        return ttl;
    }

}
