package com.example.healthpt;

public class Machine {//운동기구 정보 저장하는 클래스
    private String name;
    private String crowdLevel;
    private int imageResId;

    public Machine(String name, String crowdLevel, int imageResId) {
        this.name = name;
        this.crowdLevel = crowdLevel;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public String getCrowdLevel() {
        return crowdLevel;
    }

    public int getImageResId() {
        return imageResId;
    }
}
