package com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.fragment;


import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ammar.shreeKrishnaNationalSchoolOfExcellence.R;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.adapter.AlphabetAdapter;
import com.ammar.shreeKrishnaNationalSchoolOfExcellence.KidsCorner.model.Alphabet;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private AlphabetAdapter adapter;
    private List<Alphabet> alphabetList;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.hf_RV);

        alphabetList = new ArrayList<>();
        adapter = new AlphabetAdapter(getActivity(), alphabetList);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        prepareAlbums();

        return view;
    }

    private void prepareAlbums() {

        int[] covers = new int[]{
                R.drawable.acopy,
                R.drawable.bcopy,
                R.drawable.ccopy,
                R.drawable.dcopy,
                R.drawable.ecopy,
                R.drawable.fcopy,
                R.drawable.gcopy,
                R.drawable.hcopy,
                R.drawable.icopy,
                R.drawable.jcopy,
                R.drawable.kcopy,
                R.drawable.lcopy,
                R.drawable.mcopy,
                R.drawable.ncopy,
                R.drawable.ocopy,
                R.drawable.pcopy,
                R.drawable.qcopy,
                R.drawable.rcopy,
                R.drawable.scopy,
                R.drawable.tcopy,
                R.drawable.ucopy,
                R.drawable.vcopy,
                R.drawable.wcopy,
                R.drawable.xcopy,
                R.drawable.ycopy,
                R.drawable.zcopy,
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
        String[] title = new String[]{
                getString(R.string.acopy),
                getString(R.string.bcopy),
                getString(R.string.ccopy),
                getString(R.string.dcopy),
                getString(R.string.ecopy),
                getString(R.string.fcopy),
                getString(R.string.gcopy),
                getString(R.string.hcopy),
                getString(R.string.icopy),
                getString(R.string.jcopy),
                getString(R.string.Kcopy),
                getString(R.string.lcopy),
                getString(R.string.mcopy),
                getString(R.string.ncopy),
                getString(R.string.ocopy),
                getString(R.string.pcopy),
                getString(R.string.qcopy),
                getString(R.string.rcopy),
                getString(R.string.scopy),
                getString(R.string.tcopy),
                getString(R.string.ucopy),
                getString(R.string.vcopy),
                getString(R.string.wcopy),
                getString(R.string.xcopy),
                getString(R.string.ycopy),
                getString(R.string.zcopy),
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

        int[] sounds = new int[]{
                R.raw.a,
                R.raw.b,
                R.raw.c,
                R.raw.d,
                R.raw.e,
                R.raw.f,
                R.raw.g,
                R.raw.h,
                R.raw.i,
                R.raw.j,
                R.raw.k,
                R.raw.l,
                R.raw.m,
                R.raw.n,
                R.raw.o,
                R.raw.p,
                R.raw.q,
                R.raw.r,
                R.raw.s,
                R.raw.t,
                R.raw.u,
                R.raw.v,
                R.raw.w,
                R.raw.x,
                R.raw.y,
                R.raw.z,
                R.raw.apple,
                R.raw.ant,
                R.raw.aeroplane,
                R.raw.ball,
                R.raw.balloon,
                R.raw.bear,
                R.raw.cat,
                R.raw.chick,
                R.raw.cow,
                R.raw.dog,
                R.raw.dolphin,
                R.raw.donkey,
                R.raw.elephant,
                R.raw.egg,
                R.raw.envelope,
                R.raw.flower,
                R.raw.frog,
                R.raw.fox,
                R.raw.goat,
                R.raw.grasshopper,
                R.raw.grass,
                R.raw.horse,
                R.raw.house,
                R.raw.hippo,
                R.raw.ink,
                R.raw.injection,
                R.raw.insect,
                R.raw.jacket,
                R.raw.jar,
                R.raw.juice,
                R.raw.kite,
                R.raw.kettle,
                R.raw.key,
                R.raw.leaf,
                R.raw.lion,
                R.raw.lamp,
                R.raw.mouse,
                R.raw.moon,
                R.raw.map,
                R.raw.nest,
                R.raw.net,
                R.raw.nail,
                R.raw.orange,
                R.raw.octopus,
                R.raw.owl,
                R.raw.pencil,
                R.raw.penguin,
                R.raw.pineapple,
                R.raw.queen,
                R.raw.queue,
                R.raw.quail,
                R.raw.rainbow,
                R.raw.ring,
                R.raw.road,
                R.raw.sun,
                R.raw.ship,
                R.raw.strawberry,
                R.raw.tiger,
                R.raw.train,
                R.raw.turtle,
                R.raw.umbrella,
                R.raw.unicorn,
                R.raw.uniform,
                R.raw.van,
                R.raw.vase,
                R.raw.violin,
                R.raw.watch,
                R.raw.watermelon,
                R.raw.water,
                R.raw.xmas,
                R.raw.xylophone,
                R.raw.xerox,
                R.raw.yacht,
                R.raw.yarn,
                R.raw.yellow,
                R.raw.zebra,
                R.raw.zoo,
                R.raw.zip,
        };

        Alphabet a = new Alphabet(title[0], covers[0], covers[26], covers[27], covers[28], title[26], title[27], title[28], sounds[0], sounds[26], sounds[27], sounds[28]);
        alphabetList.add(a);
        a = new Alphabet(title[1], covers[1], covers[29], covers[30], covers[31], title[29], title[30], title[31], sounds[1], sounds[29], sounds[30], sounds[31]);
        alphabetList.add(a);
        a = new Alphabet(title[2], covers[2], covers[32], covers[33], covers[34], title[32], title[33], title[34], sounds[2], sounds[32], sounds[33], sounds[34]);
        alphabetList.add(a);
        a = new Alphabet(title[3], covers[3], covers[35], covers[36], covers[37], title[35], title[36], title[37], sounds[3], sounds[35], sounds[36], sounds[37]);
        alphabetList.add(a);
        a = new Alphabet(title[4], covers[4], covers[38], covers[39], covers[40], title[38], title[39], title[40], sounds[4], sounds[38], sounds[39], sounds[40]);
        alphabetList.add(a);
        a = new Alphabet(title[5], covers[5], covers[41], covers[42], covers[43], title[41], title[42], title[43], sounds[5], sounds[41], sounds[42], sounds[43]);
        alphabetList.add(a);
        a = new Alphabet(title[6], covers[6], covers[44], covers[45], covers[46], title[44], title[45], title[46], sounds[6], sounds[44], sounds[45], sounds[46]);
        alphabetList.add(a);
        a = new Alphabet(title[7], covers[7], covers[47], covers[48], covers[49], title[47], title[48], title[49], sounds[7], sounds[47], sounds[48], sounds[49]);
        alphabetList.add(a);
        a = new Alphabet(title[8], covers[8], covers[50], covers[51], covers[52], title[50], title[51], title[52], sounds[8], sounds[50], sounds[51], sounds[52]);
        alphabetList.add(a);
        a = new Alphabet("Jj", covers[9], covers[53], covers[54], covers[55], title[53], title[54], title[55], sounds[9], sounds[53], sounds[54], sounds[55]);
        alphabetList.add(a);
        a = new Alphabet("Kk", covers[10], covers[56], covers[57], covers[58], title[56], title[57], title[58], sounds[10], sounds[56], sounds[57], sounds[58]);
        alphabetList.add(a);
        a = new Alphabet("Ll", covers[11], covers[59], covers[60], covers[61], title[59], title[60], title[61], sounds[11], sounds[59], sounds[60], sounds[61]);
        alphabetList.add(a);
        a = new Alphabet("Mm", covers[12], covers[62], covers[63], covers[64], title[62], title[63], title[64], sounds[12], sounds[62], sounds[63], sounds[64]);
        alphabetList.add(a);
        a = new Alphabet("Nn", covers[13], covers[65], covers[66], covers[67], title[65], title[66], title[67], sounds[13], sounds[65], sounds[66], sounds[67]);
        alphabetList.add(a);
        a = new Alphabet("Oo", covers[14], covers[68], covers[69], covers[70], title[68], title[69], title[70], sounds[14], sounds[68], sounds[69], sounds[70]);
        alphabetList.add(a);
        a = new Alphabet("Pp", covers[15], covers[71], covers[72], covers[73], title[71], title[72], title[73], sounds[15], sounds[71], sounds[72], sounds[73]);
        alphabetList.add(a);
        a = new Alphabet("Qq", covers[16], covers[74], covers[75], covers[76], title[74], title[75], title[76], sounds[16], sounds[74], sounds[75], sounds[76]);
        alphabetList.add(a);
        a = new Alphabet("Rr", covers[17], covers[77], covers[78], covers[79], title[77], title[78], title[79], sounds[17], sounds[77], sounds[78], sounds[79]);
        alphabetList.add(a);
        a = new Alphabet("Ss", covers[18], covers[80], covers[81], covers[82], title[80], title[81], title[82], sounds[18], sounds[80], sounds[81], sounds[82]);
        alphabetList.add(a);
        a = new Alphabet("Tt", covers[19], covers[83], covers[84], covers[85], title[83], title[84], title[85], sounds[19], sounds[83], sounds[84], sounds[85]);
        alphabetList.add(a);
        a = new Alphabet("Uu", covers[20], covers[86], covers[87], covers[88], title[86], title[87], title[88], sounds[20], sounds[86], sounds[87], sounds[88]);
        alphabetList.add(a);
        a = new Alphabet("Vv", covers[21], covers[89], covers[90], covers[91], title[89], title[90], title[91], sounds[21], sounds[89], sounds[90], sounds[91]);
        alphabetList.add(a);
        a = new Alphabet("Ww", covers[22], covers[92], covers[93], covers[94], title[92], title[93], title[94], sounds[22], sounds[92], sounds[93], sounds[94]);
        alphabetList.add(a);
        a = new Alphabet("Xx", covers[23], covers[95], covers[96], covers[97], title[95], title[96], title[97], sounds[23], sounds[95], sounds[96], sounds[97]);
        alphabetList.add(a);
        a = new Alphabet("Yy", covers[24], covers[98], covers[99], covers[100], title[98], title[99], title[100], sounds[24], sounds[98], sounds[99], sounds[100]);
        alphabetList.add(a);
        a = new Alphabet("Zz", covers[25], covers[101], covers[102], covers[103], title[101], title[102], title[103], sounds[25], sounds[101], sounds[102], sounds[103]);
        alphabetList.add(a);


        adapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Home");
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * RecyclerView item decoration - give equal margin around grid item
     */
    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
