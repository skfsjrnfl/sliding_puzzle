package edu.skku.map.pa_2016313549;

import androidx.appcompat.app.AppCompatActivity;

import android.app.LauncherActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    public float dpToPx(Context context, float dp) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,dm);
    }


    ArrayList<puzzle> puzzles3 =null;
    ArrayList<puzzle> puzzles4 =null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ImageView Complete=(ImageView)findViewById(R.id.imageView) ;
        Complete.setImageResource(R.drawable.likemon); //show perfect image

        Bitmap image = BitmapFactory.decodeResource(getResources(),R.drawable.likemon);
        int width=image.getWidth();
        int Height=image.getHeight();
        Bitmap whiteimage=BitmapFactory.decodeResource(getResources(),R.drawable.white);
        Bitmap three[]=new Bitmap[9];
        for (int i =0;i<8;i++){
            three[i]= Bitmap.createBitmap(image,(width/3)*(i%3),(Height/3)*(i/3),width/3,Height/3);
        }
        three[8]=whiteimage;

        puzzles3 = new ArrayList<puzzle>();
        for(int i =0;i<9;i++){
            puzzle puz = new puzzle(i, three[i]);
            puzzles3.add(puz);
        } //make 3 by 3 puzzle pieces

        Bitmap four[] =new Bitmap[16];
        for (int i =0;i<15;i++){
            four[i]=Bitmap.createBitmap(image,(width/4)*(i%4),(Height/4)*(i/4),width/4,Height/4);
        }
        four[15]=whiteimage;

        puzzles4 = new ArrayList<puzzle>();
        for(int i =0;i<16;i++){
            puzzle puz = new puzzle(i,four[i]);
            puzzles4.add(puz);
        }


        ArrayList<puzzle>puzzles3_save = (ArrayList<puzzle>) puzzles3.clone();
        ArrayList<puzzle>puzzles4_save = (ArrayList<puzzle>) puzzles4.clone();

        ImageAdapter adapter3 =new ImageAdapter(this,puzzles3_save);
        ImageAdapter adapter4 =new ImageAdapter(this,puzzles4_save);


        GridView gridView=(GridView)findViewById(R.id.gridview);
        gridView.setAdapter(adapter3);
        int xlist[]={1,-1,0,0};
        int ylist[]={0,0,1,-1};

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int col=gridView.getNumColumns();
                ArrayList<puzzle> puzzleonboard=null;
                int move=0;
                int right=0;

                if (col==3)
                    puzzleonboard=puzzles3;
                else
                    puzzleonboard=puzzles4;

                int nowx=position/col;
                int nowy=position%col;

                for (int i=0;i<4;i++){
                    int nextx=nowx+xlist[i];
                    int nexty=nowy+ylist[i];

                    if(((0<=nextx)&&(nextx<col)&&(0<=nexty)&&(nexty<col)&&(puzzleonboard.get(nextx*col+nexty).img==whiteimage))){
                        puzzle tmp = puzzleonboard.get(position);
                        puzzleonboard.set(position,puzzleonboard.get(nextx*col+nexty));
                        puzzleonboard.set(nextx*col+nexty,tmp);
                        gridView.setAdapter(new ImageAdapter(parent.getContext(),puzzleonboard));
                        move=1;
                        break;
                    }
                }


                if (move==1) {
                    for (int i = 0; i < col * col; i++) {//complete?
                        puzzle puz = (puzzle) gridView.getAdapter().getItem(i);
                        if ((puz.rightposition != i)) {
                            right = 0;
                            break;
                        }
                        right = 1;
                    }
                    if (right == 1)
                        Toast.makeText(MainActivity.this, "FINISH!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button button_4 =(Button)findViewById(R.id.button_four);
        button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.setNumColumns(4);
                puzzles4=(ArrayList<puzzle>)puzzles4_save.clone();
                gridView.setAdapter(adapter4);
            }
        });

        Button button_3 =(Button)findViewById(R.id.button_three);
        button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridView.setNumColumns(3);
                puzzles3=(ArrayList<puzzle>)puzzles3_save.clone();
                gridView.setAdapter(adapter3);
            }
        });

        Button button_suffle =(Button)findViewById(R.id.button_suffle);
        button_suffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int col=gridView.getNumColumns();
                ArrayList<puzzle> puzzleonboard=null;
                if (col==3)
                    puzzleonboard=puzzles3;
                else
                    puzzleonboard=puzzles4;
                Random random = new Random();
                int nowx=0;
                int nowy=0;
                for (int i = 0; i<col*col;i++){
                    puzzle white=(puzzle)gridView.getAdapter().getItem(i);
                    if (white.img==whiteimage) {
                        nowx = i / col;
                        nowy = i % col;
                        break;
                    }
                }

                for (int i =0; i<200;i++){
                    int randomNum = random.nextInt(4);
                    int nextx=nowx+xlist[randomNum];
                    int nexty=nowy+ylist[randomNum];

                    if(((0<=nextx)&&(nextx<col)&&(0<=nexty)&&(nexty<col))){
                        puzzle tmp = puzzleonboard.get(nowx*col+nowy);
                        puzzleonboard.set(nowx*col+nowy,puzzleonboard.get(nextx*col+nexty));
                        puzzleonboard.set(nextx*col+nexty,tmp);
                        nowx=nextx;
                        nowy=nexty;
                    }
                }
                gridView.setAdapter(new ImageAdapter(MainActivity.this,puzzleonboard));
            }
        });


    }


    public class ImageAdapter extends BaseAdapter {
        Context context;
        ArrayList<puzzle> arrayList = null;
        LayoutInflater mLayoutInflater=null;

        public ImageAdapter(Context c, ArrayList<puzzle> data){
            context=c;
            arrayList= data;
        }

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public puzzle getItem(int position) {
            return arrayList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = new ImageView(context);
            int dp;
            if (arrayList.size()<10) {
                dp=(int)dpToPx(MainActivity.this,120);

            }
            else {
                dp = (int) dpToPx(MainActivity.this, 90);
            }
            imageView.setLayoutParams(new GridView.LayoutParams(dp, dp));
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageView.setPadding(0,0,0,0);
            imageView.setImageBitmap(arrayList.get(position).img);

            return imageView;
        }

    }

    public class puzzle{
        Bitmap img;
        int rightposition;

        public puzzle(int rightposition, Bitmap img){
            this.rightposition=rightposition;
            this.img=img;
        }
    }

}