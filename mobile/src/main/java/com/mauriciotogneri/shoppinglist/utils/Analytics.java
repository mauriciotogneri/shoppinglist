package com.mauriciotogneri.shoppinglist.utils;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;
import com.mauriciotogneri.shoppinglist.model.CartItem;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Analytics
{
    private final Tracker tracker;
    private final ExecutorService threadPool;

    public Analytics(Tracker tracker)
    {
        this.tracker = tracker;
        this.threadPool = Executors.newFixedThreadPool(10);
    }

    public synchronized void sendAppLaunched()
    {
        threadPool.submit(() -> {
            tracker.setScreenName("App Launched");
            tracker.send(new HitBuilders.ScreenViewBuilder().build());
        });

        threadPool.submit(() -> {
            Date date = new Date();
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(date);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            EventBuilder eventBuilder = new EventBuilder();
            eventBuilder.setCategory("TIME_APP_OPEN");
            eventBuilder.setAction(String.valueOf(hour));
            tracker.send(eventBuilder.build());
        });
    }

    public synchronized void addedCartItem(CartItem cartItem)
    {
        threadPool.submit(() -> {
            EventBuilder eventBuilder = new EventBuilder();
            eventBuilder.setCategory("ADDED_PRODUCT_TO_CART");
            eventBuilder.setAction(cartItem.getCategory().getName());
            tracker.send(eventBuilder.build());
        });
    }
}