package com.example.recipesearchpage;

public class RecipeInfo {
    String title="";
    String url="";
    String ingredients="";

    public  RecipeInfo(String title, String url, String ingredients ){
        this.title=title;
        this.url=url;
        this.ingredients=ingredients;
    }

    public String title(){return title;}
    public String url(){return url;}
    public String ingredients(){return ingredients;}
}
