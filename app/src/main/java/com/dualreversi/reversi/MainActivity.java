// --------------------------------------
// reversi
//
// issue
// ・win or lose or draw
// --------------------------------------
package com.dualreversi.reversi;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.graphics.Color;
import android.graphics.Insets;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Locale;

// 別クラスのallocation
//import static com.dualreversi.reversi.Allocation.*;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // import class
//    AllocationDynamic ClassAllocationDynamic = new AllocationDynamic();

    public static final int IdImageBG     = 1;
    public static final int IdButtonBW    = 2;
    public static final int IdButtonReset = 3;
    public static final int IdButtonUndo  = 4;
    public static final int IdTextB = 900;
    public static final int IdTextW = 901;
    public static final int IdText1st = 902;
    public static final int IdText2nd = 903;
    public static final int IdTextWinLose = 904;

    // put black or white
    public boolean black_true = true;

    // boolean
    public boolean Bool1st;
    public boolean Bool2nd;
    public boolean BoolWinORLose = false; // decision to win or lose
    public boolean BoolPlayer    = true;  // 1st or 2nd player

    // color
    public int now_bw;
    public int rev_bw;

    // number black or white
    public int nBlack;
    public int nWhite;


    // status on board
    public int[][] status          = new int[8][8];
    public int[][][] statusHistory = new int[64][8][8];
    public final int statusLength = 8;

    // iteration for decision
    public int IteStatusHistory = 0;

    // direction of reversi
    public boolean[][][] ReversiDirection = new boolean[8][8][8];


    // --------------------------------------
    // initial setup
    // --------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --------------------------------------
        // make Constraint layout
        // --------------------------------------

        // ConstraintLayout
        ConstraintLayout constraintSet = new ConstraintLayout(this);

        // set layout Width and Height as maximum
        constraintSet.setLayoutParams(new ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT));


        // --------------------------------------
        // some setup
        // --------------------------------------
        // set back ground board
        SetBackGround(constraintSet);

        // set button to change the color of the next piece to be placed.
        SetButtonSwitchBW(constraintSet);
        SetTextSwitchBW(constraintSet);

        // set reset button
        SetButtonReset(constraintSet);

        // set undo button
        SetButtonUndo(constraintSet);

        // set button on board
        SetButtonANDImgOnBoard(constraintSet);

        // set text player
        SetTextPlayer(constraintSet);

        // set text win or lose
        SetTextWinLose(constraintSet);

        // set initial condition
        SetInitialStatus();

        // check status
        CalStatus();

        // set initial condition for status history
        SetInitialStatusHistory();

        // set view
        SetViewStatus();
    }

    // --------------------------------------
    // set back ground (ConstraintLayout)
    // --------------------------------------
