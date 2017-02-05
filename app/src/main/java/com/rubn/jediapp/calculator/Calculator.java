package com.rubn.jediapp.calculator;

import android.provider.DocumentsContract;
import android.util.Log;

/**
 * Created by Rubén on 30/01/2017.
 */

public class Calculator {

    private static final String TAG = "Calculator";
    private static Calculator mCalculator;
    private double mFirstNumber = 0;
    private double mSecondNumber = 0;
    private int mOperand = Operands.NONE;
    public static Calculator getInstance(){
        if(mCalculator == null)
            mCalculator = new Calculator();
        return mCalculator;
    }
    public double setDelete()
    {
        mFirstNumber = 0;
        mSecondNumber = 0;
        mOperand = Operands.NONE;
        return mFirstNumber;
    }
    public double setErase()
    {
        //TODO Redo
        double mNumReturn = 0;
        if(mOperand != Operands.NONE){
            if(mSecondNumber != 0){
                if (mSecondNumber % 1 == 0)
                    mSecondNumber = (int)mSecondNumber / 10;
                else
                    mSecondNumber = Double.parseDouble(String.valueOf(mSecondNumber).substring(0,((String.valueOf(mFirstNumber).length()-2>=0)? String.valueOf(mFirstNumber).length()-2:0)));
                Log.v(TAG, "Erase Second: "+mSecondNumber);
                mNumReturn = mSecondNumber;
            }
        }
        else {
            if(mFirstNumber != 0) {
                if (mFirstNumber % 1 == 0)
                    mFirstNumber = (int)mFirstNumber / 10;
                else
                    mSecondNumber = Double.parseDouble(String.valueOf(mFirstNumber).substring(0,((String.valueOf(mFirstNumber).length()-2>=0)? String.valueOf(mFirstNumber).length()-2:0)));
                Log.v(TAG, "Erase First: "+mFirstNumber);
                mNumReturn = mFirstNumber;
            }
        }
        return mNumReturn;
    }
    public double setNumber(int number)
    {
        double mNumReturn = 0;
        if(mOperand != Operands.NONE){
            mSecondNumber *= 10;
            mSecondNumber += number;
            mNumReturn = mSecondNumber;
        }
        else {

            mFirstNumber *= 10;
            mFirstNumber += number;
            mNumReturn = mFirstNumber;
        }
        return mNumReturn;
    }

    public double setOperand(int operand)
    {
        double returnNumber;
        if(operand == Operands.EQUAL) {
            if(mOperand == Operands.NONE)
                returnNumber = mFirstNumber;
            else
                returnNumber = getResultOperation();
            mFirstNumber = 0;
            mOperand = Operands.NONE;
        }
        else
        {
            if(mOperand != Operands.NONE)
            {
                mFirstNumber = returnNumber = getResultOperation();
                mOperand = operand;
                mSecondNumber = 0;
            }
            else {
                mOperand = operand;
                returnNumber = mFirstNumber;
            }
        }
        return returnNumber;
    }

    private double getResultOperation() {

        switch (mOperand)
        {
            case Operands.DIVISION:
                return mFirstNumber/mSecondNumber;
            case Operands.MULTIPLICATION:
                return mFirstNumber*mSecondNumber;
            case Operands.SUM:
                return mFirstNumber+mSecondNumber;
            case Operands.SUBSTRACT:
                return mFirstNumber-mSecondNumber;
            default:
                return 0;
        }
    }
    public Calculator() {
    }
}
