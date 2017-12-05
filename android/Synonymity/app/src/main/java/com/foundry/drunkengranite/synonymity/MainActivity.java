package com.foundry.drunkengranite.synonymity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.foundry.drunkengranite.synonymity.Fragments.MenuFragment;

public class MainActivity extends AppCompatActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getSupportFragmentManager()
				.beginTransaction()
				.replace(R.id.parent, MenuFragment.newInstance())
				.addToBackStack("GAME")
				.commitAllowingStateLoss();
	}

	@Override
	public void onBackPressed() {
		if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
			finish();
			return;
		}
		if (getFragmentManager().findFragmentByTag("LOADING") != null) {

		}
		super.onBackPressed();
	}

}
