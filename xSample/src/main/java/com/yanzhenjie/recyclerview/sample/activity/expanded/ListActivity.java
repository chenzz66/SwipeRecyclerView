/*
 * Copyright 2019 Zhenjie Yan
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
package com.yanzhenjie.recyclerview.sample.activity.expanded;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.yanzhenjie.recyclerview.OnItemClickListener;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;
import com.yanzhenjie.recyclerview.sample.R;
import com.yanzhenjie.recyclerview.sample.activity.BaseActivity;
import com.yanzhenjie.recyclerview.sample.activity.expanded.entity.Group;
import com.yanzhenjie.recyclerview.sample.activity.expanded.entity.GroupMember;
import com.yanzhenjie.recyclerview.sample.adapter.ExpandedAdapter;
import com.yanzhenjie.recyclerview.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

/**
 * Created by Zhenjie Yan on 1/30/19.
 */
public class ListActivity extends BaseActivity {

    private SwipeRecyclerView mRecyclerView;
    private ExpandedAdapter mAdapter;
    private List<Group> mDataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color)));
        mRecyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                // ?????????position?????????item?????????parent item
                if (mAdapter.isParentItem(position)) {
                    // ??????parent position
                    final int parentPosition = mAdapter.parentItemPosition(position);

                    // ??????parent???????????????????????????
                    if (mAdapter.isExpanded(parentPosition)) {
                        mDataList.get(parentPosition).setExpanded(false);
                        mAdapter.notifyParentChanged(parentPosition);

                        // ?????????parent??????????????????
                        mAdapter.collapseParent(parentPosition);
                    } else {
                        mDataList.get(parentPosition).setExpanded(true);
                        mAdapter.notifyParentChanged(parentPosition);

                        // ?????????parent??????????????????
                        mAdapter.expandParent(parentPosition);
                    }
                } else {
                    // ??????parent position
                    int parentPosition = mAdapter.parentItemPosition(position);
                    // ??????child position
                    int childPosition = mAdapter.childItemPosition(position);
                    String message = String.format("??????%1$d?????????%2$d??????", parentPosition, childPosition);
                    Toast.makeText(ListActivity.this, message, Toast.LENGTH_LONG).show();
                }
            }
        });

        mAdapter = new ExpandedAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        refresh();
    }

    /**
     * ????????????
     */
    private void refresh() {
        mDataList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Group group = new Group();
            group.setName("??????????????????????????? " + i);
            group.setMemberList(new ArrayList<GroupMember>());
            for (int j = 0; j < 10; j++) {
                GroupMember member = new GroupMember();
                member.setName("??????????????????????????? " + j);
                group.getMemberList().add(member);
            }
            mDataList.add(group);
        }

        mAdapter.setGroupList(mDataList);
        mAdapter.notifyDataSetChanged();
    }
}