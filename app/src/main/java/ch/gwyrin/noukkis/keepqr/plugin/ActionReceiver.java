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

package ch.gwyrin.noukkis.keepqr.plugin;


import android.content.Intent;
import android.os.Bundle;

import com.google.zxing.Result;

import ch.gwyrin.noukkis.keepqr.R;
import ch.gwyrin.noukkis.keepqr.ctrl.Main;
import ch.gwyrin.noukkis.keepqr.ctrl.QrScanner;
import ch.gwyrin.noukkis.keepqr.keepass2android.pluginsdk.PluginAccessException;
import ch.gwyrin.noukkis.keepqr.keepass2android.pluginsdk.PluginActionBroadcastReceiver;
import ch.gwyrin.noukkis.keepqr.model.KeepassEntry;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Noukkis on 26.05.2018.
 */

public class ActionReceiver extends PluginActionBroadcastReceiver implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;

    @Override
    protected void openEntry(OpenEntryAction oe) {
        try {
            oe.addEntryAction("KeepQR", R.drawable.ic_launcher_background, null);
        } catch (PluginAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void actionSelected(ActionSelectedAction actionSelected) {
        KeepassEntry keepass = new KeepassEntry();
        keepass.setName(actionSelected.getEntryFields().get("Title"));
        keepass.setUsername(actionSelected.getEntryFields().get("UserName"));
        keepass.setPassword(actionSelected.getEntryFields().get("Password"));
        Intent i = new Intent(actionSelected.getContext(), QrScanner.class);
        Bundle b = new Bundle();
        b.putString("keepass", keepass.toString()); //Your id
        i.putExtras(b);
        actionSelected.getContext().startActivity(i);
    }

    @Override
    protected void entryOutputModified(EntryOutputModifiedAction eom) {
            System.out.println(eom.getModifiedFieldId());
    }

    @Override
    public void handleResult(Result result) {

    }
}
