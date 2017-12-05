package com.Entities;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * Created by drunkengranite on 5/17/17.
 */
@Entity
@Table(name = "problems")
public class Problem implements Serializable
{
    @Id
    private int id;
    @Expose
    @Column(name = "word")
    private String word;
    @Expose
    @OneToMany(mappedBy = "problem")
    private List<Word> words;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public List<Word> getWords()
    {
        return words;
    }

    public void setWords(List<Word> words)
    {
        this.words = words;
    }
    public String getWord()
    {
        return word;
    }

    public void setWord(String word)
    {
        this.word = word;
    }


}


