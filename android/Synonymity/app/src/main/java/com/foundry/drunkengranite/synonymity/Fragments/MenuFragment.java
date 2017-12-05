package com.foundry.drunkengranite.synonymity.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.foundry.drunkengranite.synonymity.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment
{


    public MenuFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View toReturn = inflater.inflate(R.layout.menu_layout, container, false);
        Button start = (Button) toReturn.findViewById(R.id.start);
        Button leaderBoard = (Button) toReturn.findViewById(R.id.leaderboard);

        start.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.parent, GameFragment.newInstance())
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            }
        });

        leaderBoard.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(), "Coming soon!", Toast.LENGTH_SHORT).show();
            }
        });
        return toReturn;
    }

    public static MenuFragment newInstance()
    {

        Bundle args = new Bundle();

        MenuFragment fragment = new MenuFragment();
        fragment.setArguments(args);
        return fragment;
    }

}
