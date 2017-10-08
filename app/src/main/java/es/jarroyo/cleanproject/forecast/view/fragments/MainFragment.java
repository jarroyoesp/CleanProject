package es.jarroyo.cleanproject.forecast.view.fragments;


import android.location.Location;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;
import java.util.Locale;

import ai.wit.sdk.Wit;
import butterknife.BindView;
import butterknife.OnClick;
import es.jarroyo.cleanproject.R;
import es.jarroyo.cleanproject.base.ui.BaseFragment;
import es.jarroyo.cleanproject.base.ui.BaseNavigationActivity;
import es.jarroyo.cleanproject.contract.DataContract;
import es.jarroyo.cleanproject.forecast.model.domain.model.Data;
import es.jarroyo.cleanproject.forecast.view.adapter.DataWeatherRvAdapter;
import es.jarroyo.cleanproject.utils.DateUtils;
import es.jarroyo.cleanproject.utils.SpeechUtils;

import static android.content.Context.AUDIO_SERVICE;

public class MainFragment extends BaseFragment implements DataContract.View, DataWeatherRvAdapter.OnItemClickListener {
    public static final String TAG = MainFragment.class.toString();

    @BindView(R.id.fragment_main_coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.fragment_main_rv_data)
    RecyclerView mRecyclerViewData;
    LinearLayoutManager linearLayoutManager;

