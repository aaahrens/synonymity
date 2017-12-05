package com.foundry.drunkengranite.synonymity.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.foundry.drunkengranite.synonymity.Entities.Get.WordProblem;
import com.foundry.drunkengranite.synonymity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FinishedGame extends Fragment
{

    private String correctWord;
    private String wrongWord;
    private String originalWord;
    private int score;

    public FinishedGame()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle saves)
    {
        super.onCreate(saves);
        if (getArguments() != null)
        {
            this.wrongWord = (String) getArguments().get("inCorrect");
            this.correctWord = (String) getArguments().get("correct");
            this.originalWord = (String) getArguments().get("originalWord");
            this.score = getArguments().getInt("score");

        }
    }

    public static FinishedGame newInstance(WordProblem word, String inCorrectOption, int score)
    {
        Bundle args = new Bundle();
        FinishedGame fragment = new FinishedGame();
        args.putString("originalWord", word.getCurrentWord());
        args.putInt("score", score);
        args.putString("inCorrect", inCorrectOption);
        args.putString("correct", word.getCorrectWord());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View toReturn = inflater.inflate(R.layout.fragment_finished_game, container, false);
        TextView correctWord = (TextView) toReturn.findViewById(R.id.correctWord);
        TextView wrongWordSelected = (TextView) toReturn.findViewById(R.id.selectedWord);
        TextView score = (TextView) toReturn.findViewById(R.id.final_score);
        TextView originalWord = (TextView) toReturn.findViewById(R.id.original_word);
        Button restart = (Button) toReturn.findViewById(R.id.restart);


        score.setText("Score: \n " + this.score);
        originalWord.setText("The word was : " + this.originalWord);
        correctWord.setText("The correct answer was " + this.correctWord);
        wrongWordSelected.setText("But you selected " + this.wrongWord);


        restart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.parent, GameFragment.newInstance())
                        .commitAllowingStateLoss();
            }
        });
        return toReturn;
    }

}
