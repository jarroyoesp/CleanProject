package es.jarroyo.cleanproject.contract;

import java.util.List;

import es.jarroyo.cleanproject.base.BasePresenter;
import es.jarroyo.cleanproject.base.BaseView;
import es.jarroyo.cleanproject.forecast.model.domain.model.Data;

public interface DataContract {

    interface View extends BaseView<Presenter> {
        boolean isActive();
        void showData(List<Data> dynamicActionList);
        void showLoading();
        void hideLoading();
        void onErrorGetData();

        void requestingVoiceAction();
        void onSuccessRequestVoiceAction();
        void onErrorRequestVoiceAction();
    }

    interface Presenter extends BasePresenter {
        void startRequestVoiceAction();
        void stopRequestVoiceAction();
        void loadData(Double latitude, Double longitud);
    }

}