    @BindView(R.id.fragment_main_swipe_refresh)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.fragment_main_progress_loading)
    View mProgressLayout;

    @BindView(R.id.fragment_main_loading_progressbar)
    ProgressBar mProgressBarLoading;

    @BindView(R.id.fragment_main_progress_tv_loading)
    TextView mTextViewLoading;

    @BindView(R.id.fragment_main_layout_error)
    View mLayoutError;

    @BindView(R.id.fragment_main_tv_info)
    TextView mTextViewInfo;

    @BindView(R.id.fragment_list_feed_text_view_date)
    TextView mTextViewDate;

    @BindView(R.id.fragment_main_layout_info)
    View mLayoutInfo;

    DataWeatherRvAdapter mDataWeatherRVAdapter;

    private DataContract.Presenter mPresenter;

    // DATA
    // Coordenadas Zaragoza 41.650606, -0.906176
    // 41.647078, -0.885536
    private Double mLatitude = 41.647078;
    private Double mLongitud = -0.885536;
    private FusedLocationProviderClient mFusedLocationClient;

    // Record
    private boolean isRecording = false;

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
        getLastKnowPosition();
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

    private void getLastKnowPosition() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    mLatitude = location.getLatitude();
                    mLongitud = location.getAltitude();
                }
            }
        });
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
        prepareRecyclerView(dataList);
        readInfoToUser(dataList);
    }

    @Override
    public void showLoading() {
        if (mProgressLayout.getVisibility() == View.GONE) {
            mProgressLayout.setVisibility(View.VISIBLE);
        }
        mLayoutError.setVisibility(View.GONE);
        mLayoutInfo.setVisibility(View.GONE);
    }

    @Override
    public void hideLoading() {
        mSwipeRefreshLayout.setRefreshing(false);
        if (mProgressLayout.getVisibility() == View.VISIBLE) {
            mProgressLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onErrorGetData() {
        mRecyclerViewData.setVisibility(View.GONE);
        mLayoutInfo.setVisibility(View.GONE);
        mLayoutError.setVisibility(View.VISIBLE);
    }

    @Override
    public void requestingVoiceAction() {
        isRecording = true;
        showLoading();
        mRecyclerViewData.setVisibility(View.GONE);
        mTextViewLoading.setText("Listening...\nClick sound button to stop recording");
    }

    @Override
    public void onSuccessRequestVoiceAction() {
        isRecording = false;
        mPresenter.loadData(mLatitude, mLongitud);
    }

    @Override
    public void onErrorRequestVoiceAction() {
        isRecording = false;
        //Show error

    }

    @Override
    public void setPresenter(DataContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private void prepareRecyclerView(List<Data> dataList) {
        mRecyclerViewData.setVisibility(View.VISIBLE);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerViewData.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerViewData.getContext(),
                linearLayoutManager.getOrientation());
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.divider_rv_weather_data));
        mRecyclerViewData.addItemDecoration(dividerItemDecoration);

        mDataWeatherRVAdapter = new DataWeatherRvAdapter(getContext(), dataList, this);
        mRecyclerViewData.setAdapter(mDataWeatherRVAdapter);

        mRecyclerViewData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int firstPosShowed = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
                int lastPos = linearLayoutManager.getItemCount() - 1;
                if (dy != 0) {
                    showDateFirstVisibleItem(firstPosShowed);
                }
            }

            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:

                        // Cuando paramos de scrollear si se esta mostrando la fecha, la ocultamos
                        if (mTextViewDate != null && mTextViewDate.getVisibility() == View.VISIBLE) {
                            animateViewAppersFromUp(mTextViewDate, false, 1000);
                            mTextViewDate.setVisibility(View.GONE);
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        break;
                }

            }
        });
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadForecastData();
            }
        });
    }

    /**
     * ON CLICKS
     *
     * @param data
     * @param position
     */
    @Override
    public void onDataClick(String data, int position) {

    }

    @OnClick(R.id.fragment_main_button_reload)
    public void onClickReload() {
        mPresenter.loadData(mLatitude, mLongitud);
    }

    @OnClick(R.id.main_content_fab_button)
    public void onClickProccessRequestWit() {
        clickRecord();
    }

    private void clickRecord() {
        mLayoutInfo.setVisibility(View.GONE);

        if (isRecording) {
            mPresenter.stopRequestVoiceAction();
        } else {
            mPresenter.startRequestVoiceAction();
        }
    }

    private void loadForecastData() {
        isRecording = false;
        mTextViewLoading.setText("Getting info about forecast...");
        if (mPresenter != null) {
            mPresenter.loadData(mLatitude, mLongitud);
        }
    }


    private void readInfoToUser(List<Data> dataList) {

        mTextToSpeech.speak(SpeechUtils.getTextToSpeech(dataList, 1), TextToSpeech.QUEUE_FLUSH, null);
    }


    /**
     * Anima la vista para mostrar si hay conexión a internet o no.
     *
     * @param show true indica que se va a mostrar el error
     *             false para ocultar el error
     */
    private void animateViewAppersFromUp(View viewToAnimate, boolean show, int startMillDelay) {
        TranslateAnimation anim;

        if (show) {
            anim = new TranslateAnimation(0, 0, -(viewToAnimate.getHeight() * 3), 0);
        } else {
            anim = new TranslateAnimation(0, 0, 0, -(viewToAnimate.getHeight() * 3));
        }
        anim.setDuration(300);
        anim.setFillAfter(true);
        anim.setStartOffset(startMillDelay);
        viewToAnimate.startAnimation(anim);
    }

    /**
     * Muestra la fecha del primer item mostrado en el rv.
     *
     * @param lastPosShowed
     */
    private void showDateFirstVisibleItem(int lastPosShowed) {
        if (lastPosShowed >= 0) {
            boolean hasToAnimate = false;
            if (mTextViewDate.getVisibility() == View.GONE) {
                mTextViewDate.setVisibility(View.VISIBLE);
                hasToAnimate = true;
            }
            if (hasToAnimate) {
                animateViewAppersFromUp(mTextViewDate, true, 0);
            }
            setDateFirstVisibleItem(mTextViewDate, mDataWeatherRVAdapter.getDataList().get(lastPosShowed));
        }
    }

    /**
     * Metodo que pone en el textview la fecha indicada
     *
     * @param textView
     * @param data
     */
    private void setDateFirstVisibleItem(TextView textView, Data data) {
        String str = DateUtils.getDateOnlyMonthString(Long.valueOf(data.getDt()) * 1000);
        textView.setText(str);
    }

}
