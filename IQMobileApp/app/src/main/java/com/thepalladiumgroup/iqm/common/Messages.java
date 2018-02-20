package com.thepalladiumgroup.iqm.common;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Koske Kimutai [2016/04/12]
 */
public class Messages {
    public static void show(Context context,String message){
        Toast toast=Toast.makeText(context,message,Toast.LENGTH_LONG);
        toast.show();
    }
}
