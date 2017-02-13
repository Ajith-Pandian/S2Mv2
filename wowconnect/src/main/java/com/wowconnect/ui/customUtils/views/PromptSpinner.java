package com.wowconnect.ui.customUtils.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.wowconnect.R;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by thoughtchimp on 12/29/2016.
 */

public class PromptSpinner extends Spinner {
    android.view.ViewGroup.LayoutParams params;

    public PromptSpinner(Context context) {
        super(context);
    }

    public PromptSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PromptSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

   /* private int oldCount;

    @Override
    protected void onDraw(Canvas canvas) {
        if (getCount() != oldCount) {
            int height = getChildAt(0).getHeight();// + 1 ;
            oldCount = getCount();
            params = getLayoutParams();
            params.height = getCount() * (height + Utils.getInstance().getPixelAsDp(getContext(), 1));
            setLayoutParams(params);
        }

        super.onDraw(canvas);
    }*/


    @Override
    public void setAdapter(SpinnerAdapter orig) {
        final SpinnerAdapter adapter = newProxy(orig);

        super.setAdapter(adapter);

        try {
            final Method m = AdapterView.class.getDeclaredMethod(
                    "setNextSelectedPositionInt", int.class);
            m.setAccessible(true);
            m.invoke(this, -1);

            final Method n = AdapterView.class.getDeclaredMethod(
                    "setSelectedPositionInt", int.class);
            n.setAccessible(true);
            n.invoke(this, -1);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected SpinnerAdapter newProxy(SpinnerAdapter spinnerAdapter) {
        return (SpinnerAdapter) java.lang.reflect.Proxy.newProxyInstance(
                spinnerAdapter.getClass().getClassLoader(),
                new Class[]{SpinnerAdapter.class},
                new SpinnerAdapterProxy(spinnerAdapter));
    }


    /**
     * Intercepts getView() to display the prompt if position < 0
     */
    protected class SpinnerAdapterProxy implements InvocationHandler {

        SpinnerAdapter spinnerAdapter;
        Method getView;


        SpinnerAdapterProxy(SpinnerAdapter spinnerAdapter) {
            this.spinnerAdapter = spinnerAdapter;
            try {
                this.getView = SpinnerAdapter.class.getMethod(
                        "getView", int.class, View.class, ViewGroup.class);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
            try {
                return m.equals(getView) &&
                        (Integer) (args[0]) < 0 ?
                        getView((Integer) args[0], (View) args[1], (ViewGroup) args[2]) :
                        m.invoke(spinnerAdapter, args);
            } catch (InvocationTargetException e) {
                throw e.getTargetException();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        View getView(int position, View convertView, ViewGroup parent)
                throws IllegalAccessException {

            if (position < 0) {
                final View v =
                        ((LayoutInflater) getContext().getSystemService(
                                Context.LAYOUT_INFLATER_SERVICE)).inflate(
                                R.layout.item_spinner, parent, false);
                TextView textView = (TextView) v.findViewById(R.id.text_spinner);
                textView.setText(getPrompt());
                textView.setTextColor(getResources().getColor(R.color.text_input_layer_color));
                return v;
            }
            return spinnerAdapter.getView(position, convertView, parent);
        }
    }
}
