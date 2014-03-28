package com.seventh.intelligentguide.receiver;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * ¹ã²¥assetsList
 * @author rui
 *
 */
public class ActivityReceiver extends BroadcastReceiver {
	public static List<String> assetsList = new ArrayList<String>();
	@Override
	public void onReceive(Context context, Intent intent) {
		assetsList=intent.getStringArrayListExtra("assetslist");
	}
	public void setAssetsList(List<String> assetsList) {
		this.assetsList = assetsList;
	}
	public List<String> getAssetsList() {
		return assetsList;
	}
}
