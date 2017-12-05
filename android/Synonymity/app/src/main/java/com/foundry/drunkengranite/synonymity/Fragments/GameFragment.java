package com.foundry.drunkengranite.synonymity.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.foundry.drunkengranite.synonymity.Adapters.ChoiceAdapter;
import com.foundry.drunkengranite.synonymity.Async.Get.GetWords;
import com.foundry.drunkengranite.synonymity.Entities.Get.WordProblem;
import com.foundry.drunkengranite.synonymity.R;

import java.util.ArrayList;


public class GameFragment extends Fragment implements GetWords.onReturn, ChoiceAdapter.onClick
{

    private StateKeeper stateKeeper;
    private GridView options;
    private ArrayList<WordProblem> problems;
    private ChoiceAdapter adapter;
    private RelativeLayout fragmentParent;
    private TextView currentWord;
    private boolean isLoading;
    private GetWords asyncActions;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.stateKeeper = new StateKeeper(this);
        this.problems = new ArrayList<>();
        this.adapter = new ChoiceAdapter(getContext(), this);
        showLoading();
        this.asyncActions = new GetWords(this);
        this.asyncActions.execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View toReturn = inflater.inflate(R.layout.fragment_game, container, false);
        this.options = (GridView) toReturn.findViewById(R.id.gridView);
        this.options.setAdapter(this.adapter);
        this.fragmentParent = (RelativeLayout) toReturn.findViewById(R.id.fragment_parent);

        if (isLoading)
        {
            showLoading();
        }
        this.currentWord = (TextView) toReturn.findViewById(R.id.current_word);
        this.stateKeeper.setScoreContainer((TextView) toReturn.findViewById(R.id.score));
        this.stateKeeper.setTimerContainer((TextView) toReturn.findViewById(R.id.timer));
        return toReturn;
    }

    public void addProblems(ArrayList<WordProblem> wordProblems)
    {
        if (wordProblems.size() == 0)
        {
            return;
        }
        this.problems.addAll(wordProblems);
        WordProblem currProb = this.problems.remove(0);
        this.currentWord.setText(currProb.getCurrentWord());
        this.adapter.setNewProblem(currProb);
    }

    public void showLoading()
    {
        this.isLoading = true;
        if (this.fragmentParent != null && getFragmentManager() != null)
        {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragment_parent, LoadingFragment.newInstance())
                    .addToBackStack("Loading")
                    .commitAllowingStateLoss();
        }
    }

    public void removeLoading()
    {
        this.isLoading = false;
        if (this.fragmentParent != null && getChildFragmentManager() != null)
        {
            getChildFragmentManager().popBackStack();
        }
    }


    @Override
    public void Callback(ArrayList<WordProblem> input)
    {
        this.asyncActions = null;
        removeLoading();
        addProblems(input);
        stateKeeper.reset();
    }

    @Override
    public void isCorrect(boolean isCorrect)
    {
        if (isCorrect)
        {
            if (this.problems.size() != 0)
            {
                WordProblem newProb = this.problems.remove(0);
                this.currentWord.setText(newProb.getCurrentWord());
                this.adapter.setNewProblem(newProb);
                stateKeeper.reset();
            } else
            {
                stateKeeper.setPaused();
                showLoading();
                new GetWords(this).execute();
            }
        } else
        {
            kill();
        }

    }

    public void kill()
    {
        WordProblem missedProblem = adapter.getCurrentProblem();
        String selectedOption = adapter.getLastSelectedItem();

        this.adapter.clear();
        this.stateKeeper.kill();
        this.currentWord.setVisibility(View.GONE);
        System.out.println("should be replacing");
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_parent, FinishedGame.newInstance(missedProblem, selectedOption, stateKeeper.score))
                .commitAllowingStateLoss();
    }


    private class StateKeeper
    {
        private TextView scoreContainer;

        private TextView timerContainer;
        private int score;
        private boolean paused;
        private CountDownTimer countDownTimer;
        private GameFragment parent;

        //        initializes the data holders
        StateKeeper(GameFragment parent)
        {
            this.parent = parent;
            this.paused = false;
            this.score = 0;
        }


        //        this method increments the current score, resets the timer, increments the difficulty rating
//        and it
        void reset()
        {
            this.score += 10;
            this.scoreContainer.setText(String.valueOf(this.score));

            if (this.countDownTimer != null)
            {
                this.countDownTimer.cancel();
            }
            this.countDownTimer = null;
            this.countDownTimer = new CountDownTimer(10000, 10)
            {
                @Override
                public void onTick(long millisUntilFinished)
                {
                    String time = String.valueOf(millisUntilFinished / 10);
                    if (time.length() < 2)
                    {
                        return;
                    }
                    timerContainer.setText(new StringBuilder(time).insert(time.length() - 2, ".").toString());
                }

                @Override
                public void onFinish()
                {
                    if (!paused)
                    {
                        parent.kill();
                    }
                }
            };
            this.countDownTimer.start();
        }


        void kill()
        {
            this.countDownTimer.cancel();
            this.timerContainer.setText("(✖╭╮✖)");
        }

        void setScoreContainer(TextView scoreContainer)
        {
            this.scoreContainer = scoreContainer;
        }


        void setTimerContainer(TextView timerContainer)
        {
            this.timerContainer = timerContainer;
        }

        void setPaused()
        {
            this.paused = true;
        }


    }


    //junk methods go here

    public GameFragment()
    {
        // Required empty public constructor
    }

    public static GameFragment newInstance()
    {
        GameFragment fragment = new GameFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        if (this.asyncActions != null)
        {
            this.asyncActions.delegate = null;
            this.asyncActions.cancel(true);
        }
        this.asyncActions = null;
        this.stateKeeper.kill();
    }


}
