package th.ac.ku.testwritepane;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    GestureDetector gd;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        constraintLayout = (ConstraintLayout) findViewById(R.id.Main_BG);
        constraintLayout.setBackgroundResource(R.drawable.bg2);

        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this,DrawView.class);
//                startActivity(intent);
                Log.i("ii", "debug " + "OK" + view.getY());
            }
        });

        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.i("ii", "debug " + "OK Ontouch" + motionEvent.getY());
                gd.onTouchEvent(motionEvent);
                return false;
            }
        });
        gd = new GestureDetector(MainActivity.this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent motionEvent) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent motionEvent) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                //for (int i = 0 ; i<5 ; i++){

                constraintLayout.setBackgroundResource(R.drawable.download);
                //}
                Log.i("ii", "debug " + "OK Scroll" + motionEvent.getPointerCount());
                return false;
            }


            @Override
            public void onLongPress(MotionEvent motionEvent) {
                Log.i("ii", "debug " + "OK" + motionEvent.getY());
                Intent intent = new Intent(MainActivity.this, DrawView.class);

                startActivity(intent);
            }

            @Override
            public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
                return false;
            }
        });

    }


}
