package flash.hippo.com.some;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class DairyActivity extends AppCompatActivity {


    private SQLiteDatabase db;
    private EditText titletext;
    private EditText dairytext;
    final int R_ADD=101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.input_layout);
        android.support.v7.app.ActionBar bar = getSupportActionBar();
        bar.setElevation(0);
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00BCD4")));
        bar.setDisplayHomeAsUpEnabled(true);
       Typeface typeface=Typeface.createFromAsset(this.getAssets(),"fonts/Roboto-Black.ttf");
        Typeface thintype=Typeface.createFromAsset(this.getAssets(),"fonts/Roboto-Regular.ttf");
         titletext=(EditText)findViewById(R.id.title);
         dairytext=(EditText)findViewById(R.id.dairytext);
        TextView heading =(TextView)findViewById(R.id.heading);
        titletext.setTypeface(thintype);
        dairytext.setTypeface(thintype);
        heading.setTypeface(typeface);
        titletext.setNextFocusDownId(R.id.dairytext);
        createDatabase();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.second_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        if(id==android.R.id.home){
            finish();

        }

        return super.onOptionsItemSelected(item);
    }



    protected void createDatabase(){
        db=openOrCreateDatabase("DairyDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS dairy(id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,titlecontent VARCHAR, dairycontent VARCHAR,datetime DATETIME);");
    }


    @Override
    public void onBackPressed() {
        Intent intent= new Intent();
        setResult(R_ADD,intent);
        finish();
            super.onBackPressed();

    }

    public void savecontent(View v) {
        String dairycontent = dairytext.getText().toString().trim().replace("'", "''");
        String titlecontent = titletext.getText().toString().trim().replace("'", "''");;
        String datetime = DateFormat.getDateTimeInstance().format(new Date());
        if (dairycontent.equals("")||datetime.equals("")||titlecontent.equals("")) {
            Toast.makeText(getApplicationContext(), "Please fill all fields", Toast.LENGTH_LONG).show();
            return;
        }
        String query = "INSERT INTO dairy (titlecontent,dairycontent,datetime) VALUES('"+titlecontent+"','"+dairycontent+"', '"+datetime+"');";
        db.execSQL(query);
        Toast.makeText(getApplicationContext(),"Saved Successfully", Toast.LENGTH_LONG).show();
        onBackPressed();

    }
}
