package com.foundry.drunkengranite.synonymity.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;

import com.foundry.drunkengranite.synonymity.Entities.Get.WordProblem;
import com.foundry.drunkengranite.synonymity.R;

import java.util.ArrayList;
import java.util.Collections;

import me.grantland.widget.AutofitTextView;

/**
 * Created by drunkengranite on 5/17/17.
 */

public class ChoiceAdapter extends BaseAdapter
{
    private onClick delegate;
    private ArrayList<String> choices;
    private String correctWord;
    private Context context;
    private WordProblem currentProblem;
    private String lastClickedWord;

    @Override
    public int getCount()
    {
        return choices.size();
    }

    @Override
    public Object getItem(int position)
    {
        return choices.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        RelativeLayout localView;
        if (convertView == null)
        {
            localView = (RelativeLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.choice_button, null);
        } else
        {
            localView = (RelativeLayout) convertView;
        }
        AutofitTextView textView = (AutofitTextView) localView.findViewById(R.id.textHolder);
        textView.setText((String) getItem(position));
        localView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                lastClickedWord = (String) getItem(position);
                delegate.isCorrect(getItem(position).equals(correctWord));
            }
        });
        return localView;
    }

    public interface onClick
    {
        void isCorrect(boolean wrong);
    }

    public ChoiceAdapter(Context context, onClick delegate)
    {
        this.delegate = delegate;
        this.context = context;
        this.choices = new ArrayList<>();
    }

    public void setNewProblem(WordProblem problem)
    {
        this.currentProblem = problem;
        this.correctWord = problem.getCorrectWord();
        ArrayList<String> newChoices;
        if (problem.getOptions().size() > 10)
        {
            newChoices = new ArrayList<>(problem.getOptions().subList(0, 8));
        } else
        {
            newChoices = problem.getOptions();
        }
        newChoices.add(problem.getCorrectWord());
        Collections.shuffle(newChoices);
        this.choices = newChoices;
        notifyDataSetChanged();
    }

    public void clear()
    {
        this.choices = new ArrayList<>();
        this.correctWord = "";
        notifyDataSetChanged();
    }

    public WordProblem getCurrentProblem()
    {
        return this.currentProblem;
    }

    public String getLastSelectedItem()
    {
        return this.lastClickedWord;
    }
}
