package es.jarroyo.cleanproject.forecast.view.adapter;

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
import es.jarroyo.cleanproject.forecast.model.domain.model.Data;
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
        void onDataClick(String data, int position);
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
        //DATE SEPARATOR
        if (showSeparator(position)) {
            viewHolder.textViewTitleDate.setVisibility(View.VISIBLE);
            viewHolder.textViewTitleDate.setText(DateUtils.getDateOnlyMonthString(mDataList.get(position).getDt()*1000));
        } else {
            viewHolder.textViewTitleDate.setVisibility(View.GONE);
        }

        viewHolder.textViewTitleHour.setText(DateUtils.getHoursAndMinutesFrom(mDataList.get(position).getDt()*1000)+"");
        viewHolder.textViewTitleTemperature.setText(Math.round(mDataList.get(position).getMain().getTemp())+"");
        viewHolder.textViewTitleWind.setText(mDataList.get(position).getWind().getSpeed() + " m/s");

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
        protected TextView textViewTitleHour;
        protected TextView textViewTitleTemperature;
        protected TextView textViewTitleWind;
        protected TextView textViewTitlePrecipitation;
        protected ImageView ivItemSelected;

        public ViewHolder(View view) {
            super(view);
            mainLayout = view.findViewById(R.id.item_rv_weather_info_layout_main);
            textViewTitleDate = (TextView) view.findViewById(R.id.item_rv_weather_info_tv_date);
            textViewTitleHour = (TextView) view.findViewById(R.id.item_rv_weather_info_tv_hour);
            textViewTitleTemperature = (TextView) view.findViewById(R.id.item_rv_weather_info_tv_temperature);
            textViewTitleWind = (TextView) view.findViewById(R.id.item_rv_weather_info_tv_wind);
            textViewTitlePrecipitation = (TextView) view.findViewById(R.id.item_rv_weather_info_tv_precipitation);
            ivItemSelected = (ImageView) view.findViewById(R.id.item_rv_weather_info_iv_selected);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() != -1 && getDataList() != null && getAdapterPosition() < getDataList().size()) {
                mOnitemClickListener.onDataClick(mDataList.get(getAdapterPosition()).getMain().getGrndLevel().toString(), getAdapterPosition());

                mPositionSelected = getAdapterPosition();
                v.setSelected(true);
                DataWeatherRvAdapter.this.notifyDataSetChanged();
            }
        }

    }

    public List<Data> getDataList() {
        return mDataList;
    }

    public void setList(List<Data> dataList) {
        this.mDataList = dataList;
    }

    public int getPositionSelected() {
        return mPositionSelected;
    }

    public boolean showSeparator(int currentPosition){
        int nextPosition = currentPosition + 1;

        //Comparamos entre la fecha actual y la siguiente
        if(currentPosition >= 1
                && mDataList.size() > 0
                && mDataList.size() > nextPosition
                && mDataList.get(nextPosition) != null) {
            return DateUtils.isCurrentDateInOtherDayThanBefore(mDataList.get(currentPosition).getDt().toString(), mDataList.get(currentPosition -1).getDt().toString());
        }
        // Si es la ultima posicion mostramos el separador tambien para ver la fecha
        else if (currentPosition == 0){
            return true;
        } else {
            return false;
        }
    }
}
