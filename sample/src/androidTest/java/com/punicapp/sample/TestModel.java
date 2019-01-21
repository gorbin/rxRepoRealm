package com.punicapp.sample;

import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Evgeny on 24.01.18.
 */

public class TestModel extends RealmObject {
    @PrimaryKey
    private int id;
    private String name = "";
    private Date dateValue = Calendar.getInstance().getTime();
    private byte byteValue;
    private short shortValue;
    private long longValue;
    private boolean booleanValue;
    private float floatValue;
    private double doubleValue;


    public TestModel() {
    }

    public TestModel(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDateValue() {
        return dateValue;
    }

    public void setDateValue(Date dateValue) {
        this.dateValue = dateValue;
    }

    public byte getByteValue() {
        return byteValue;
    }

    public void setByteValue(byte byteValue) {
        this.byteValue = byteValue;
    }

    public short getShortValue() {
        return shortValue;
    }

    public void setShortValue(short shortValue) {
        this.shortValue = shortValue;
    }

    public long getLongValue() {
        return longValue;
    }

    public void setLongValue(long longValue) {
        this.longValue = longValue;
    }

    public boolean isBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public float getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(float floatValue) {
        this.floatValue = floatValue;
    }

    public double getDoubleValue() {
        return doubleValue;
    }

    public void setDoubleValue(double doubleValue) {
        this.doubleValue = doubleValue;
    }

    @Override
    public boolean equals(Object secondTestModel) {
        if (!(secondTestModel instanceof TestModel)) {
            return false;
        }

        TestModel testModel = (TestModel) secondTestModel;

        boolean isIntEquivalent = this.id == testModel.id;
        boolean isNameEquivalent = this.name.equals(testModel.name);
        boolean isDateEquivalent = this.dateValue.equals(testModel.dateValue);
        boolean isByteEquivalent = this.byteValue == testModel.byteValue;
        boolean isShortEquivalent = this.shortValue == testModel.shortValue;
        boolean isLongEquivalent = this.longValue == testModel.longValue;
        boolean isBooleanEquivalent = this.booleanValue == testModel.booleanValue;
        boolean isFloatEquivalent = this.floatValue == testModel.floatValue;
        boolean isDoubleEquivalent = this.doubleValue == testModel.doubleValue;

        boolean isEquivalent = isIntEquivalent && isNameEquivalent && isDateEquivalent && isByteEquivalent && isShortEquivalent && isLongEquivalent && isBooleanEquivalent && isFloatEquivalent && isDoubleEquivalent;
        return isEquivalent;
    }
}
