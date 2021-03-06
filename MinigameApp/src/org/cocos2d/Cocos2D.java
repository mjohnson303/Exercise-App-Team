/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.cocos2d;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class Cocos2D extends ListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setListAdapter(new SimpleAdapter(this,
                (List<Map<String, ?>>)getData("org.cocos2d.tests"),
                android.R.layout.simple_list_item_1, new String[]{"title"},
                new int[]{android.R.id.text1}));
        getListView().setTextFilterEnabled(true);
    }

    protected List<?> getData(String prefix) {
        List<Map<String,?>> myData = new ArrayList<Map<String,?>>();


        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_TEST);

        PackageManager pm = getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(mainIntent, 0);

        if (null == list)
            return myData;

        /*
        String[] prefixPath;
        if (prefix.equals("")) {
            prefixPath = null;
        } else {
            prefixPath = prefix.split("/");
        }*/
        int len = list.size();

        // Map<String, Boolean> entries = new HashMap<String, Boolean>();


        for (int i = 0; i < len; i++) {
            ResolveInfo info = list.get(i);
            String activityName = info.activityInfo.name;
            if (prefix.length() == 0 || activityName.startsWith(prefix)) {
                String[] labelPath = activityName.split("\\.");
                String nextLabel = labelPath[labelPath.length - 1];
                addItem(myData, nextLabel, activityIntent(
                        info.activityInfo.applicationInfo.packageName,
                        info.activityInfo.name));
            }
        }
        Collections.sort(myData, sDisplayNameComparator);
        return myData;
    }

    private final static Comparator<Map<String, ?>> sDisplayNameComparator = new Comparator<Map<String, ?>>() {
        private final Collator collator = Collator.getInstance();

        public int compare(Map<String,?> map1, Map<String,?> map2) {
            return collator.compare(map1.get("title"), map2.get("title"));
        }
    };

    protected Intent activityIntent(String pkg, String componentName) {
        Intent result = new Intent();
        result.setClassName(pkg, componentName);
        return result;
    }

    protected Intent browserIntent(String path) {
        Intent result = new Intent();
        result.setClass(this, Cocos2D.class);
        result.putExtra("org.cocos2d.tests.Path", path);
        return result;
    }

    protected void addItem(List<Map<String,?>> data, String name, Intent intent) {
        Map<String, Object> temp = new HashMap<String, Object>();
        temp.put("title", name);
        temp.put("intent", intent);
        data.add(temp);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Map<String,?> map = (Map<String,?>) l.getItemAtPosition(position);
        Intent intent = (Intent) map.get("intent");
        startActivity(intent);
    }
}
