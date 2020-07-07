package com.aghamiri.apkbackup.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;


import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.aghamiri.apkbackup.R;
import com.aghamiri.apkbackup.adapter.FragmentAdapter;
import com.aghamiri.apkbackup.data.PermissionUtil;
import com.aghamiri.apkbackup.fragment.FragmentApkBackup;
import com.aghamiri.apkbackup.fragment.FragmentApkRestore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;


public class ActivityApkBackup extends AppCompatActivity {

    //public static Toolbar toolbar;
    public static FragmentApkBackup frag_backup;
    private FragmentApkRestore frag_restore;

    private ViewPager viewPager;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apk_backup);
        if (!PermissionUtil.isGroupPermissionGranted(this,new String[] {
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        })) {
            showDialogPermission();

        }
    }

    private void onCreateProcess() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                frag_backup.refresh(true);
            }
        });

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                // close contextual action mode
                if (frag_backup.getActionMode() != null) {
                    frag_backup.getActionMode().finish();
                }
                if (frag_restore.getActionMode() != null) {
                    frag_restore.getActionMode().finish();
                }

                if (tab.getPosition() == 0) {
                    fab.show();
                } else {
                    frag_restore.refreshList();
                    fab.hide();
                }

                // search.onActionViewCollapsed();

                supportInvalidateOptionsMenu();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });

        Window window = this.getWindow();

        if (getAPIVerison() >= 5.0) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.setStatusBarColor(this.getResources().getColor(R.color.colorPrimaryDark));
            }
        }

    }


    private void setupViewPager(ViewPager viewPager) {
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());

        if (frag_backup == null) {
            frag_backup = new FragmentApkBackup();
        }
        if (frag_restore == null) {
            frag_restore = new FragmentApkRestore();
        }
        adapter.addFragment(frag_backup, getString(R.string.tab_title_backup));
        adapter.addFragment(frag_restore, getString(R.string.tab_title_restore));
        viewPager.setAdapter(adapter);
    }


    public static float getAPIVerison() {

        Float f = null;
        try {
            StringBuilder strBuild = new StringBuilder();
            strBuild.append(Build.VERSION.RELEASE.substring(0, 2));
            f = new Float(strBuild.toString());
        } catch (NumberFormatException e) {

        }
        return f.floatValue();
    }


    @Override
    protected void onResume() {
        if (!PermissionUtil.isAllPermissionGranted(this)) {
            // showDialogPermission();
        } else {
            onCreateProcess();
        }
        super.onResume();
    }

    private void showDialogPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.dialog_title_permission));
        builder.setMessage(getString(R.string.dialog_content_permission));
        builder.setCancelable(false);
        builder.setPositiveButton("تایید", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                PermissionUtil.goToPermissionSettingScreen(ActivityApkBackup.this);
            }
        });
        builder.setNegativeButton("انصراف", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        builder.show();
    }

}
