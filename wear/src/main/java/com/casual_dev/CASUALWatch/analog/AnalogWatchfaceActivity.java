package com.casual_dev.CASUALWatch.analog;

import android.os.Bundle;
import android.widget.TextView;

import com.casual_dev.CASUALWatch.R;
import com.casual_dev.CASUALWatch.widget.WatchFaceLifecycle;
import com.casual_dev.casualmessenger.Message;
import com.casual_dev.casualmessenger.WatchMessaging;
import com.casual_dev.casualmessenger.observer.MessageObserver;
import com.twotoasters.watchface.gears.activity.GearsWatchfaceActivity;
import com.twotoasters.watchface.gears.widget.IWatchface;

public class AnalogWatchfaceActivity extends GearsWatchfaceActivity implements WatchFaceLifecycle.Listener, MessageObserver.MessageInterface {

    static AnalogWatchfaceActivity instance;

    TextView mCASUAL;
    TextView mDev;

    public static AnalogWatchfaceActivity getInstance() {
        return instance;
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.analog_watchface;
    }

    @Override
    protected IWatchface getWatchface() {
        return (IWatchface) findViewById(R.id.watchface);
    }


    @Override
    protected void onCreate(Bundle saved) {
        super.onCreate(saved);
        instance = this;
        setWidgets();
        MessageObserver.connect(this);

    }

    private void setWidgets() {

        mCASUAL = (TextView) findViewById(R.id.analogtoptext);
        mDev = (TextView) findViewById(R.id.analogbottomtext);

        WatchMessaging wm = new WatchMessaging(getFilesDir().getAbsolutePath());
        try {
            wm.load();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        setDataItems(wm);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onScreenDim() {

    }

    @Override
    public void onScreenAwake() {

    }

    @Override
    public void onWatchFaceRemoved() {

    }

    @Override
    public void onScreenOff() {

    }


    public void setDataItems(WatchMessaging wm) {
        setPrimaryText(wm.getObject(Message.ITEMS.TOPTEXTTAG, String.class));
        setSecondaryText((String) wm.getObject(Message.ITEMS.BOTTOMTEXTTAG, String.class));
    }

    public void setPrimaryText(String s) {
        ;
        if (null == s || s.isEmpty()) {
            s = "CASUAL";
        }
        mCASUAL.setText(s);
    }

    public void setSecondaryText(String s) {

        if (null == s || s.isEmpty()) {
            s = "DEV";
        }
        mDev.setText(s);

    }


    @Override
    public void onMessageReceived(final Message message) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                mCASUAL.setText(message.get(Message.ITEMS.TOPTEXTTAG, String.class));
                mDev.setText(message.get(Message.ITEMS.BOTTOMTEXTTAG, String.class));

            }
        });
    }
}
