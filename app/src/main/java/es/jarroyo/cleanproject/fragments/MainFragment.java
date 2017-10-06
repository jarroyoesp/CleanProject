package es.jarroyo.cleanproject.fragments;


import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ai.wit.sdk.IWitListener;
import ai.wit.sdk.Wit;
import ai.wit.sdk.model.WitOutcome;
import butterknife.BindView;
import butterknife.OnClick;
import es.jarroyo.cleanproject.R;
import es.jarroyo.cleanproject.base.ui.BaseFragment;
import es.jarroyo.cleanproject.base.ui.BaseNavigationActivity;
import es.jarroyo.cleanproject.contract.DataContract;
import es.jarroyo.cleanproject.domain.model.Data;
import es.jarroyo.cleanproject.ui.adapter.DataWeatherRvAdapter;
import es.jarroyo.cleanproject.utils.SpeechUtils;

import static android.content.Context.AUDIO_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends BaseFragment implements DataContract.View, DataWeatherRvAdapter.OnItemClickListener, IWitListener {
    public static final String TAG = MainFragment.class.toString();

    @BindView(R.id.fragment_main_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.fragment_main_rv_data)
    RecyclerView mRecyclerViewData;

    @BindView(R.id.fragment_main_progress_loading)
    ProgressBar mProgressLoading;

    @BindView(R.id.fragment_main_layout_error)
    View mLayoutError;

    @BindView(R.id.fragment_main_tv_info)
    TextView mTextViewInfo;

    DataWeatherRvAdapter mDataWeatherRVAdapter;

    private DataContract.Presenter mPresenter;

    // DATA
    // Coordenadas Zaragoza 41.650606, -0.906176
    private Double mLatitude = 41.650606;
    private Double mLongitud = -0.906176;

    // Speech
    TextToSpeech mTextToSpeech;

    // WIT
    Wit _wit;


    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflateView(inflater, container, R.layout.fragment_main);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        checkVolumeLevel();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*if (mPresenter != null) {
            mPresenter.loadData(mLatitude, mLongitud);
        }*/
        //prepareWit();
    }

    @Override
    public void onResume() {
        super.onResume();
        initTextToSpeech();
    }

    public void onPause() {
        if (mTextToSpeech != null) {
            mTextToSpeech.stop();
            mTextToSpeech.shutdown();
        }
        super.onPause();
    }

    private void initTextToSpeech() {
        mTextToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    mTextToSpeech.setLanguage(Locale.UK);
                }
            }
        });
    }

    private void checkVolumeLevel() {
        AudioManager am = (AudioManager) getActivity().getSystemService(AUDIO_SERVICE);
        int volumelevel = am.getStreamVolume(AudioManager.STREAM_MUSIC);

        if (volumelevel == 0) {
            if (getActivity() instanceof BaseNavigationActivity) {
                ((BaseNavigationActivity) getActivity()).showErrorSnackBar(mCoordinatorLayout, getString(R.string.error_volume));
            }
        }
    }


    /***********************************************************************************************
     /* PRESENTER METHODS
     /*********************************************************************************************/
    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showData(List<Data> dataList) {
        Toast.makeText(getContext(), "SHOW DATA", Toast.LENGTH_SHORT).show();
        prepareRecyclerView(dataList);
        readInfoToUser(dataList);
    }

    @Override
    public void showLoading() {
        if (!mProgressLoading.isShown()) {
            mProgressLoading.setVisibility(View.VISIBLE);
        }
        mLayoutError.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        if (mProgressLoading.isShown()) {
            mProgressLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onErrorGetData() {
        Toast.makeText(getContext(), "ON ERROR", Toast.LENGTH_SHORT).show();
        mLayoutError.setVisibility(View.VISIBLE);
    }

    @Override
    public void setPresenter(DataContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void prepareRecyclerView(List<Data> dataList) {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewData.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerViewData.getContext(),
                linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_rv_weather_data));
        mRecyclerViewData.addItemDecoration(dividerItemDecoration);

        mDataWeatherRVAdapter = new DataWeatherRvAdapter(getContext(), dataList, this);
        mRecyclerViewData.setAdapter(mDataWeatherRVAdapter);
    }

    /**
     * ON CLICKS
     *
     * @param medication
     * @param position
     */
    @Override
    public void onMedicationClick(String medication, int position) {

    }

    @OnClick(R.id.fragment_main_button_reload)
    public void onClickReload() {
        mPresenter.loadData(mLatitude, mLongitud);
    }

    @OnClick(R.id.main_content_fab_button)
    public void onClickProccessRequestWit() {
        try {
            _wit.toggleListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void readInfoToUser(List<Data> dataList) {

        mTextToSpeech.speak(SpeechUtils.getTextToSpeech(dataList, 1), TextToSpeech.QUEUE_FLUSH, null);
    }

    /***********************************************************************************************
     * WIT
     **********************************************************************************************/

    private void prepareWit() {
        String accessToken = "59d7d0f4-dd6b-43c6-992e-9a9fd700bde5";
        _wit = new Wit(accessToken, this);
        _wit.enableContextLocation(getContext());
    }

    @Override
    public void witDidGraspIntent(ArrayList<WitOutcome> witOutcomes, String s, Error error) {
        mTextViewInfo.setVisibility(View.VISIBLE);
        mTextViewInfo.setMovementMethod(new ScrollingMovementMethod());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (error != null) {
            mTextViewInfo.setText(error.getLocalizedMessage());
            return ;
        }
        String jsonOutput = gson.toJson(witOutcomes);
        mTextViewInfo.setText(jsonOutput);
        //mTextViewInfo.setText("Done!");
    }

    @Override
    public void witDidStartListening() {
        mTextViewInfo.setText("Witting...");
    }

    @Override
    public void witDidStopListening() {
        mTextViewInfo.setText("Processing...");    }

    @Override
    public void witActivityDetectorStarted() {
        mTextViewInfo.setText("Listening");
    }

    @Override
    public String witGenerateMessageId() {
        return null;
    }
}
