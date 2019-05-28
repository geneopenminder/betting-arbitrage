package com.isfootball.branching;

import com.isfootball.parser.BetType;

/**
 * Created by Evgeniy Pshenitsin on 03.02.2015.
 */
public class Formula {

    public int id;

    public String title;

    public BetType var1;

    public BetType var2;

    public BetType var3;

    public Formula(int id, String title, BetType var1, BetType var2, BetType var3) {
        this.id = id;
        this.title = title;
        this.var1 = var1;
        this.var2 = var2;
        this.var3 = var3;
    }

    public boolean isTwoArgs() {
        return var3 == null ? true : false;
    }
}
