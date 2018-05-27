/*
 * The MIT License
 *
 * Copyright (c) 2018 Noukkis.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package ch.gwyrin.noukkis.keepqr.keepass2android.pluginsdk;

import java.util.ArrayList;

import android.R.anim;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Broadcast flow between Host and Plugin
 * ======================================
 * 
 * The host is responsible for deciding when to initiate the session. It 
 * should initiate the session as soon as plugins are required or when a plugin 
 * has been updated through the OS.
 * It will then send a broadcast to request the currently required scope.
 * The plugin then sends a broadcast to the app which scope is required. If an 
 * access token is already available, it's sent along with the requset.
 * 
 * If a previous permission has been revoked (or the app settings cleared or the 
 * permissions have been extended or the token is invalid for any other reason) 
 * the host will answer with a Revoked-Permission broadcast (i.e. the plugin is 
 * unconnected.)
 * 
 *  Unconnected plugins must be permitted by the user (requiring user action). 
 *  When the user grants access, the plugin will receive an access token for 
 *  the host. This access token is valid for the requested scope. If the scope
 *  changes (e.g after an update of the plugin), the access token becomes invalid.
 *
 */
public abstract class PluginAccessBroadcastReceiver extends BroadcastReceiver {
	
	private static final String _tag = "Kp2aPluginSDK";
	
	@Override
	public void onReceive(Context ctx, Intent intent) {
		String action = intent.getAction();
		android.util.Log.d(_tag, "received broadcast with action="+action);
		if (action == null)
			return;
		if (action.equals(Strings.ACTION_TRIGGER_REQUEST_ACCESS))
		{
			requestAccess(ctx, intent);	
		} else if (action.equals(Strings.ACTION_RECEIVE_ACCESS))
		{
			receiveAccess(ctx, intent);
		} else if (action.equals(Strings.ACTION_REVOKE_ACCESS))
		{
			revokeAccess(ctx, intent);
		} else
		{
			//TODO handle unexpected action
		}		
	}
	
	

	private void revokeAccess(Context ctx, Intent intent) {
		String senderPackage = intent.getStringExtra(Strings.EXTRA_SENDER);
		String accessToken = intent.getStringExtra(Strings.EXTRA_ACCESS_TOKEN);
		//this intent must send the invalid(ated) token to prevent malicious apps
		//from revoking access of all plugins.
		AccessManager.removeAccessToken(ctx, senderPackage, accessToken);
	}



	private void receiveAccess(Context ctx, Intent intent) {
		String senderPackage = intent.getStringExtra(Strings.EXTRA_SENDER);
		String accessToken = intent.getStringExtra(Strings.EXTRA_ACCESS_TOKEN);
		AccessManager.storeAccessToken(ctx, senderPackage, accessToken, getScopes());
	}

	public void requestAccess(Context ctx, Intent intent) {
		String senderPackage = intent.getStringExtra(Strings.EXTRA_SENDER);
		String requestToken = intent.getStringExtra(Strings.EXTRA_REQUEST_TOKEN);
		Intent rpi = new Intent(Strings.ACTION_REQUEST_ACCESS);
		rpi.setPackage(senderPackage);
		rpi.putExtra(Strings.EXTRA_SENDER, ctx.getPackageName());
		rpi.putExtra(Strings.EXTRA_REQUEST_TOKEN, requestToken);
		
		String token = AccessManager.tryGetAccessToken(ctx, senderPackage, getScopes());
		if (token != null)
		{
			rpi.putExtra(Strings.EXTRA_ACCESS_TOKEN, token);
		}
		
		rpi.putStringArrayListExtra(Strings.EXTRA_SCOPES, getScopes());
		Log.d(_tag, "requesting access for "+getScopes().size()+" tokens.");
		ctx.sendBroadcast(rpi);
	}

	/** 
	 * 
	 * @return the list of required scopes for this plugin. 
	 */
	abstract public ArrayList<String> getScopes();

}
