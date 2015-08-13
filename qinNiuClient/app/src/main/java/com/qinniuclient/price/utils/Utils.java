package com.qinniuclient.price.utils;

import android.content.Context;
import android.util.TypedValue;
/*这个类好像在其他地方没有用到，不过不敢将其删除 子博*/
public class Utils {

    public static int dp2px(Context context, int dp) {
        return Math.round(TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics()));
    }

}
