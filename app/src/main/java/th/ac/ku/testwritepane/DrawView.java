package th.ac.ku.testwritepane;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class DrawView extends AppCompatActivity {
    DrawViewCode Myview;
    public int Default_Paint = Color.BLACK;
    public int Default_Size_Pain = 6;
    public int Default_colorRubber;
    View OptionPaint;
    AlertDialog dialog;
    int rProgress = 0;
    int gProgress = 0;
    int bProgress = 0;
    TextView r_show;
    TextView g_show;
    TextView b_show;
    TextView size_view_bar;
    int sizeNum = 0;
    int Rubber_size = 50;
    boolean rubberOn = false;
    ImageView imageView;
    View PickImageView;
    View SelectPenStyle;
    boolean style1;
    boolean style2;
    boolean style3;
    boolean style4;
    boolean style5;
    public ArrayList<Integer> RecentColor;
    public int Default_bg_color;
    int rand, rand2, rand3;
    Random random;
    View rootView;
    //final static String dirPath = Environment.getExternalStorageDirectory()+File.separator + "Screenshots" + File.separator;
    //final static String dirPath = "/storage/emulated/0/Screenshots/Screenshots";
    //final static String dirPath = "/storage/1719-0C1A/Screenshots";
    public String Filename;
    public File file;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Myview = new DrawViewCode(this);
        setContentView(Myview);
        Default_bg_color = Color.rgb(255, 255, 255);
        Myview.setBackgroundColor(Default_bg_color);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        random = new Random();
        RecentColor = new ArrayList<Integer>();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            Uri uri = data.getData();
            imageView = (ImageView) PickImageView.findViewById(R.id.BGimage);
            imageView.setImageURI(uri);
            BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
            Bitmap immutableBitmap = Bitmap.createBitmap(drawable.getBitmap());
            Bitmap bitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
            Bitmap resized;
            Drawable drawableResize;

            if (bitmap.getWidth() > 4000) {
                resized = Bitmap.createScaledBitmap(bitmap, (int) (3000 * 0.8), (int) (bitmap.getHeight() * 0.8), true);
                drawableResize = new BitmapDrawable(getResources(), resized);
                Myview.setBackground(drawableResize);
                imageView.setImageBitmap(resized);
                Log.i("ii", "debug Up ");

            } else {
                resized = Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * 0.8), (int) (bitmap.getHeight() * 0.8), true);
                drawableResize = new BitmapDrawable(getResources(), resized);
                Myview.setBackground(drawableResize);
                Log.i("ii", "debug below ");

                imageView.setImageBitmap(resized);
            }

            Log.i("ii", "debug scale" + bitmap.getWidth() + "   " + bitmap.getHeight());
            Log.i("ii", "debug scale" + Myview.getHeight() + "   " + Myview.getWidth());

        } catch (Exception e) {
            Toast.makeText(this, "Try again later", Toast.LENGTH_SHORT).show();
        }


    }

    public void setAllStyleFalse() {
        style1 = false;
        style2 = false;
        style3 = false;
        style4 = false;
        style5 = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.manu_painview, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.Menu1:

                Myview.Clear();
                Myview.invalidate();
                return true;

            case R.id.Menu2:

                Myview.SetColorBackground();
                return true;

            case R.id.Menu3:
                AlertDialog.Builder Option = new AlertDialog.Builder(DrawView.this);
                OptionPaint = getLayoutInflater().inflate(R.layout.dialog_set_color_size, null);
                final Button setRED = (Button) OptionPaint.findViewById(R.id.red);
                final Button setBlue = (Button) OptionPaint.findViewById(R.id.blue);
                final Button setGreen = (Button) OptionPaint.findViewById(R.id.green);
                Button CancleBtn = (Button) OptionPaint.findViewById(R.id.cancle);
                Button okBtn = (Button) OptionPaint.findViewById(R.id.ok);

                final SeekBar rSeekbar = (SeekBar) OptionPaint.findViewById(R.id.r_seekbar);
                final SeekBar gSeekbar = (SeekBar) OptionPaint.findViewById(R.id.g_seekbar);
                final SeekBar bSeekbar = (SeekBar) OptionPaint.findViewById(R.id.b_seekbar);
                final SeekBar sizeBar = (SeekBar) OptionPaint.findViewById(R.id.sizeSeekbar);

                r_show = (TextView) OptionPaint.findViewById(R.id.r_num);
                g_show = (TextView) OptionPaint.findViewById(R.id.g_num);
                b_show = (TextView) OptionPaint.findViewById(R.id.b_num);
                size_view_bar = (TextView) OptionPaint.findViewById(R.id.size_value);

                final Button exBtn = (Button) OptionPaint.findViewById(R.id.ex_color);
                //exBtn.setBackgroundColor(Color.rgb(0, 0, 0));
                OptionPaint.setBackgroundResource(R.drawable.images);
                Option.setView(OptionPaint);
                rSeekbar.setProgress(rProgress);
                gSeekbar.setProgress(gProgress);
                bSeekbar.setProgress(bProgress);
                sizeBar.setProgress(Default_Size_Pain);

                r_show.setText(rProgress + "");
                g_show.setText(gProgress + "");
                b_show.setText(bProgress + "");
                size_view_bar.setText(Default_Size_Pain + "");
                exBtn.setBackgroundColor(Color.rgb(rProgress, gProgress, bProgress));
                dialog = Option.create();
                dialog.show();


                rSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        Log.i("ii", "debug " + rSeekbar.getProgress());
                        rProgress = rSeekbar.getProgress();
                        Default_Paint = Color.rgb(rSeekbar.getProgress(), gProgress, bProgress);
                        Myview.brush.setColor(Color.rgb(rSeekbar.getProgress(), gProgress, bProgress));
                        exBtn.setBackgroundColor(Color.rgb(rSeekbar.getProgress(), gProgress, bProgress));
                        r_show.setText(rProgress + "");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                gSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        Log.i("ii", "debug " + gSeekbar.getProgress());
                        gProgress = gSeekbar.getProgress();
                        Default_Paint = Color.rgb(rProgress, gSeekbar.getProgress(), bProgress);
                        Myview.brush.setColor(Color.rgb(rProgress, gSeekbar.getProgress(), bProgress));
                        exBtn.setBackgroundColor(Color.rgb(rProgress, gSeekbar.getProgress(), bProgress));
                        g_show.setText(gProgress + "");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                bSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        Log.i("ii", "debug " + bSeekbar.getProgress());
                        bProgress = bSeekbar.getProgress();
                        Default_Paint = Color.rgb(rProgress, gProgress, bSeekbar.getProgress());
                        Myview.brush.setColor(Color.rgb(rProgress, gProgress, bSeekbar.getProgress()));
                        exBtn.setBackgroundColor(Color.rgb(rProgress, gProgress, bSeekbar.getProgress()));
                        b_show.setText(bProgress + "");
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                sizeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        sizeNum = sizeBar.getProgress();
                        Default_Size_Pain = sizeBar.getProgress();
                        Myview.brush.setStrokeWidth(sizeBar.getProgress());
                        size_view_bar.setText(sizeBar.getProgress() + "");
                        Log.i("ii", "debug " + sizeBar.getProgress());
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                setRED.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Default_Paint = Color.rgb(255, 0, 0);
                        Myview.brush.setColor(Color.rgb(255, 0, 0));
                        rSeekbar.setProgress(255);
                        gSeekbar.setProgress(0);
                        bSeekbar.setProgress(0);
                        //exBtn.setBackgroundColor(Color.rgb(255,0,0));
                    }
                });
                setBlue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Default_Paint = Color.rgb(0, 0, 255);
                        Myview.brush.setColor(Color.rgb(0, 0, 255));
                        bSeekbar.setProgress(255);
                        rSeekbar.setProgress(0);
                        gSeekbar.setProgress(0);
                        //exBtn.setBackgroundColor(Color.rgb(0,0,255));
                    }
                });

                setGreen.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Myview.brush.setColor(Color.rgb(0, 255, 0));
                        Default_Paint = Color.rgb(0, 255, 0);
                        bSeekbar.setProgress(0);
                        rSeekbar.setProgress(0);
                        gSeekbar.setProgress(255);
                        //exBtn.setBackgroundColor(Color.rgb(0,255,0));
                    }
                });
                CancleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                okBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Myview.invalidate();
                        dialog.cancel();
                    }
                });
                exBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        exBtn.setBackgroundColor(Color.rgb(rProgress, gProgress, bProgress));
                        Log.i("ii", "debug " + rProgress + " " + gProgress + " " + bProgress);
                        exBtn.invalidate();
                    }
                });
                return true;
            case R.id.Menu4:
                Myview.resetPaint();
                return true;
            case R.id.Menu5:
                finish();
                return true;
            case R.id.Menu6:
                Myview.undo();
                Log.i("ii", "debug " + "menu 6");
                return true;
            case R.id.rubber:
                Default_colorRubber = Default_bg_color;
                Myview.brush.setColor(Default_colorRubber);
