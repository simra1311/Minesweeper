package com.example.android.minesweeper;

import android.content.Context;
import android.widget.Button;

/**
 * Created by Simra Afreen on 30-08-2017.
 */

public class CustomButton extends Button{

    public final int row;
    public final int col;
    public  int flag = 0;

    public CustomButton(Context context,int i,int j) {
        super(context);
        row = i;
        col = j;
        //setPadding(0,0,0,0);

    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }


}
