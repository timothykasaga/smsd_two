package com.victoria.timothykasaga.supermarketdirector;

/**
 * Created by Leontymo on 4/19/2016.
 */
        import android.app.FragmentManager;
        import android.app.FragmentTransaction;
        import android.content.Intent;
        import android.os.Bundle;
        import android.support.design.widget.NavigationView;
        import android.support.design.widget.NavigationView.OnNavigationItemSelectedListener;
        import android.support.v4.widget.DrawerLayout;
        import android.support.v7.app.ActionBar;
        import android.support.v7.app.ActionBarDrawerToggle;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.Toolbar;
        import android.view.Gravity;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.widget.Button;

public class Home
        extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private static Button b_admin;
    private static Button b_client;
    DrawerLayout drawer;
    Toolbar toolbar;

    private void initialise() {}

    public void onBackPressed()
    {
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.drawerlayout);
        if (drawerLayout.isDrawerOpen(drawerLayout))
        {
            drawerLayout.closeDrawer(drawerLayout);
            return;
        }
        super.onBackPressed();
    }

    protected void onCreate(Bundle paramBundle)
    {
        super.onCreate(paramBundle);
        setContentView(R.layout.home);
        initialise();
        this.toolbar = ((Toolbar)findViewById(R.id.toolbar_nu));
        setSupportActionBar(this.toolbar);
        this.drawer = ((DrawerLayout)findViewById(2131624111));
        paramBundle = new ActionBarDrawerToggle(Home.this, this.drawer, this.toolbar, 2131165269, 2131165268);
        this.drawer.setDrawerListener(paramBundle);
        paramBundle.syncState();
        ((NavigationView)findViewById(R.id.nav_view)).setNavigationItemSelectedListener(this);
        getFragmentManager().beginTransaction().replace(R.id.my_frag, new Initial()).commit();
        getSupportActionBar().setTitle("Supermarket Director");
        this.drawer.closeDrawer(Gravity.LEFT);
    }

    public boolean onCreateOptionsMenu(Menu paramMenu)
    {
        getMenuInflater().inflate(R.menu.home_drawer, paramMenu);
        return true;
    }

    public boolean onNavigationItemSelected(MenuItem paramMenuItem)
    {
        int i = paramMenuItem.getItemId();
        if (i == 2131624183)
        {
            getFragmentManager().beginTransaction().replace(2131624112, new Initial()).commit();
            getSupportActionBar().setTitle("Supermarket Director");
            this.drawer.closeDrawer(3);
        }
        for (;;)
        {
            ((DrawerLayout)findViewById(2131624111)).closeDrawer(8388611);
            return true;
            if (i == 2131624184)
            {
                startActivity(new Intent(this, Locate_sm.class));
            }
            else if (i == 2131624185)
            {
                getFragmentManager().beginTransaction().replace(2131624112, new Locate_pdt()).commit();
                getSupportActionBar().setTitle("Product location");
                this.drawer.closeDrawer(3);
            }
            else if (i == 2131624186)
            {
                paramMenuItem = new Price_comparison();
                FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
                localFragmentTransaction.replace(2131624112, paramMenuItem);
                localFragmentTransaction.commit();
                getSupportActionBar().setTitle("Price comparison");
                this.drawer.closeDrawer(3);
            }
            else if ((i == 2131624188) || (i != 2131624187)) {}
        }
    }

    public boolean onOptionsItemSelected(MenuItem paramMenuItem)
    {
        if (paramMenuItem.getItemId() == 2131624182) {
            return true;
        }
        return super.onOptionsItemSelected(paramMenuItem);
    }
}