//                Rubber_size = Default_Size_Pain;
//                Default_Size_Pain = 50;
                Myview.brush.setStrokeWidth(Rubber_size);
                Log.i("ii", "debug " + Rubber_size);
                rubberOn = true;
                return true;
            case R.id.Menu7:
                if (rubberOn) {
                    //Default_Size_Pain = Rubber_size;
                    Default_Paint = Color.rgb(rProgress, gProgress, bProgress);
                    Myview.brush.setColor(Color.rgb(rProgress, gProgress, bProgress));
                    Myview.brush.setStrokeWidth(Default_Size_Pain);
                    Log.i("ii", "debug " + Default_Size_Pain + " " + Rubber_size);
                    rubberOn = false;
                }
                return true;
            case R.id.Menu8:
                AlertDialog.Builder Pickimge = new AlertDialog.Builder(DrawView.this);
                PickImageView = getLayoutInflater().inflate(R.layout.pick_image, null);
                Button Pick = (Button) PickImageView.findViewById(R.id.selectimage);
                Button finnish = (Button) PickImageView.findViewById(R.id.now);
                Pick.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(intent, 0);
                    }
                });
                finnish.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });
                PickImageView.setBackgroundResource(R.drawable.bgpickimage);
                Pickimge.setView(PickImageView);
                dialog = Pickimge.create();
                dialog.show();
                return true;
            case R.id.PenStyle:
                AlertDialog.Builder setPenStyle = new AlertDialog.Builder(DrawView.this);
                SelectPenStyle = getLayoutInflater().inflate(R.layout.select_pen_style, null);
                final RadioGroup radioGroup = (RadioGroup) SelectPenStyle.findViewById(R.id.radioGroup);
                Button ok = (Button) SelectPenStyle.findViewById(R.id.ok);
                SelectPenStyle.setBackgroundResource(R.drawable.pen);

                ok.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        setAllStyleFalse();
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case R.id.penstyle1:
                                style1 = true;
                                break;
                            case R.id.penstyle2:
                                style2 = true;
                                break;
                            case R.id.penstyle3:
                                style3 = true;
                                break;
                            case R.id.penstyle4:
                                style4 = true;
                                break;
                            case R.id.penstyle5:
                                style5 = true;
                                break;
                        }
                        Log.i("ii", "debug " + style1 + style2 + style3 + style4 + style5);
                        dialog.cancel();
                    }
                });
                setPenStyle.setView(SelectPenStyle);
                dialog = setPenStyle.create();
                dialog.show();
                return true;
            case R.id.share:
                rand = random.nextInt(10);
                rand2 = random.nextInt(10);
                rand3 = random.nextInt(10);
                Filename = "Pic" + rand + rand2 + rand3 + ".png";
                rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                rootView.setDrawingCacheEnabled(true);
                rootView.layout(0, 0, rootView.getWidth(), rootView.getHeight());
                rootView.buildDrawingCache(true);
                Bitmap capture = Bitmap.createBitmap(rootView.getDrawingCache());
                store(capture, Filename);
                shareImage(file);

                return true;
            case R.id.Save:
                rand = random.nextInt(10);
                rand2 = random.nextInt(10);
                rand3 = random.nextInt(10);
                Filename = "Pic" + rand + rand2 + rand3 + ".png";
                AlertDialog.Builder setFileName = new AlertDialog.Builder(DrawView.this);
                final View viewFileName = getLayoutInflater().inflate(R.layout.save_image_name, null);
                EditText fileNameStr = viewFileName.findViewById(R.id.FileName);
                fileNameStr.setText(Filename);
                setFileName.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        int permission = ContextCompat.checkSelfPermission(DrawView.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        if (permission < 0) {
                            ActivityCompat.requestPermissions(DrawView.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1111);

                        }
                        System.out.println("permiss" + permission);
                        rootView = getWindow().getDecorView().findViewById(android.R.id.content);
                        rootView.setDrawingCacheEnabled(true);
                        rootView.layout(0, 0, rootView.getWidth(), rootView.getHeight());
                        rootView.buildDrawingCache(true);
                        Bitmap capture = Bitmap.createBitmap(rootView.getDrawingCache());
                        store(capture, Filename);

                        dialog.cancel();
                    }
                });
                setFileName.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                setFileName.setView(viewFileName);
                dialog = setFileName.create();
                dialog.show();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

