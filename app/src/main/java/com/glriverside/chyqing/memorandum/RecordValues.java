package com.glriverside.chyqing.memorandum;

public class RecordValues {
    private Integer mId;
    private String mTitle;
    private String mContent;

    public void setId(Integer id){
        mId = id;
    }

    public Integer getId(){
        return mId;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public String getTitle(){
        return mTitle;
    }

    public void setContent(String content){
        mContent = content;
    }

    public String getContent(){
        return mContent;
    }
}
