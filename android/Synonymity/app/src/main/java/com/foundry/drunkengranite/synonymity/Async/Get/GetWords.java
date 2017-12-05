package com.foundry.drunkengranite.synonymity.Async.Get;

import android.os.AsyncTask;

import com.foundry.drunkengranite.synonymity.Entities.Get.WordProblem;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by drunkengranite on 5/10/17.
 */

public class GetWords extends AsyncTask<Void, Void, ArrayList<WordProblem>>
{
    private final OkHttpClient client = new OkHttpClient();
    private static final String URL_STRING = "http://163.172.172.179:4567/problem";
    private static Type REPONSE_TYPE = new TypeToken<ArrayList<WordProblem>>()
    {
    }.getType();

    public interface onReturn
    {
        void Callback(ArrayList<WordProblem> input);
    }

    public onReturn delegate;

    public GetWords(onReturn delegate)
    {
        this.delegate = delegate;
    }

    @Override
    protected ArrayList<WordProblem> doInBackground(Void... params)
    {
        ArrayList<WordProblem> toReturn = new ArrayList<>();
        try
        {

            Request request = new Request.Builder()
                    .url(URL_STRING)
                    .build();

            Response response;
            response = client.newCall(request).execute();

            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

            String resp = response.body().string();
            response.close();
            Gson gson = new Gson();
            return gson.fromJson(resp, REPONSE_TYPE);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    protected void onPostExecute(ArrayList<WordProblem> input)
    {
        if (delegate != null)
        {
            delegate.Callback(input);
        }
    }


    class Problem
    {
        String correctAnswer;
        String word;

        public String getCorrectAnswer()
        {
            return correctAnswer;
        }

        public void setCorrectAnswer(String correctAnswer)
        {
            this.correctAnswer = correctAnswer;
        }

        public String getWord()
        {
            return word;
        }

        public void setWord(String word)
        {
            this.word = word;
        }

        public Problem()
        {
        }
    }

    class ProblemResponse
    {
        ArrayList<String> options;
        Problem problem;

        public ProblemResponse()
        {

        }

        public ArrayList<String> getOptions()
        {
            return options;
        }

        public void setOptions(ArrayList<String> options)
        {
            this.options = options;
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

}
