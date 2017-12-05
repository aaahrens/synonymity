package com;
/**
 * Created by drunkengranite on 5/17/17.
 */


import java.util.ArrayList;

import com.Entities.Problem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Spark;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


public class Handler
{
    public Database database;
    ArrayList<Problem> allProblems;

    public Handler(Database database)
    {
        this.database = database;
    }

    public static void main(String[] args)
    {
        Spark.get("/", (req, res) -> "I am healthy");

        Database database = new Database();
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();

        Spark.get("/problem", (req, res) -> {
            if (!database.ready){
                return database.statusMessage;
            }
            return gson.toJson(database.getProblemSet());
        });


        System.out.println("initializing connection");
        database.init();
        System.out.println("running hawt");
    }

    public static void init()
    {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("database");
    }

}
