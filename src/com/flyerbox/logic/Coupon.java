package com.flyerbox.logic;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by tmrafael on 03.12.2014.
 */
public class Coupon {
    private int id;
    private String title;
    private boolean used;
    private int discount;
    private Date expire;

    public Coupon(int id, String title, boolean used, int discount, Date expire) {
        this.id = id;
        this.title = title;
        this.used = used;
        this.discount = discount;
        this.expire = expire;

        getCoolTime();
    }

    public Coupon() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUsed() {
        return used;
    }

    public void setUsed(boolean used) {
        this.used = used;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public Date getExpire() {
        return expire;
    }

    public void setExpire(Date expire) {
        this.expire = expire;
    }

    public String getCoolTime() {
        Calendar diff = Calendar.getInstance();
        diff.setTimeInMillis(expire.getTime() - new Date().getTime());

        if(expire.getTime() - new Date().getTime() > 0) {
            return (diff.get(Calendar.DAY_OF_YEAR) - 1) + " days "
                    + diff.get(Calendar.HOUR_OF_DAY) + ":"
                    + diff.get(Calendar.MINUTE) + ":" + diff.get(Calendar.SECOND);
        }
        used = true;
        return "used or expired";
    }

}
