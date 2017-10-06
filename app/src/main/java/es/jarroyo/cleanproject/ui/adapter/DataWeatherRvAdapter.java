package es.jarroyo.cleanproject.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import es.jarroyo.cleanproject.R;
import es.jarroyo.cleanproject.domain.model.Data;
import es.jarroyo.cleanproject.utils.DateUtils;


public class DataWeatherRvAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private OnItemClickListener mOnitemClickListener;
    private List<Data> mDataList = new ArrayList<>();

    int mPositionSelected = -1;

    /**
     * Clickinterface on items
     */
    public interface OnItemClickListener {
        void onMedicationClick(String medication, int position);
    }


    public DataWeatherRvAdapter(Context context, List<Data> dataList, OnItemClickListener onItemClickListener) {
        mContext = context;
        mOnitemClickListener = onItemClickListener;
        mDataList = dataList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_rv_weather_info, null);
        viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        configView(viewHolder, position);
    }

    private void configView(ViewHolder viewHolder, int position) {
        viewHolder.textViewTitleDate.setText(DateUtils.getDateWithMonthString(mDataList.get(position).getDt()*1000));
        viewHolder.textViewTitleTemperature.setText(mDataList.get(position).getMain().getTemp() + " grades");
        viewHolder.textViewTitleWind.setText(mDataList.get(position).getWind().getSpeed() + " velocity");

        if (mDataList.get(position).getWeather() != null && mDataList.get(position).getWeather().get(0) != null) {
            viewHolder.textViewTitlePrecipitation.setText(mDataList.get(position).getWeather().get(0).getDescription());
        }

        if (mPositionSelected == position) {
            viewHolder.mainLayout.setSelected(true);
            viewHolder.ivItemSelected.setVisibility(View.VISIBLE);
            //viewHolder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.light_gray));
        } else {
            viewHolder.mainLayout.setSelected(false);
            viewHolder.ivItemSelected.setVisibility(View.GONE);
            //viewHolder.mainLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        if (mDataList != null) {
            return mDataList.size();
        }
        return 0;
    }


    /**
     * VIEW HOLDER
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected View mainLayout;
        protected TextView textViewTitleDate;
        protected TextView textViewTitleTemperature;
        protected TextView textViewTitleWind;
        protected TextView textViewTitlePrecipitation;
        protected ImageView ivItemSelected;

        public ViewHolder(View view) {
            super(view);
            mainLayout = view.findViewById(R.id.item_rv_weather_info_layout_main);
            textViewTitleDate = (TextView) view.findViewById(R.id.item_rv_weather_info_tv_date);
            textViewTitleTemperature = (TextView) view.findViewById(R.id.item_rv_weather_info_tv_temperature);
            textViewTitleWind = (TextView) view.findViewById(R.id.item_rv_weather_info_tv_wind);
            textViewTitlePrecipitation = (TextView) view.findViewById(R.id.item_rv_weather_info_tv_precipitation);
            ivItemSelected = (ImageView) view.findViewById(R.id.item_rv_weather_info_iv_selected);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() != -1 && getDevicesList() != null && getAdapterPosition() < getDevicesList().size()) {
                mOnitemClickListener.onMedicationClick(mDataList.get(getAdapterPosition()).getMain().getGrndLevel().toString(), getAdapterPosition());

                mPositionSelected = getAdapterPosition();
                v.setSelected(true);
                DataWeatherRvAdapter.this.notifyDataSetChanged();
            }
        }

    }

    public List<Data> getDevicesList() {
        return mDataList;
    }

    public void setList(List<Data> medicationList) {
        this.mDataList = medicationList;
    }

    public int getPositionSelected() {
        return mPositionSelected;
    }
}
