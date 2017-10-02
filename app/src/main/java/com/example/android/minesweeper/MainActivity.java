package com.example.android.minesweeper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,View.OnLongClickListener {

    public int size;
    LinearLayout rootLayout;
    LinearLayout[] rows;
    CustomButton[][] board;

    boolean[][] clicked;
    public static final int INCOMPLETE = 0;
    public static final int COMPLETE = 1;
    public int currentStatus = INCOMPLETE;
    int mines;
    public int noOfMines = 0;
    public int revealed = 0;
    SharedPreferences sharedPreferences;
    boolean[][] longclicked;
   // SharedPreferences.Editor editor = sharedPreferences.edit();
    int last_score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayout = (LinearLayout) findViewById(R.id.rootLayout);

        Intent intent = getIntent();
        size = intent.getIntExtra("level",6);
        mines = size;
        sharedPreferences = getSharedPreferences("Minesweeper",MODE_PRIVATE);

        //last_score = sharedPreferences.getInt("score",0);
        setUpBoard();
       // Toast.makeText(this,"Last score="+last_score,Toast.LENGTH_SHORT).show();

    }

    public void setUpBoard() {
        rows = new LinearLayout[size];
        board = new CustomButton[size][size];
        currentStatus = INCOMPLETE;
        revealed = 0;
        noOfMines = 0;

        rootLayout.removeAllViews();

        for (int i = 0; i < size; ++i) {

            LinearLayout linearLayout = new LinearLayout(this);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(params);
            rows[i] = linearLayout;
            rootLayout.addView(linearLayout);
        }
        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                CustomButton button = new CustomButton(this, i, j);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                // button.setBackgroundColor(getResources().getColor(R.color.black));
                button.setLayoutParams(params);
                button.setOnClickListener(this);
                button.setOnLongClickListener(this);
                board[i][j] = button;
                rows[i].addView(button);
            }
        }

        clicked = new boolean[size][size];
        longclicked = new boolean[size][size];

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                clicked[i][j] = false;
                longclicked[i][j] = false;
            }
        }
        for (int j = 0; j < mines; ++j) {
            Random rand = new Random();
            int row = rand.nextInt(size);
            int col = rand.nextInt(size);

            if (board[row][col].flag != -1) {
                board[row][col].flag = -1;
                setNeighbours(row, col);
                noOfMines++;
            }
        }
    }
        //setting nos to buttons

    private void setNeighbours(int row, int col) {
        Log.i("TAG", "setNeighbours: at "+row+" "+col);
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int i = 0; i < 8; i++) {
            int x = row + dx[i];
            int y = col + dy[i];
            if (x >= 0 && x < size && y >= 0 && y < size) {
                if (board[x][y].flag != -1) {
                    board[x][y].flag++;
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.Reset) {
            setUpBoard();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View view) {

        if (currentStatus == INCOMPLETE) {
            CustomButton btn = (CustomButton) view;

            int i = btn.getRow();
            int j = btn.getCol();

            if (longclicked[i][j])  return;

            if (clicked[i][j]) {
                Toast.makeText(this, "Click another box!", Toast.LENGTH_SHORT).show();
            } else
                //if I encounter a mine
                if (board[i][j].flag == -1) {
//                    board[i][j].setText("mine");
                    board[i][j].setBackground(getResources().getDrawable(R.drawable.flag));
                    clicked[i][j] = true;
                    Toast.makeText(this, "Oops! Game Over!Score:"+revealed, Toast.LENGTH_LONG).show();
//                    editor.putInt("score",revealed);
//                    editor.commit();
                    currentStatus = COMPLETE;
                } else
                    setClick(i, j);
        } else
            Toast.makeText(this, "Reset game", Toast.LENGTH_SHORT).show();
    }

    public void setClick(int i, int j) {
        if (clicked[i][j]) return;
        revealed++;
        if(noOfMines == (size*size- revealed)){
            board[i][j].setText(board[i][j].flag+"");
            Toast.makeText(this,"You won!!  Score:"+ revealed,Toast.LENGTH_LONG).show();
//            editor.putInt("score",revealed);
//            editor.commit();
            currentStatus = COMPLETE;
            return;
        }
        //if I win the game
        clicked[i][j] = true;

        if (board[i][j].flag != 0) {
            String s = String.valueOf(board[i][j].flag);
            board[i][j].setText(s);
//            Toast.makeText(this,"Good going!",Toast.LENGTH_SHORT).show();
            return;
        }
        //it is empty
        else if (board[i][j].flag == 0) {
            board[i][j].setText("0");
            //   Toast.makeText(this,"You clicked an empty cell",Toast.LENGTH_SHORT).show();
            if (i - 1 >= 0 && !clicked[i - 1][j])
                setClick(i - 1, j);
            if (j - 1 >= 0 && !clicked[i][j - 1])
                setClick(i, j - 1);
            if (i - 1 >= 0 && j - 1 >= 0 && !clicked[i - 1][j - 1])
                setClick(i - 1, j - 1);
            if (i + 1 < size && !clicked[i + 1][j])
                setClick(i + 1, j);
            if (j + 1 < size && !clicked[i][j + 1])
                setClick(i, j + 1);
            if (i + 1 < size && j + 1 < size && !clicked[i + 1][j + 1])
                setClick(i + 1, j + 1);
            if (i - 1 >= 0 && j + 1 < size && !clicked[i - 1][j + 1])
                setClick(i - 1, j + 1);
            if (i + 1 < size && j - 1 >= 0 && !clicked[i + 1][j - 1])
                setClick(i + 1, j - 1);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        CustomButton btn = (CustomButton)view;
        int i = btn.getRow();
        int j = btn.getCol();
        if(!clicked[i][j]) {
            if (!longclicked[i][j]) {
               // btn.setEnabled(false);
                longclicked[i][j] = true;
                btn.setBackground(getResources().getDrawable(R.drawable.flag));
               // btn.setText("Flag");
            }
            else if(longclicked[i][j]){
                longclicked[i][j] = false;
                btn.setBackground(null);
            }
            return true;
        }
        return true;
    }
}
