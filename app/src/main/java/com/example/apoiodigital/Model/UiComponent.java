package com.example.apoiodigital.Model;

public class UiComponent {

    private final int viewID;

    private final String className;

    private final String addicionalInfo;

    public int getViewID() { return viewID; }

    public String getClassName() {
        return className;
    }

    public String getAddicionalInfo() {
        return addicionalInfo;
    }

    public UiComponent(int viewId, String className, String addicionalInfo) {
        this.viewID = viewId;
        this.className = className;
        this.addicionalInfo = addicionalInfo;
    }

}
