package com.ammar.schooldesign.KidsCorner.fragment;


import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.ammar.schooldesign.R;
import com.ammar.schooldesign.KidsCorner.imageslid.CirclePageIndicator;
import com.ammar.schooldesign.KidsCorner.imageslid.SlideImage_Adapter;

import java.util.ArrayList;
import java.util.Random;

public class SlideFragment extends Fragment {

    private static final int[] images = new int[]{
            R.drawable.apple,
            R.drawable.ant,
            R.drawable.aeroplane,
            R.drawable.ball,
            R.drawable.balloon,
            R.drawable.bear,
            R.drawable.cat,
            R.drawable.chik,
            R.drawable.cow,
            R.drawable.dog,
            R.drawable.dolphin,
            R.drawable.donkey,
            R.drawable.elephant,
            R.drawable.egg,
            R.drawable.envelope,
            R.drawable.flower,
            R.drawable.frog,
            R.drawable.fox,
            R.drawable.goat,
            R.drawable.grasshopper,
            R.drawable.grass,
            R.drawable.horse,
            R.drawable.house,
            R.drawable.hippo,
            R.drawable.ink,
            R.drawable.injection,
            R.drawable.insect,
            R.drawable.jacket,
            R.drawable.jar,
            R.drawable.juice,
            R.drawable.kite,
            R.drawable.kettle,
            R.drawable.key,
            R.drawable.leaf,
            R.drawable.lion,
            R.drawable.lamp,
            R.drawable.mouse,
            R.drawable.moon,
            R.drawable.map,
            R.drawable.nest,
            R.drawable.net,
            R.drawable.nail,
            R.drawable.orange,
            R.drawable.octopus,
            R.drawable.owl,
            R.drawable.pencil,
            R.drawable.penguin,
            R.drawable.pineapple,
            R.drawable.queen,
            R.drawable.queue,
            R.drawable.quail,
            R.drawable.rainbow,
            R.drawable.ring,
            R.drawable.road,
            R.drawable.sun,
            R.drawable.ship,
            R.drawable.strawberry,
            R.drawable.tiger,
            R.drawable.train,
            R.drawable.turtle,
            R.drawable.umbrella,
            R.drawable.unicorn,
            R.drawable.uniform,
            R.drawable.van,
            R.drawable.vase,
            R.drawable.violin,
            R.drawable.watch,
            R.drawable.watermelon,
            R.drawable.water,
            R.drawable.xmas,
            R.drawable.xylophone,
            R.drawable.xerox,
            R.drawable.yacht,
            R.drawable.yarn,
            R.drawable.yellow,
            R.drawable.zebra,
            R.drawable.zoo,
            R.drawable.zip,
    };

    String[] words;
    private static ViewPager mpager;
    private static int currentPage = 0;
    private static int num_page = 0;
    CirclePageIndicator indicator;
    TextView word1, word2, word3;
    private ArrayList<Integer> imageArray = new ArrayList<Integer>();

    public SlideFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_slide, container, false);
        mpager = view.findViewById(R.id.view_pager);
        indicator = view.findViewById(R.id.circlePageindicator);
        word1 = view.findViewById(R.id.slidetext1);
        word2 = view.findViewById(R.id.slidetext2);
        word3 = view.findViewById(R.id.slidetext3);
        prepareArrays();
        init();
        return view;
    }

    private void prepareArrays() {
        words = new String[]{
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
    }

    private void init() {
        for (int i = 0; i < images.length; i++)
            imageArray.add(images[i]);

        mpager.setAdapter(new SlideImage_Adapter(getActivity(), imageArray));


        indicator.setViewPager(mpager);

        int r = new Random().nextInt(76) + 1;

        mpager.setCurrentItem(r, true);
        currentPage = r ;

        final float density = getResources().getDisplayMetrics().density;

        indicator.setRadius(2 * density);

        num_page = images.length;

        int rand = new Random().nextInt(3);
        int space = new Random().nextInt(num_page) + 1;

        if(rand == 0)
        {
            word1.setText(words[currentPage]);
            word2.setText(words[(currentPage + space * 2 ) % num_page]);
            word3.setText(words[(currentPage + space) % num_page]);
        }
        else if(rand == 1)
        {
            word2.setText(words[currentPage]);
            word1.setText(words[(currentPage + space * 2) % num_page]);
            word3.setText(words[(currentPage + space) % num_page]);
        }
        else {
            word3.setText(words[currentPage]);
            word1.setText(words[(currentPage + space * 2) % num_page]);
            word2.setText(words[(currentPage + space) % num_page]);

        }

            word1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(word1.getText().toString() == words[currentPage])
                    {
                        //showDialog(getActivity());
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Correct Answer!!!")
                                .setPositiveButton("Next Item", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                                        currentPage = new Random().nextInt(words.length) + 1;
                                        currentPage = (mpager.getCurrentItem() + currentPage) % words.length;
                                        mpager.setCurrentItem((mpager.getCurrentItem() + currentPage) % words.length, true);
                                    }
                                })
                                .show();
                    }
                    else{
                        Toast.makeText(getActivity(),"Try Again", Toast.LENGTH_LONG).show();
                    }
                }
            });

            word2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(word2.getText().toString() == words[currentPage])
                    {
                        //showDialog(getActivity());
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Correct Answer!!!")
                                .setPositiveButton("Next Item", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                                        currentPage = new Random().nextInt(words.length) + 1;
                                        currentPage = (mpager.getCurrentItem() + currentPage) % words.length;
                                        mpager.setCurrentItem((mpager.getCurrentItem() + currentPage) % words.length, true);

                                    }
                                })
                                .show();
                    }
                    else{
                        Toast.makeText(getActivity(),"Try Again", Toast.LENGTH_LONG).show();
                    }
                }
            });

            word3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(word3.getText().toString() == words[currentPage])
                    {
                        //showDialog(getActivity());
                        new AlertDialog.Builder(getActivity())
                                .setTitle("Correct Answer!!!")
                                .setPositiveButton("Next Item", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                                        currentPage = new Random().nextInt(words.length) + 1;
                                        currentPage = (mpager.getCurrentItem() + currentPage) % words.length;
                                        mpager.setCurrentItem((mpager.getCurrentItem() + currentPage) % words.length, true);

                                    }
                                })
                                .show();
                    }
                    else{
                        Toast.makeText(getActivity(),"Try Again", Toast.LENGTH_LONG).show();
                    }
                }
            });

                // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {
                int rand = new Random().nextInt(3);
                int space = new Random().nextInt(num_page) + 1;

                if(rand == 0)
                {
                    word1.setText(words[currentPage]);
                    word2.setText(words[(currentPage + space * 2) % num_page]);
                    word3.setText(words[(currentPage + space) % num_page]);
                }
                else if(rand == 1)
                {
                    word2.setText(words[currentPage]);
                    word1.setText(words[(currentPage + space * 2) % num_page]);
                    word3.setText(words[(currentPage + space) % num_page]);
                }
                else{
                    word3.setText(words[currentPage]);
                    word1.setText(words[(currentPage + space * 2) % num_page]);
                    word2.setText(words[(currentPage + space) % num_page]);
                }
            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });
    }

    private void showDialog(Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Correct Answer!!!")
                .setPositiveButton("Next Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("MainActivity", "Sending atomic bombs to Jupiter");
                        if(currentPage == num_page)
                            currentPage = 0;
                        mpager.setCurrentItem(currentPage++, true);

                    }
                })
                .show();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Slide Show");
    }


}
