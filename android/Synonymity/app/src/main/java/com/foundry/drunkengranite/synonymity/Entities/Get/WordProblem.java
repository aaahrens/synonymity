package com.foundry.drunkengranite.synonymity.Entities.Get;

import java.util.ArrayList;

/**
 * Created by drunkengranite on 5/17/17.
 */

public class WordProblem
{
    public String currentWord;
    public String correctWord;
    public ArrayList<String> options;

    public String getCurrentWord()
    {
        return currentWord;
    }

    public String getCorrectWord()
    {
        return correctWord;
    }

    public ArrayList<String> getOptions()
    {
        return options;
    }

    public WordProblem()
    {

    }

    public WordProblem(String word, String correctWord, ArrayList<String> options)
    {
        this.correctWord = correctWord;
        this.currentWord = word;
        this.options = options;
    }
}
