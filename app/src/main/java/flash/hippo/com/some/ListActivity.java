package flash.hippo.com.some;

import android.animation.LayoutTransition;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ListActivity extends AppCompatActivity {
    ListView list;
    CustomAdapter adapter;
    SQLiteDatabase db;
    SearchView searchView;
    final int A_Add=100,R_ADD=101;
    public  ListActivity CustomListView = null;
    public ArrayList<ListModel> CustomListViewValuesArr;
    public Menu smallmenu;
    public FloatingActionButton fabcal;
    private int pYear;
    private int pMonth;
    private int pDay;
    static final int DATE_DIALOG_ID = 0;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;



    private DatePickerDialog.OnDateSetListener pDateSetListener =
            new DatePickerDialog.OnDateSetListener() {

                public void onDateSet(DatePicker view, int year,
                                      int monthOfYear, int dayOfMonth) {
                    pYear = year;
                    pMonth = monthOfYear+1;
                    pDay = dayOfMonth;
                    String key;
                    if(pDay<10) {
                       key= "0"+pDay + "-" + getMonthName(pMonth) + "-" + year;
                    }
                    else {
                        key = pDay + "-" + getMonthName(pMonth) + "-" + year;

                    }
                    String query = "SELECT * FROM dairy WHERE datetime LIKE '%"+key+"%';";

                    setListData(query);

                }
            };


    private String getMonthName(int monthNum){
        switch (monthNum){
            case 1:
                return "Jan";
            case 2:
                return "Feb";
            case 3:
                return "Mar";
            case 4:
                return "Apr";
            case 5:
                return "May";
            case 6:
                return "Jun";
            case 7:
                return "Jul";
            case 8:
                return "Aug";
            case 9:
                return "Sep";
            case 10:
                return "Oct";
            case 11:
                return "Nov";
            case 12:
                return "Dec";
            default:
                return " ";
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.second_activity_main);
        CustomListView=this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setElevation(0);
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00BCD4")));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i1=new Intent(ListActivity.this,DairyActivity.class);
                startActivityForResult(i1,A_Add);

            }
        });

        fabcal = (FloatingActionButton) findViewById(R.id.fabcal);
        fabcal.setAlpha(0.75f);
        fab.setAlpha(.75f);

        fabcal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDialog(DATE_DIALOG_ID);

            }
        });


        String query = "SELECT * FROM dairy ORDER BY datetime DESC LIMIT 10;";

        setListData(query);

        LayoutInflater inflator = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.left_menu    , null);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled (false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setCustomView(v);



        mPlanetTitles = getResources().getStringArray(R.array.countries);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mPlanetTitles));
        // Set the list's click listener
       // mDrawerList.setOnItemClickListener(new DrawerItemClickListener());



    }



    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        smallmenu=menu;
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
          //  searchView.setLayoutTransition(new LayoutTransition());

            searchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    String query = "SELECT * FROM dairy ORDER BY datetime DESC LIMIT 10;";
                    setListData(query);
                    recreate();
                    InputMethodManager inputManager = (InputMethodManager)
                            getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);                    //some operation
                    return  true;
                }
            });
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //some operation
                }
            });
            EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchPlate.setHint("Search");
            View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            searchPlateView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }
                @Override
                public boolean onQueryTextChange(String newText) {
                    String querydb = "SELECT * FROM dairy WHERE dairycontent LIKE '%"+newText+"%';";
                    setListData(querydb);
                    return false;
                }
            });
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:

                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                return new DatePickerDialog(this,
                        pDateSetListener, year, month, day);

        }
        return null;
    }




    protected void createDatabase(){
        db=openOrCreateDatabase("DairyDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS dairy(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,titlecontent VARCHAR, dairycontent VARCHAR,datetime DATETIME);");
    }


    public void setListData(String query)
    {
        createDatabase();
        CustomListViewValuesArr = new ArrayList<ListModel>();

        Cursor cur = db.rawQuery(query, null);

        if (cur != null) {
            if (cur.moveToFirst()) {
                do { final ListModel sched = new ListModel();
                    sched.setTitle(cur.getString(cur.getColumnIndex("titlecontent")));
                    sched.setContent(cur.getString(cur.getColumnIndex("dairycontent")));
                    sched.setDatetime(cur.getString(cur.getColumnIndex("datetime")));
                    CustomListViewValuesArr.add( sched );
                } while (cur.moveToNext());
            }
        }
        else {

        }
        Resources res =getResources();
        list= ( ListView )findViewById( R.id.list );
        adapter=new CustomAdapter( CustomListView, CustomListViewValuesArr,res );
        list.setAdapter( adapter );
        list=(ListView)findViewById(R.id.list);
    }

    public void onItemClick(int mPosition)
    {
        ListModel tempValues = ( ListModel ) CustomListViewValuesArr.get(mPosition);
        Toast.makeText(CustomListView, ""+tempValues.getTitle()
                        +"Url:"+tempValues.getContent(),
        Toast.LENGTH_LONG)
        .show();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("Came to optionsItem","");
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Log.d("clicked","");
            return true;
        }
        if(id == R.id.btn_menu){

            mDrawerLayout.openDrawer(GravityCompat.START,true);
              Log.d("clicked","");

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==A_Add&& resultCode==R_ADD){
            recreate();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
