package es.jarroyo.cleanproject.contract;

import android.support.annotation.NonNull;

import java.util.List;

import es.jarroyo.cleanproject.base.UseCase;
import es.jarroyo.cleanproject.base.UseCaseHandler;
import es.jarroyo.cleanproject.forecast.model.domain.model.Data;
import es.jarroyo.cleanproject.forecast.model.domain.usecase.GetDataUseCase;

import static com.google.common.base.Preconditions.checkNotNull;


public class DataPresenter implements DataContract.Presenter {
    private final DataContract.View mDataContractView;
    private final UseCaseHandler mUseCaseHandler;
    private final GetDataUseCase mGetDataUseCase;

    public DataPresenter(@NonNull UseCaseHandler useCaseHandler,
                         @NonNull DataContract.View dataContractView,
                         @NonNull GetDataUseCase getDataUseCase) {
        mUseCaseHandler = checkNotNull(useCaseHandler, "usecaseHandler cannot be null");
        mGetDataUseCase = checkNotNull(getDataUseCase, "getDataUseCase cannot be null!");
        mDataContractView = checkNotNull(dataContractView, "requestDynamicAction cannot be null!");

        mDataContractView.setPresenter(this);
    }




    @Override
    public void start() {

    }

    @Override
    public void loadData(Double latitude, Double longitud) {

        mDataContractView.showLoading();

        mUseCaseHandler.execute(mGetDataUseCase, new GetDataUseCase.RequestValues(latitude, longitud), new UseCase.UseCaseCallback<GetDataUseCase.ResponseValue>(){

            @Override
            public void onSuccess(GetDataUseCase.ResponseValue response) {
                // The view may not be able to handle UI updates anymore
                if (!mDataContractView.isActive()) {
                    return;
                }

                mDataContractView.hideLoading();
                List<Data> dynamicActionList = response.getTasks();
                mDataContractView.showData(dynamicActionList);
            }

            @Override
            public void onError(GetDataUseCase.ResponseValue response) {
                // The view may not be able to handle UI updates anymore
                if (!mDataContractView.isActive()) {
                    return;
                }

                mDataContractView.hideLoading();
                if(response.getError() != null){
                    mDataContractView.onErrorGetData();
                }
            }
        });
    }


}
