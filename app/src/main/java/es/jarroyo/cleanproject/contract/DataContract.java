package es.jarroyo.cleanproject.contract;

import java.util.List;

import es.jarroyo.cleanproject.base.BasePresenter;
import es.jarroyo.cleanproject.base.BaseView;
import es.jarroyo.cleanproject.domain.model.Data;

public interface DataContract {

    interface View extends BaseView<Presenter> {
        boolean isActive();
        void showData(List<Data> dynamicActionList);
        void showLoading();
        void hideLoading();
        void onErrorGetData();
    }

    interface Presenter extends BasePresenter {
        void loadData(Double latitude, Double longitud);
    }

}