package com.example.recipesearchpage;

/**
 * class to store recipe info
 */
public class RecipeInfo {
    String title="";
    String url="";
    String ingredients="";

    /**
     * constructor create instance of recipe info
     * @param title the text display title
     * @param url the text display url
     * @param ingredients the text display ingredients
     */
    public  RecipeInfo(String title, String url, String ingredients ){
        this.title=title;
        this.url=url;
        this.ingredients=ingredients;
    }

    /**
     *gets title
     * @return return title
     */
    public String title(){return title;}

    /**
     * gets url
     * @return return url
     */
    public String url(){return url;}

    /**
     * gets ingredients
     * @return return ingredients
     */
    public String ingredients(){return ingredients;}
}
