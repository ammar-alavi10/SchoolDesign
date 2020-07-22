package com.ammar.schooldesign.KidsCorner.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ammar.schooldesign.R;

import java.util.Random;

public class DrawingFragment extends Fragment implements View.OnClickListener {

    String[] words = new String[]{
            getString(R.string.apple),
            getString(R.string.ant),
            getString(R.string.aeroplane),
            getString(R.string.ball),
            getString(R.string.balloon),
            getString(R.string.bear),
            getString(R.string.cat),
            getString(R.string.chick),
            getString(R.string.cow),
            getString(R.string.dog),
            getString(R.string.dolphin),
            getString(R.string.donkey),
            getString(R.string.elephant),
            getString(R.string.egg),
            getString(R.string.envelope),
            getString(R.string.flower),
            getString(R.string.frog),
            getString(R.string.fox),
            getString(R.string.goat),
            getString(R.string.grasshopper),
            getString(R.string.grass),
            getString(R.string.horse),
            getString(R.string.house),
            getString(R.string.hippo),
            getString(R.string.ink),
            getString(R.string.injection),
            getString(R.string.insect),
            getString(R.string.jacket),
            getString(R.string.jar),
            getString(R.string.juice),
            getString(R.string.kite),
            getString(R.string.kettle),
            getString(R.string.key),
            getString(R.string.leaf),
            getString(R.string.lion),
            getString(R.string.lamp),
            getString(R.string.mouse),
            getString(R.string.moon),
            getString(R.string.map),
            getString(R.string.nest),
            getString(R.string.net),
            getString(R.string.nail),
            getString(R.string.orange),
            getString(R.string.octopus),
            getString(R.string.owl),
            getString(R.string.pencil),
            getString(R.string.penguin),
            getString(R.string.pineapple),
            getString(R.string.queen),
            getString(R.string.queue),
            getString(R.string.quail),
            getString(R.string.rainbow),
            getString(R.string.ring),
            getString(R.string.road),
            getString(R.string.sun),
            getString(R.string.ship),
            getString(R.string.strawberry),
            getString(R.string.tiger),
            getString(R.string.train),
            getString(R.string.turtle),
            getString(R.string.umbrella),
            getString(R.string.unicorn),
            getString(R.string.uniform),
            getString(R.string.van),
            getString(R.string.vase),
            getString(R.string.violin),
            getString(R.string.watch),
            getString(R.string.watermelon),
            getString(R.string.water),
            getString(R.string.xmas),
            getString(R.string.xylophone),
            getString(R.string.xerox),
            getString(R.string.yacht),
            getString(R.string.yarn),
            getString(R.string.yellow),
            getString(R.string.zebra),
            getString(R.string.zoo),
            getString(R.string.zip),
    };

    public DrawingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drawing, container, false);

        TextView tv = view.findViewById(R.id.DrawText);
        int random = new Random().nextInt((77) + 1);
        String newWord = words[random];
        newWord = newWord.toUpperCase();
        tv.setText(newWord);
        tv.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.DrawText) {
            ChangeWord(view);
        }
    }

    private void ChangeWord(View view) {
        int random = new Random().nextInt((77) + 1);
        String newWord = words[random];
        newWord = newWord.toUpperCase();
        TextView tv = view.findViewById(R.id.DrawText);
        tv.setText(newWord);
    }
}