//    public static Bitmap getScreenShot(View view) {
//        View screenView = view.getRootView();
//        screenView.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(screenView.getDrawingCache());
//        screenView.setDrawingCacheEnabled(false);
//        System.out.println("OK screenshot");
//
//        return bitmap;
//    }

    public void store(Bitmap bm, String FilenameRand) {

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Screenshots");
        System.out.println(dir.getPath() + "AAAAAAAAA");
        if (!dir.exists()) {
            System.out.println("IIIIIIIII");
            dir.mkdirs();
        }
        file = new File(dir.getPath(), FilenameRand);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(DrawView.this,"Create New File Failed",Toast.LENGTH_SHORT).show();
        }
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            Log.i("ii", "SaveTOexternalSuccess");
            Toast.makeText(DrawView.this,"Save "+FilenameRand+" Successful ",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(DrawView.this,"Save Failed Please Try again",Toast.LENGTH_SHORT).show();
        }
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(file);
        scanIntent.setData(uri);
        sendBroadcast(scanIntent);
    }

    public void shareImage(File file) {
        Uri uri = Uri.fromFile(file);
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");

        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        try {
            startActivity(Intent.createChooser(intent, "Share Screenshot"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No App Available", Toast.LENGTH_SHORT).show();
        }
    }


    public class DrawViewCode extends View {
        private Paint brush = new Paint();
        ArrayList<Path> listPath = new ArrayList<>();
        ArrayList<Paint> listPaint = new ArrayList<>();
        private Path path = new Path();

        public DrawViewCode(Context context) {
            super(context);
            brush.setAntiAlias(true);    //เส้นมีความโค้งได้
            brush.setStrokeWidth(Default_Size_Pain);    //ความหนาของเส้น
            brush.setColor(Default_Paint);  //สี
            brush.setStyle(Paint.Style.STROKE);  //style
            brush.setStrokeJoin(Paint.Join.ROUND);  //เส้นขอบของเส้น โค้ง
        }

        public void SetColorBackground() {
            Default_bg_color = Default_Paint;
            Myview.setBackgroundColor(Default_Paint);
            Myview.invalidate();


        }

        public void Clear() {
            for (Path path : listPath) {
                path.reset();
            }
            Myview.setBackgroundColor(Color.rgb(255, 255, 255));
            invalidate();

        }

        public void undo() {
            try {
                listPath.remove(listPath.size() - 1);
                listPaint.remove(listPaint.size() - 1);
                invalidate();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void resetPaint() {
            //for (Paint paint : listPaint) {
            setAllStyleFalse();
            Default_Paint = Color.BLACK;
            brush.setAntiAlias(true);
            brush.setStrokeWidth(6);
            brush.setColor(Default_Paint);
            brush.setStyle(Paint.Style.STROKE);
            brush.setStrokeJoin(Paint.Join.ROUND);
            //}
        }

        public boolean CheckStylePen() {
            if (style1 == false && style2 == false && style3 == false && style4 == false && style5 == false)
                return true;

            else return false;

        }

        @Override
        public boolean onTouchEvent(MotionEvent pos) {
            float x = pos.getX();
            float y = pos.getY();

            switch (pos.getAction()) {
                case MotionEvent.ACTION_DOWN:  //นิ้วจิ่้มลงไป
                    listPath.add(path);
                    listPaint.add(brush);
                    path.moveTo(x, y);
                    //path.addCircle(x, y, Default_Size_Pain, Path.Direction.CW);

                    return true;

                case MotionEvent.ACTION_MOVE:  //ลากนิ้ว
                    if (CheckStylePen()) {

                        path.lineTo(x, y);
                    } else if (style1 == true) {

                        path.lineTo(x, y);
                    } else if (style2 == true) {

                        path.lineTo(x, y);
                        path.addCircle(x, y, Default_Size_Pain, Path.Direction.CW);
                        path.lineTo(x, y);
                    } else if (style3 == true) {

                        path.addCircle(x, y, Default_Size_Pain, Path.Direction.CW);
                    } else if (style4 == true) {

                        path.lineTo(x, y);
                        path.addCircle(x, y, Default_Size_Pain + 20, Path.Direction.CW);
                    } else if (style5 == true) {

                        path.lineTo(x, y);
                        path.moveTo(x + 50, y + 100);
                    }

                    break;
                case MotionEvent.ACTION_UP:   //ยกนิ้วขึ้น

                    path = new Path();
                    brush = new Paint();
                    brush.setAntiAlias(true);
                    brush.setStrokeWidth(Default_Size_Pain);
                    brush.setColor(Default_Paint);
                    brush.setStyle(Paint.Style.STROKE);
                    brush.setStrokeJoin(Paint.Join.ROUND);
                    break;

                default:
                    return false;
            }
            invalidate();


            return true;
        }

        @Override
        protected void onDraw(Canvas drawer) {

            for (int i = 0; i < listPaint.size(); i++) {

                drawer.drawPath(listPath.get(i), listPaint.get(i));
            }
            drawer.save();


        }

    }
}