//    @RequiresApi(api = Build.VERSION_CODES.R)
    public void SetBackGround(ConstraintLayout constraintSet){
        // new image view
        ImageView img_reverse_board = new ImageView(getApplicationContext());
        // set image resource
        img_reverse_board.setImageResource(R.drawable.reverse_board);
        // set id
        img_reverse_board.setId(IdImageBG);

        // set on constrain layout
        constraintSet.addView(img_reverse_board);

        // get dp
        float dp = getResources().getDisplayMetrics().density;

        // SW button
        int widthBG       = (int) (360 * dp);
        int heightBG      = (int) (360 * dp);
        int bottomBG = (int) (100 * dp);
        int leftBG   = (int) (0 * dp);

        // --------------------------------------
        // adjust button
        // --------------------------------------
        SetConstrainSetBackground(constraintSet, IdImageBG,
                heightBG,
                widthBG,
                bottomBG,
                leftBG);
//        SetConstrainSetBackground(constraintSet, IdImageBG,
//                ClassAllocationDynamic.heightBG,
//                ClassAllocationDynamic.widthBG,
//                ClassAllocationDynamic.bottomBG,
//                ClassAllocationDynamic.leftBG);


    }

    // --------------------------------------
    // set button switch BW (ConstraintLayout)
    // --------------------------------------
    public void SetButtonSwitchBW(ConstraintLayout constraintSet){
        // get dp
        float dp = getResources().getDisplayMetrics().density;
        // SW button
        int SWButton_width       = (int) (100 * dp);
        int SWButton_height      = (int) (50 * dp);
        int SWButton_from_bottom = (int) (-60 * dp);
        int SWButton_from_left   = (int) (80 * dp);

        // new button
        Button fst_btn = new Button(this);
        // set id
        fst_btn.setId(Integer.parseInt("2"));
        // set text
        fst_btn.setText(String.format(Locale.US, "ch b or w", "1"));
        // set click motion
        fst_btn.setOnClickListener(this);
        // set on constrain layout
        constraintSet.addView(fst_btn);

        // --------------------------------------
        // adjust button
        // --------------------------------------
        SetConstrainSetBackground(constraintSet, 2, SWButton_height,
                SWButton_width, SWButton_from_bottom, SWButton_from_left);
    }

    // --------------------------------------
    // set text switch BW (ConstraintLayout)
    // --------------------------------------
    public void SetTextSwitchBW(ConstraintLayout constraintSet){
        // get dp
        float dp = getResources().getDisplayMetrics().density;
        // SW button
        int SWButton_width       = (int) (100 * dp);
        int SWButton_height      = (int) (50 * dp);
        int SWButton_from_bottom = (int) (50 * dp);
        int SWButton_from_left   = (int) (200 * dp);
        float text_size = 24.0f;

        // --------------------------------------
        // text, black
        // --------------------------------------
        // TextView
        TextView tvB = new TextView(this);
        String strB = "black";
        tvB.setText(strB);
        // set id
        tvB.setId(Integer.parseInt("900"));
        // set size
        tvB.setTextSize(text_size);
        // set color
        tvB.setTextColor(Color.rgb(0, 0, 0));
        // set on constrain layout
        constraintSet.addView(tvB);

        SetConstrainSetBackground(constraintSet, 900, SWButton_height,
                SWButton_width, SWButton_from_bottom, SWButton_from_left);

        // set invisible
//        tvB.setVisibility(View.GONE);

        // --------------------------------------
        // text, white
        // --------------------------------------
        // TextView
        TextView tvW = new TextView(this);
        String strW = "white";
        tvW.setText(strW);
        // set id
        tvW.setId(Integer.parseInt("901"));
        // set size
        tvW.setTextSize(text_size);
        // set color
        tvB.setTextColor(Color.rgb(0, 0, 0));
        // set on constrain layout
        constraintSet.addView(tvW);

        // --------------------------------------
        // adjust button
        // --------------------------------------
        SetConstrainSetBackground(constraintSet, 901, SWButton_height,
                SWButton_width, SWButton_from_bottom, SWButton_from_left);

        // set invisible
        tvW.setVisibility(View.GONE);
    }

    // --------------------------------------
    // set text Player (ConstraintLayout)
    // --------------------------------------
    public void SetTextPlayer(ConstraintLayout constraintSet){
        // get dp
        float dp = getResources().getDisplayMetrics().density;
        // SW button
        int PlayerText_width       = (int) (400 * dp);
        int PlayerText_height      = (int) (60 * dp);
        int PlayerText_from_bottom = (int) (450 * dp);
        int PlayerText_from_left   = (int) (25 * dp);
        float text_size = 36.0f;

        // --------------------------------------
        // text, 1st player
        // --------------------------------------
        // TextView
        TextView tv1 = new TextView(this);
        String str1 = "1st player ( black )";
        tv1.setText(str1);
        // set id
        tv1.setId(Integer.parseInt("902"));
        // set size
        tv1.setTextSize(text_size);
        // set color
        tv1.setTextColor(Color.rgb(0, 0, 0));
        // set on constrain layout
        constraintSet.addView(tv1);

        SetConstrainSetBackground(constraintSet, 902, PlayerText_height,
                PlayerText_width, PlayerText_from_bottom, PlayerText_from_left);

        // --------------------------------------
        // text, 2nd player
        // --------------------------------------
        // TextView
        TextView tv2 = new TextView(this);
        String str2 = "2nd player ( white )";
        tv2.setText(str2);
        // set id
        tv2.setId(Integer.parseInt("903"));
        // set size
        tv2.setTextSize(text_size);
        // set color
        tv2.setTextColor(Color.rgb(0, 0, 0));
        // set on constrain layout
        constraintSet.addView(tv2);

        SetConstrainSetBackground(constraintSet, 903, PlayerText_height,
                PlayerText_width, PlayerText_from_bottom, PlayerText_from_left);

        // set invisible
        tv2.setVisibility(View.GONE);
    }

    // --------------------------------------
    // set text Win lose (ConstraintLayout)
    // --------------------------------------
    public void SetTextWinLose(ConstraintLayout constraintSet){
        // get dp
        float dp = getResources().getDisplayMetrics().density;
        // SW button
        int SWButton_width       = (int) (300 * dp);
        int SWButton_height      = (int) (130 * dp);
        int SWButton_from_bottom = (int) (220 * dp);
        int SWButton_from_left   = (int) (30 * dp);
        float text_size = 28.0f;

        // TextView
        TextView tv = new TextView(this);
        // set id
        tv.setId(IdTextWinLose);
        // set size
        tv.setTextSize(text_size);
        // set color
        tv.setTextColor(Color.rgb(0, 0, 0));
        // background
        tv.setBackgroundColor(Color.rgb(255, 255, 255));
        // set on constrain layout
        constraintSet.addView(tv);

        SetConstrainSetBackground(constraintSet, IdTextWinLose, SWButton_height,
                SWButton_width, SWButton_from_bottom, SWButton_from_left);

        tv.setVisibility(View.INVISIBLE);
    }

    // --------------------------------------
    // set button switch BW (ConstraintLayout)
    // --------------------------------------
    public void SetButtonReset(ConstraintLayout constraintSet){
        // get dp
        float dp = getResources().getDisplayMetrics().density;
        // SW button
        int ResetButton_width       = (int) (100 * dp);
        int ResetButton_height      = (int) (50 * dp);
        int ResetButton_from_bottom = (int) (10 * dp);
        int ResetButton_from_left   = (int) (240 * dp);

        // new button
        Button btn = new Button(this);
        // set id
        btn.setId(Integer.parseInt("3"));
        // set text
        btn.setText(String.format(Locale.US, "Reset", "1"));
        // set click motion
        btn.setOnClickListener(this);
        // set on constrain layout
        constraintSet.addView(btn);

        // --------------------------------------
        // adjust button
        // --------------------------------------
        SetConstrainSetBackground(constraintSet, 3, ResetButton_height,
                ResetButton_width, ResetButton_from_bottom, ResetButton_from_left);
    }

    // --------------------------------------
    // set button switch Undo (ConstraintLayout)
    // --------------------------------------
    public void SetButtonUndo(ConstraintLayout constraintSet){
        // get dp
        float dp = getResources().getDisplayMetrics().density;
        // SW button
        int ResetButton_width       = (int) (100 * dp);
        int ResetButton_height      = (int) (50 * dp);
        int ResetButton_from_bottom = (int) (10 * dp);
        int ResetButton_from_left   = (int) (20 * dp);

        // new button
        Button btn = new Button(this);
        // set id
        btn.setId(IdButtonUndo);
        // set text
        btn.setText("Undo");
        // set click motion
        btn.setOnClickListener(this);
        // set on constrain layout
        constraintSet.addView(btn);

        // --------------------------------------
        // adjust button
        // --------------------------------------
        SetConstrainSetBackground(constraintSet, IdButtonUndo, ResetButton_height,
                ResetButton_width, ResetButton_from_bottom, ResetButton_from_left);
    }

    // --------------------------------------
    // set button on board (ConstraintLayout)
    // --------------------------------------
    public void SetButtonANDImgOnBoard(ConstraintLayout constraintSet){
        // get dp
        float dp = getResources().getDisplayMetrics().density;
        // cell
        int cell_from_bottom     = (int) (397 * dp); // 11
        int cell_from_left       = (int) (17 * dp);  // 11
        int cell_margins         = (int) (40.1 * dp);  // between cells
        int cell_size            = (int) (45 * dp);  // square

        for (int i = 0; i < statusLength; i++) {
            for (int j = 0; j < statusLength; j++) {
                for (int k = 0; k < 3; k++) {

                    // ite
                    int ii = i + 1;
                    int jj = j + 1;

                    // cell id
                    int cell_id          = ii*10 + jj + 100*k;
                    int temp_from_bottom = cell_from_bottom - cell_margins * (ii-1);
                    int temp_from_left   = cell_from_left   + cell_margins * (jj-1);

                    if (k==0) {
                        // new image button
                        ImageButton imageBtn = new ImageButton(this);
                        // set image resource
                        imageBtn.setImageResource(R.drawable.button_on_board_small);
                        // set click motion
                        imageBtn.setOnClickListener(this);
                        // set id
                        imageBtn.setId(cell_id);
                        // set relative size
                        imageBtn.setScaleType(ImageView.ScaleType.FIT_START);
                        // set background as transparency
                        imageBtn.setBackground(null);
                        // set on constrain layout
                        constraintSet.addView(imageBtn);
                    }else{
                        // set img view
                        ImageView imgV = new ImageView(getApplicationContext());

                        // set image resource
                        if (k==1) {
                            imgV.setImageResource(R.drawable.black_small);
                        }else if(k==2){
                            imgV.setImageResource(R.drawable.white_small);
                        }

                        // set id
                        imgV.setId(cell_id);
                        // set relative size
                        imgV.setScaleType(ImageView.ScaleType.FIT_START);
                        // set background as transparency
                        imgV.setBackground(null);
                        // set on constrain layout
                        constraintSet.addView(imgV);
                        // set invisible
                        imgV.setVisibility(View.GONE);
                    }


                    // --------------------------------------
                    // adjust button
                    // --------------------------------------
                    SetConstrainSetBackground(constraintSet, cell_id, cell_size, cell_size,
                            temp_from_bottom, temp_from_left);
                }
            }
        }
    }

    // --------------------------------------
    // set constraint set
    // --------------------------------------
    public void SetConstrainSetBackground(ConstraintLayout constraintSet,
                                          int id, int height, int width, int FromBottom,
                                          int FromLeft){
        // clone set
        ConstraintSet tempCS = new ConstraintSet();
        tempCS.clone(constraintSet);

        // adjust layout height
        tempCS.constrainHeight(id, height);

        // adjust layout width
        tempCS.constrainWidth(id, width);

        if (id ==1){
            // adjust layout
            tempCS.connect(
                    id,               // SWButton
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,               // back ground
                    ConstraintSet.TOP,
                    0);

            // adjust layout
            tempCS.connect(
                    id,               // SWButton
                    ConstraintSet.RIGHT,
                    ConstraintSet.PARENT_ID,               // back ground
                    ConstraintSet.RIGHT,
                    0);

            // adjust layout
            tempCS.connect(
                    id,               // SWButton
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,               // back ground
                    ConstraintSet.BOTTOM,
                    0);

            // adjust layout
            tempCS.connect(
                    id,               // SWButton
                    ConstraintSet.LEFT,
                    ConstraintSet.PARENT_ID,               // back ground
                    ConstraintSet.LEFT,
                    0);
        }else {
            // adjust layout
            tempCS.connect(
                    id,               // SWButton
                    ConstraintSet.BOTTOM,
                    1,               // back ground
                    ConstraintSet.BOTTOM,
                    FromBottom);

            // adjust layout
            tempCS.connect(
                    id,               // SWButton
                    ConstraintSet.LEFT,
                    1,               // back ground
                    ConstraintSet.LEFT,
                    FromLeft);
        }

        // apply
        tempCS.applyTo(constraintSet);

        // ConstraintLayout set on ContentView
        setContentView(constraintSet);
    }


    // --------------------------------------
    // set initial condition
    // --------------------------------------
    public void SetInitialStatus(){

        IteStatusHistory = 0;

        for (int i = 0; i < statusLength; i++) {
            for (int j = 0; j < statusLength; j++) {
                status[i][j] = 0;
            }
        }
        status[3][3] = 1; // black
        status[3][4] = 2; // white
        status[4][3] = 2; // white
        status[4][4] = 1; // black

        for (int i = 0; i < statusLength; i++) {
            for (int j = 0; j < statusLength; j++) {
                statusHistory[IteStatusHistory][i][j] = status[i][j];
            }
        }
    }
    // --------------------------------------
    // set initial condition
    // --------------------------------------
    public void SetInitialStatusHistory(){
        for (int i = 0; i < statusLength; i++) {
            for (int j = 0; j < statusLength; j++) {
                statusHistory[IteStatusHistory][i][j] = status[i][j];
            }
        }
    }

    // --------------------------------------
    // set view condition
    // --------------------------------------
    public void SetViewStatus(){
        for (int i = 0; i < statusLength; i++) {
            for (int j = 0; j < statusLength; j++) {
                // ite
                int ii = i + 1;
                int jj = j + 1;

                // cell id
                int cell_id          = ii*10 + jj;

                if (status[i][j] == -1){
                    // visible button
                    ImageButton ibtn = (ImageButton) findViewById(cell_id);
                    ibtn.setVisibility(View.VISIBLE);

                    // gone black
                    ImageView b;
                    b = (ImageView) findViewById(cell_id + 100);
                    b.setVisibility(View.GONE);

                    // gone white
                    ImageView w;
                    w = (ImageView) findViewById(cell_id + 200);
                    w.setVisibility(View.GONE);

                }else if (status[i][j] == 0){
                    // visible button
                    ImageButton ibtn = (ImageButton) findViewById(cell_id);
                    ibtn.setVisibility(View.GONE);

                    // gone black
                    ImageView b;
                    b = (ImageView) findViewById(cell_id + 100);
                    b.setVisibility(View.GONE);

                    // gone white
                    ImageView w;
                    w = (ImageView) findViewById(cell_id + 200);
                    w.setVisibility(View.GONE);
                }else if (status[i][j] == 1){
                    // visible button
                    ImageButton ibtn = (ImageButton) findViewById(cell_id);
                    ibtn.setVisibility(View.GONE);

                    // gone black
                    ImageView b;
                    b = (ImageView) findViewById(cell_id + 100);
                    b.setVisibility(View.VISIBLE);

                    // gone white
                    ImageView w;
                    w = (ImageView) findViewById(cell_id + 200);
                    w.setVisibility(View.GONE);
                }else if (status[i][j] == 2){
                    // visible button
                    ImageButton ibtn = (ImageButton) findViewById(cell_id);
                    ibtn.setVisibility(View.GONE);

                    // gone black
                    ImageView b;
                    b = (ImageView) findViewById(cell_id + 100);
                    b.setVisibility(View.GONE);

                    // gone white
                    ImageView w;
                    w = (ImageView) findViewById(cell_id + 200);
                    w.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    // --------------------------------------
    // calculate status
    // --------------------------------------
    public void CalStatus(){
        for (int i = 0; i < statusLength; i++) {
            for (int j = 0; j < statusLength; j++) {
                if (status[i][j] == 1 || status[i][j] == 2){
                    // B or W already put
                    ; // pass
                }else {
                    CheckOnLine(i,j);
                }
            }
        }
    }

    // --------------------------------------
    // update status history
    // --------------------------------------
    public void UpdateStatus(){
        IteStatusHistory += 1;
        for (int i = 0; i < statusLength; i++) {
            for (int j = 0; j < statusLength; j++) {
                statusHistory[IteStatusHistory][i][j] = status[i][j];
            }
        }
    }

    // --------------------------------------
    // undo status
    // --------------------------------------
    public void UndoStatus(){
        IteStatusHistory -= 1;
        for (int i = 0; i < statusLength; i++) {
            for (int j = 0; j < statusLength; j++) {
                status[i][j] = statusHistory[IteStatusHistory][i][j];
            }
        }
    }

    // --------------------------------------
    // decision to win or lose
    // --------------------------------------
    public void DecisionWinORLose(){
        int ite = 0;
        int iteBlack = 0;
        int iteWhite = 0;
        for (int i = 0; i < statusLength; i++) {
            for (int j = 0; j < statusLength; j++) {
                if (status[i][j] == -1){
                    ite += 1;
//                }else if(status[i][j] == 1){
//                    iteBlack += 1;
//                }else if(status[i][j] == 2){
//                    iteWhite += 1;
                }
            }
        }
        // set text BW
        onButtonBW(!BoolPlayer);
        // check status
        CalStatus();
        for (int i = 0; i < statusLength; i++) {
            for (int j = 0; j < statusLength; j++) {
                if (status[i][j] == -1){
                    ite += 1;
                }else if(status[i][j] == 1){
                    iteBlack += 1;
                }else if(status[i][j] == 2){
                    iteWhite += 1;
                }
            }
        }

        if (ite == 0){
            nBlack = iteBlack;
            nWhite = iteWhite;
            ShowTextWinLose();
        }
    }
    // --------------------------------------
    // show text win or lose
    // --------------------------------------
    public void ShowTextWinLose(){
        TextView tv = (TextView) findViewById(IdTextWinLose);
        // string
        StringBuffer buf = new StringBuffer();
        String str0;
        if (nBlack > nWhite){
            str0 = "1st player (black) WIN !";
        }else if (nBlack < nWhite) {
            str0 = "2nd player (white) WIN !";
        }else{
            str0 = "Draw !";
        }
        buf.append(str0);
        String str1 = String.format(Locale.US, "\n black : %d", nBlack);
        buf.append(str1);
        String str2 = String.format(Locale.US, "\n white : %d", nWhite);
        buf.append(str2);
        String str = buf.toString();
        tv.setText(str);
        // center
        tv.setGravity(Gravity.CENTER);

        tv.setVisibility(View.VISIBLE);
    }

    // --------------------------------------
    // Check on line
    // --------------------------------------
    public void CheckOnLine(int si, int sj){
        // vis button or not
        boolean BoolVisButton = false;

        // set color
        if (black_true) {
            now_bw = 1;
            rev_bw = 2;
        } else{
            now_bw = 2;
            rev_bw = 1;
        }

        // --------------------------------------
        // check on each direction
        // --------------------------------------
        BoolVisButton = CheckOnLineRight(si,sj, BoolVisButton);
        BoolVisButton = CheckOnLineUpperRight(si,sj, BoolVisButton);
        BoolVisButton = CheckOnLineUp(si,sj, BoolVisButton);
        BoolVisButton = CheckOnLineUpperLeft(si,sj, BoolVisButton);
        BoolVisButton = CheckOnLineLeft(si,sj, BoolVisButton);
        BoolVisButton = CheckOnLineLowerLeft(si,sj, BoolVisButton);
        BoolVisButton = CheckOnLineLow(si,sj, BoolVisButton);
        BoolVisButton = CheckOnLineLowerRight(si,sj, BoolVisButton);

        if (BoolVisButton){
            status[si][sj] = -1;
        }else {
            status[si][sj] = 0;
        }
    }

    // --------------------------------------
    // Check on right line
    // --------------------------------------
    public boolean CheckOnLineRight(int si, int sj, boolean BoolVisButton){
        Bool1st = false;
        Bool2nd = false;
        if (sj > 6) {
            ;
        }else {
            // となりが逆の色かどうか
            if (status[si][sj + 1] == rev_bw) {
                Bool1st = true;
            }

            // それ以降に白があるかどうか
            int ni  = 8;
            int nj  = 8 - sj;
            int nij = Math.min(ni, nj);
            for (int ij = 2; ij < nij; ij++) {
                if (status[si][sj + ij] == now_bw) {
                    Bool2nd = true;
                    break;
                }else if(status[si][sj + ij] == rev_bw){
                    ;
                }else{
                    break;
                }
            }

            // 二つの条件を満たすかどうか
            if (Bool1st && Bool2nd){
                BoolVisButton = true;
                ReversiDirection[si][sj][0] = true;
            }else {
                ReversiDirection[si][sj][0] = false;
            }
        }
        return BoolVisButton;
    }

    // --------------------------------------
    // Check on upper right line
    // --------------------------------------
    public boolean CheckOnLineUpperRight(int si, int sj, boolean BoolVisButton){
        Bool1st = false;
        Bool2nd = false;
        if (si < 2 || sj > 6) {
            ;
        }else {
            // となりが逆の色かどうか
            if (status[si - 1][sj + 1] == rev_bw) {
                Bool1st = true;
            }

            // それ以降に自身の色があるかどうか
            int ni  = si+1;
            int nj  = 8 - sj;
            int nij = Math.min(ni, nj);
            for (int ij = 2; ij < nij; ij++) {
                if (status[si - ij][sj + ij] == now_bw) {
                    Bool2nd = true;
                    break;
                }else if(status[si - ij][sj + ij] == rev_bw){
                    ;
                }else{
                    break;
                }
            }

            // 二つの条件を満たすかどうか
            if (Bool1st && Bool2nd){
                BoolVisButton = true;
                ReversiDirection[si][sj][1] = true;
            }else {
                ReversiDirection[si][sj][1] = false;
            }
        }
        return BoolVisButton;
    }

    // --------------------------------------
    // Check on upper line
    // --------------------------------------
    public boolean CheckOnLineUp(int si, int sj, boolean BoolVisButton){
        Bool1st = false;
        Bool2nd = false;
        if (si < 2) {
            ;
        }else {
            // となりが逆の色かどうか
            if (status[si - 1][sj] == rev_bw) {
                Bool1st = true;
            }

            // それ以降に自身の色があるかどうか
            int ni  = si+1;
            int nj  = 8;
            int nij = Math.min(ni, nj);
            for (int ij = 2; ij < nij; ij++) {
                if (status[si - ij][sj] == now_bw) {
                    Bool2nd = true;
                    break;
                }else if(status[si - ij][sj] == rev_bw){
                    ;
                }else{
                    break;
                }
            }

            // 二つの条件を満たすかどうか
            if (Bool1st && Bool2nd){
                BoolVisButton = true;
                ReversiDirection[si][sj][2] = true;
            }else {
                ReversiDirection[si][sj][2] = false;
            }
        }
        return BoolVisButton;
    }

    // --------------------------------------
    // Check on upper left line
    // --------------------------------------
    public boolean CheckOnLineUpperLeft(int si, int sj, boolean BoolVisButton){
        Bool1st = false;
        Bool2nd = false;
        if (si < 2 || sj < 2) {
            ;
        }else {
            // となりが逆の色かどうか
            if (status[si - 1][sj - 1] == rev_bw) {
                Bool1st = true;
            }

            // それ以降に自身の色があるかどうか
            int ni  = si+1;
            int nj  = sj+1;
            int nij = Math.min(ni, nj);
            for (int ij = 2; ij < nij; ij++) {
                if (status[si - ij][sj - ij] == now_bw) {
                    Bool2nd = true;
                    break;
                }else if(status[si - ij][sj - ij] == rev_bw){
                    ;
                }else{
                    break;
                }
            }

            // 二つの条件を満たすかどうか
            if (Bool1st && Bool2nd){
                BoolVisButton = true;
                ReversiDirection[si][sj][3] = true;
            }else {
                ReversiDirection[si][sj][3] = false;
            }
        }
        return BoolVisButton;
    }


    // --------------------------------------
    // Check on left line
    // --------------------------------------
    public boolean CheckOnLineLeft(int si, int sj, boolean BoolVisButton){
        Bool1st = false;
        Bool2nd = false;
        if (sj < 2) {
            ;
        }else {
            // となりが逆の色かどうか
            if (status[si][sj - 1] == rev_bw) {
                Bool1st = true;
            }

            // それ以降に白があるかどうか
            int ni  = 8;
            int nj  = sj+1;
            int nij = Math.min(ni, nj);
            for (int ij = 2; ij < nij; ij++) {
                if (status[si][sj - ij] == now_bw) {
                    Bool2nd = true;
                    break;
                }else if(status[si][sj - ij] == rev_bw){
                    ;
                }else{
                    break;
                }
            }

            // 二つの条件を満たすかどうか
            if (Bool1st && Bool2nd){
                BoolVisButton = true;
                ReversiDirection[si][sj][4] = true;
            }else {
                ReversiDirection[si][sj][4] = false;
            }
        }
        return BoolVisButton;
    }

    // --------------------------------------
    // Check on lower left line
    // --------------------------------------
    public boolean CheckOnLineLowerLeft(int si, int sj, boolean BoolVisButton){
        Bool1st = false;
        Bool2nd = false;
        if (si > 6 || sj < 2) {
            ;
        }else {
            // となりが逆の色かどうか
            if (status[si + 1][sj - 1] == rev_bw) {
                Bool1st = true;
            }

            // それ以降に自身の色があるかどうか
            int ni  = 8 - si;
            int nj  = sj+1;
            int nij = Math.min(ni, nj);
            for (int ij = 2; ij < nij; ij++) {
                if (status[si + ij][sj - ij] == now_bw) {
                    Bool2nd = true;
                    break;
                }else if(status[si + ij][sj - ij] == rev_bw){
                    ;
                }else{
                    break;
                }
            }

            // 二つの条件を満たすかどうか
            if (Bool1st && Bool2nd){
                BoolVisButton = true;
                ReversiDirection[si][sj][5] = true;
            }else {
                ReversiDirection[si][sj][5] = false;
            }
        }
        return BoolVisButton;
    }

    // --------------------------------------
    // Check on low line
    // --------------------------------------
    public boolean CheckOnLineLow(int si, int sj, boolean BoolVisButton){
        Bool1st = false;
        Bool2nd = false;
        if (si > 6) {
            ;
        }else {
            // となりが逆の色かどうか
            if (status[si + 1][sj] == rev_bw) {
                Bool1st = true;
            }

            // それ以降に自身の色があるかどうか
            int ni  = 8 - si;
            int nj  = 8;
            int nij = Math.min(ni, nj);
            for (int ij = 2; ij < nij; ij++) {
                if (status[si + ij][sj] == now_bw) {
                    Bool2nd = true;
                    break;
                }else if(status[si + ij][sj] == rev_bw){
                    ;
                }else{
                    break;
                }
            }

            // 二つの条件を満たすかどうか
            if (Bool1st && Bool2nd){
                BoolVisButton = true;
                ReversiDirection[si][sj][6] = true;
            }else {
                ReversiDirection[si][sj][6] = false;
            }
        }
        return BoolVisButton;
    }

    // --------------------------------------
    // Check on lower right line
    // --------------------------------------
    public boolean CheckOnLineLowerRight(int si, int sj, boolean BoolVisButton){
        Bool1st = false;
        Bool2nd = false;
        if (si > 6 || sj > 6) {
            ;
        }else {
            // となりが逆の色かどうか
            if (status[si + 1][sj + 1] == rev_bw) {
                Bool1st = true;
            }

            // それ以降に自身の色があるかどうか
            int ni  = 8 - si;
            int nj  = 8 - sj;
            int nij = Math.min(ni, nj);
            for (int ij = 2; ij < nij; ij++) {
                if (status[si + ij][sj + ij] == now_bw) {
                    Bool2nd = true;
                    break;
                }else if(status[si + ij][sj + ij] == rev_bw){
                    ;
                }else{
                    break;
                }
            }

            // 二つの条件を満たすかどうか
            if (Bool1st && Bool2nd){
                BoolVisButton = true;
                ReversiDirection[si][sj][7] = true;
            }else {
                ReversiDirection[si][sj][7] = false;
            }
        }
        return BoolVisButton;
    }

    // --------------------------------------
    // Check on line whether reversi
    // --------------------------------------
    public void CheckOnLineReversi(int si, int sj){

        // set color
        int now_bw;
        int rev_bw;
        if (black_true) {
            now_bw = 1;
            rev_bw = 2;
        } else{
            now_bw = 2;
            rev_bw = 1;
        }

        // put now color
        status[si][sj] = now_bw;

        // --------------------------------------
        // right
        // --------------------------------------
        // ひっくり返せるかどうか
        if (ReversiDirection[si][sj][0]){
            // 逆の色部分の数字を反転させる
            int nj  = 8 - sj;
            for (int ij = 1; ij < nj; ij++) {
                if (status[si][sj + ij] == rev_bw) {
                    status[si][sj + ij] = now_bw;
                }else{
                    break;
                }
            }
        }
        // --------------------------------------
        // upper right
        // --------------------------------------
        // ひっくり返せるかどうか
        if (ReversiDirection[si][sj][1]){
            // 逆の色部分の数字を反転させる
            int ni  = si;
            int nj  = 8 - sj;
            int nij = Math.min(ni, nj);
            for (int ij = 1; ij < nij; ij++) {
                if (status[si - ij][sj + ij] == rev_bw) {
                    status[si - ij][sj + ij] = now_bw;
                }else{
                    break;
                }
            }
        }
        // --------------------------------------
        // up
        // --------------------------------------
        // ひっくり返せるかどうか
        if (ReversiDirection[si][sj][2]){
            // 逆の色部分の数字を反転させる
            int ni  = si;
            for (int ij = 1; ij < ni; ij++) {
                if (status[si - ij][sj] == rev_bw) {
                    status[si - ij][sj] = now_bw;
                }else{
                    break;
                }
            }
        }
        // --------------------------------------
        // upper left
        // --------------------------------------
        // ひっくり返せるかどうか
        if (ReversiDirection[si][sj][3]){
            // 逆の色部分の数字を反転させる
            int ni  = si;
            int nj  = sj;
            int nij = Math.min(ni, nj);
            for (int ij = 1; ij < nij; ij++) {
                if (status[si - ij][sj - ij] == rev_bw) {
                    status[si - ij][sj - ij] = now_bw;
                }else{
                    break;
                }
            }
        }
        // --------------------------------------
        // left
        // --------------------------------------
        // ひっくり返せるかどうか
        if (ReversiDirection[si][sj][4]){
            // 逆の色部分の数字を反転させる
            int nj  = sj;
            for (int ij = 1; ij < nj; ij++) {
                if (status[si][sj - ij] == rev_bw) {
                    status[si][sj - ij] = now_bw;
                }else{
                    break;
                }
            }
        }
        // --------------------------------------
        // lower left
        // --------------------------------------
        // ひっくり返せるかどうか
        if (ReversiDirection[si][sj][5]){
            // 逆の色部分の数字を反転させる
            int ni  = 8 - si;
            int nj  = sj;
            int nij = Math.min(ni, nj);
            for (int ij = 1; ij < nij; ij++) {
                if (status[si + ij][sj - ij] == rev_bw) {
                    status[si + ij][sj - ij] = now_bw;
                }else{
                    break;
                }
            }
        }
        // --------------------------------------
        // up
        // --------------------------------------
        // ひっくり返せるかどうか
        if (ReversiDirection[si][sj][6]){
            // 逆の色部分の数字を反転させる
            int ni  = 8 - si;
            for (int ij = 1; ij < ni; ij++) {
                if (status[si + ij][sj] == rev_bw) {
                    status[si + ij][sj] = now_bw;
                }else{
                    break;
                }
            }
        }
        // --------------------------------------
        // lower right
        // --------------------------------------
        // ひっくり返せるかどうか
        if (ReversiDirection[si][sj][7]){
            // 逆の色部分の数字を反転させる
            int ni  = 8 - si;
            int nj  = 8 - sj;
            int nij = Math.min(ni, nj);
            for (int ij = 1; ij < nij; ij++) {
                if (status[si + ij][sj + ij] == rev_bw) {
                    status[si + ij][sj + ij] = now_bw;
                }else{
                    break;
                }
            }
        }
    }

    // --------------------------------------
    // cal text player
    // --------------------------------------
    public void CheckTextPlayer(boolean Player){
        if (Player) {
            TextView tv1 = (TextView) findViewById(IdText1st);
            tv1.setVisibility(View.INVISIBLE);
            TextView tv2 = (TextView) findViewById(IdText2nd);
            tv2.setVisibility(View.VISIBLE);
            BoolPlayer = false;
        }else {
            TextView tv2 = (TextView) findViewById(IdText2nd);
            tv2.setVisibility(View.INVISIBLE);
            TextView tv1 = (TextView) findViewById(IdText1st);
            tv1.setVisibility(View.VISIBLE);
            BoolPlayer = true;
        }
    }

    // --------------------------------------
    // on button BW
    // --------------------------------------
    public void onButtonBW(boolean BW){
        //     set black or white
        if (BW){
            black_true = false;
            // text

            TextView tvB = (TextView) findViewById(IdTextB);
            tvB.setVisibility(View.INVISIBLE);
            TextView tvW = (TextView) findViewById(IdTextW);
            tvW.setVisibility(View.VISIBLE);
        }else{
            black_true = true;
            // text
            TextView tvW = (TextView) findViewById(IdTextW);
            tvW.setVisibility(View.INVISIBLE);
            TextView tvB = (TextView) findViewById(IdTextB);
            tvB.setVisibility(View.VISIBLE);

        }
    }


    // --------------------------------------
    // at Click
    // --------------------------------------
    @Override
    public void onClick(View v) {
        // get Id
        int id = v.getId();

        // SWButton
        if (id==IdButtonBW){
            // onButtonBW
            onButtonBW(black_true);
            // check status
            CalStatus();
            // set view
            SetViewStatus();
        }
        // reset button
        else if (id == IdButtonReset){
            // set initial condition
            SetInitialStatus();
            // reset text player
            CheckTextPlayer(false);
            // reset BW button
            onButtonBW(false);
            // check status
            CalStatus();
            // set view
            SetViewStatus();

            // remove text Win or lose
            TextView tv = (TextView) findViewById(IdTextWinLose);
            tv.setVisibility(View.INVISIBLE);
        }
        // undo button
        else if (id == IdButtonUndo){
            // undo button
            if (IteStatusHistory == 0){
                IteStatusHistory = 1;
            }

            // set initial condition
            UndoStatus();
            // set view
            SetViewStatus();

            if (IteStatusHistory != 0){
                // set text player
                CheckTextPlayer(BoolPlayer);
                // set text BW
                onButtonBW(!BoolPlayer);
            }else{
                // set text player
                CheckTextPlayer(false);
                // set text BW
                onButtonBW(false);
            }
            // check status
            CalStatus();
            // set view
            SetViewStatus();

            // remove text Win or lose
            TextView tv = (TextView) findViewById(IdTextWinLose);
            tv.setVisibility(View.INVISIBLE);
        }
        // button on board
        else if (11 <= id && id <= 88){
            // convert id to status_id
            int sj = (id % 10) - 1;
            int si = (int) Math.round((id - sj)*0.1) -1 ;

            // set black or white
            CheckOnLineReversi(si, sj);
            // set text player
            CheckTextPlayer(BoolPlayer);
//            // set text BW
//            onButtonBW(!BoolPlayer);
            // check status
            CalStatus();
            // decision
            DecisionWinORLose();
            // set view
            SetViewStatus();
            // update status history
            UpdateStatus();
        }
    }
}