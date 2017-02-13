package com.wowconnect.ui.adapters;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

import com.wowconnect.R;


/**
 * Created by thoughtchimp on 11/23/2016.
 */

public class CardMenuClickListener implements PopupMenu.OnMenuItemClickListener {

    private int position;

    public CardMenuClickListener(int position) {
        this.position = position;
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.menu_edit:
                return true;
            case R.id.menu_delete:
                return true;
            default:
        }
        return false;
    }
}
