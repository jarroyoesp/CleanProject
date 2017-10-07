package es.jarroyo.cleanproject.base.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import es.jarroyo.cleanproject.R;


public abstract class BaseNavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final int MAIN_CONTENT_FRAME_ID = R.id.main_content_framelayout;

    // VIEW
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_navigation);

        setupNavigationDrawer();
        setupToolbar();
    }

    /**
     * Configure the navigationDrawer
     */
    private void setupNavigationDrawer(){
        // Set up the navigation drawer.
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        if (mNavigationView != null) {
            setupDrawerContent();
        }
    }

    /**
     * Events in NavigationView
     */
    protected void setupDrawerContent() {

        mNavigationView.setCheckedItem(R.id.item_menu_basenavigation_section1);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * SetupToolbar to show icon
     */
    private void setupToolbar(){
        // Set up the toolbar.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu_burguer_white);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("");
    }

    public void setTitleToolbar(String titleToolbar) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            TextView tvTitleToolbar = (TextView) toolbar.findViewById(R.id.toolbar_title);
            if(tvTitleToolbar != null) {
                tvTitleToolbar.setText(titleToolbar);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*if(showMenuRightToolbar()) {
            // Crea menu con las acciones por defecto en el resto de pantallas. El diseñador lo quita.
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_nav, menu);

            //MenuItem item = menu.findItem(R.id.item_menu_basenavigation_itt);
            //item.setVisible(showOptionMenuITT);
        }*/
       return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Open the navigation drawer when the home icon is selected from the toolbar.
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Add Fragment to the container of the main view (MAIN_CONTENT_FRAME_ID)
     * @param fragment
     */
    protected void addFragmentToMainContent(Fragment fragment, String tagFragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(MAIN_CONTENT_FRAME_ID, fragment, tagFragment).commit();
    }

    protected void replaceFragmentToMainContent(Fragment fragment, String tagFragment){
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(MAIN_CONTENT_FRAME_ID, fragment, tagFragment).commit();
    }


    @Override
    public void onBackPressed() {
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }
    }

    public void hideDrawerLayout(){
        if(mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public abstract boolean showMenuRightToolbar();

    public abstract void onClickNavigationMenuItem(int position);

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_menu_basenavigation_section1:
                onClickNavigationMenuItem(0);
                break;

            /*case R.id.item_menu_basenavigation_section2:
                onClickNavigationMenuItem(1);
                break;*/
        }
        return true;
    }

    public void showErrorSnackBar(CoordinatorLayout coordinatorLayout, String error){
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, error, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}
