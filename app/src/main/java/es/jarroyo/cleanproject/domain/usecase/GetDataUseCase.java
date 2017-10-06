package es.jarroyo.cleanproject.domain.usecase;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import es.jarroyo.cleanproject.base.RequestError;
import es.jarroyo.cleanproject.base.UseCase;
import es.jarroyo.cleanproject.data.source.DataRepository;
import es.jarroyo.cleanproject.data.source.DataSourceInterface;
import es.jarroyo.cleanproject.domain.model.Data;

import static com.google.common.base.Preconditions.checkNotNull;

public class GetDataUseCase extends UseCase<GetDataUseCase.RequestValues, GetDataUseCase.ResponseValue> {

    private final DataRepository mDataRepository;

    public GetDataUseCase(@NonNull DataRepository dataRepository) {
        mDataRepository = checkNotNull(dataRepository, "dataRepository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mDataRepository.getData(new DataSourceInterface.LoadDataCallback() {
            @Override
            public void onSuccess(List<Data> listDynamicActions) {
                ResponseValue responseValue = new ResponseValue(listDynamicActions);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onError(@Nullable RequestError error) {
                ResponseValue responseValue = new ResponseValue(error);
                getUseCaseCallback().onError(responseValue);
            }
        }, getRequestValues().mLatitud, getRequestValues().mLongitud);
    }

    public static final class RequestValues implements UseCase.RequestValues {
        private final Double  mLatitud;
        private final Double mLongitud;
        public RequestValues(Double latitude, Double longitud) {
            mLatitud = latitude;
            mLongitud = longitud;
        }

        public Double getmLatitud() {
            return mLatitud;
        }

        public Double getmLongitud() {
            return mLongitud;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final RequestError mError;
        private final List<Data> mDataList;

        public ResponseValue(@NonNull List<Data> dataList) {
            mDataList = checkNotNull(dataList, "DataList cannot be null!");
            mError = null;
        }

        public ResponseValue(RequestError error) {
            mError = error;
            mDataList = null;
        }

        public List<Data> getTasks() {
            return mDataList;
        }

        public RequestError getError() {
            return mError;
        }
    }

}
