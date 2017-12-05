package com.Entities;

import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by drunkengranite on 5/20/17.
 */
@Entity
@Table(name = "words")
public class Word implements Serializable
{
    @Id
    private int Id;
    @Expose
    @Column(name = "word")
    private String word;

    @ManyToOne
    @JoinColumn(name = "problem_id")
    private Problem problem;

    public Word()
    {

    }

    public int getId()
    {
        return Id;
    }

    public void setId(int id)
    {
        Id = id;
    }

    public String getWord()
    {
        return word;
    }

    public void setWord(String word)
    {
        this.word = word;
    }

    public Problem getProblem()
    {
        return problem;
    }

    public void setProblem(Problem problem)
    {
        this.problem = problem;
    }


}
