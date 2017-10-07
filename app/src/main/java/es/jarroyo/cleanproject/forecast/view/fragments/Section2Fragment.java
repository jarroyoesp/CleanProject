package es.jarroyo.cleanproject.forecast.view.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import butterknife.BindView;
import es.jarroyo.cleanproject.R;
import es.jarroyo.cleanproject.base.ui.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class Section2Fragment extends BaseFragment {
    public static final String TAG = Section2Fragment.class.toString();


    @BindView(R.id.fragment_section2_textview)
    TextView textViewMain;

    public static Section2Fragment newInstance (){
        return new Section2Fragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateView(inflater, container, R.layout.fragment_section2);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setTitle();
    }

    private void setTitle() {
        textViewMain.setText("Section 2");
    }
}
