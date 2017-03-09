package a098.ramzan.kamran.paleodietdiary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class BackupRestore extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MenuItem itemExport;
    private MenuItem itemImport;
    private MenuItem itemShare;
    private Modal modal;
    private static final int READ_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_restore);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        modal = new Modal(this);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    itemExport.setVisible(true);
                    itemImport.setVisible(false);
                    itemShare.setVisible(false);
                } else if (tab.getPosition() == 1) {
                    itemExport.setVisible(false);
                    itemImport.setVisible(true);
                    itemShare.setVisible(true);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new BackupFragment(), "Backup");
        adapter.addFragment(new RestoreFragment(), "Restore");
        viewPager.setAdapter(adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_file_export);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_file_import);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_backup_restore, menu);
        itemExport = menu.findItem(R.id.action_export);
        itemImport = menu.findItem(R.id.action_import);
        itemImport.setVisible(false);
        itemShare = menu.findItem(R.id.action_share);
        itemShare.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                onBackPressed();
                return true;
            }
            case R.id.action_export: {
                modal.exportDatabaseToCSV();
                break;
            }
            case R.id.action_import: {
                showFileChooser();
                break;
            }
            case R.id.action_share: {
                modal.shareFile();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFileChooser() {
        new MaterialFilePicker()
                .withActivity(this)
                .withRequestCode(1)
                .withFilter(Pattern.compile(".*\\.csv$")) // Filtering files and directories by file name using regexp
                .withFilterDirectories(true) // Set directories filterable (false by default)
                .withHiddenFiles(true) // Show hidden files and folders
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String filePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
            // Do anything with file
            Log.i("TAG", "onActivityResult: " + filePath);
            new RestoreBackup().execute(filePath);
        }
    }

    private class RestoreBackup extends AsyncTask<String, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(BackupRestore.this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Restoring data...");
            progressDialog.setCancelable(false);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {
            String filePath = strings[0];
            List<String[]> backupList = new ArrayList<>();
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File(filePath))));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] RowData = line.split(",");

                    backupList.add(RowData);
                    // do something with "data" and "value"
                }

                Modal modal = new Modal(BackupRestore.this);
                for (int i = 1; i < backupList.size(); i++) {
                    EventInfo eventInfo = new EventInfo();

                    eventInfo.setEventName(backupList.get(i)[1]);
                    eventInfo.setEventIcon(Integer.parseInt(backupList.get(i)[2]));
                    eventInfo.setEventDate(backupList.get(i)[3]);
                    eventInfo.setEventTime(backupList.get(i)[4]);
                    eventInfo.setEventDescription(backupList.get(i)[5]);
                    eventInfo.setEventClass(backupList.get(i)[6]);
                    modal.addEvent(eventInfo);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    // handle exception
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.cancel();
        }
    }
}
