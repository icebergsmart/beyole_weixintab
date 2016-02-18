package com.beyole.weixintab;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TabFragment extends Fragment {

	private String mTitle = "default";

	public TabFragment() {
	}

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		if (getArguments() != null) {
			mTitle = getArguments().getString("title");
		}
		TextView textView = new TextView(getActivity());
		textView.setTextSize(20);
		textView.setBackgroundColor(Color.parseColor("#ffffffff"));
		textView.setGravity(Gravity.CENTER);
		textView.setText(mTitle);
		return textView;
	}
}
