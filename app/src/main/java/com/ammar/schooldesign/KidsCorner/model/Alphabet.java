package com.ammar.schooldesign.KidsCorner.model;

public class Alphabet {
    private String name;
    private String title2;
    private String title3;
    private String title4;
    private int thumbnail1;
    private int thumbnail2;
    private int thumbnail3;
    private int thumbnail4;
    private int lettersound;
    private int sound1;
    private int sound2;
    private int sound3;

    public Alphabet() {
    }

    public Alphabet(String name, int thumbnail1) {
        this.name = name;
        this.thumbnail1 = thumbnail1;

    }

    public Alphabet(String name, int thumbnail1, int thumbnail2, int thumbnail3, int thumbnail4, int lettersound) {
        this.name = name;
        this.thumbnail1 = thumbnail1;
        this.thumbnail2 = thumbnail2;
        this.thumbnail3 = thumbnail3;
        this.thumbnail4 = thumbnail4;
        this.lettersound = lettersound;

    }

    public Alphabet(String name, int thumbnail1, int thumbnail2, int thumbnail3, int thumbnail4,
                    String title2, String title3, String title4, int lettersound, int sound1, int sound2, int sound3) {
        this.name = name;
        this.thumbnail1 = thumbnail1;
        this.thumbnail2 = thumbnail2;
        this.thumbnail3 = thumbnail3;
        this.thumbnail4 = thumbnail4;
        this.title2 = title2;
        this.title3 = title3;
        this.title4 = title4;
        this.lettersound = lettersound;
        this.sound1 = sound1;
        this.sound2 = sound2;
        this.sound3 = sound3;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail1() {
        return thumbnail1;
    }

    public void setThumbnail1(int thumbnail1) {
        this.thumbnail1 = thumbnail1;
    }

    public int getThumbnail2() {
        return thumbnail2;
    }

    public void setThumbnail2(int thumbnail2) {
        this.thumbnail2 = thumbnail2;
    }

    public int getThumbnail3() {
        return thumbnail3;
    }

    public void setThumbnail3(int thumbnail3) {
        this.thumbnail3 = thumbnail3;
    }

    public int getThumbnail4() {
        return thumbnail4;
    }

    public void setThumbnail4(int thumbnail4) {
        this.thumbnail4 = thumbnail4;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getTitle3() {
        return title3;
    }

    public void setTitle3(String title3) {
        this.title3 = title3;
    }

    public String getTitle4() {
        return title4;
    }

    public void setTitle4(String title4) {
        this.title4 = title4;
    }

    public int getLettersound() {
        return lettersound;
    }

    public void setLettersound(int lettersound) {
        this.lettersound = lettersound;
    }

    public int getSound1() {
        return sound1;
    }

    public void setSound1(int sound1) {
        this.sound1 = sound1;
    }

    public int getSound2() {
        return sound2;
    }

    public void setSound2(int sound2) {
        this.sound2 = sound2;
    }

    public int getSound3() {
        return sound3;
    }

    public void setSound3(int sound3) {
        this.sound3 = sound3;
    }
}
