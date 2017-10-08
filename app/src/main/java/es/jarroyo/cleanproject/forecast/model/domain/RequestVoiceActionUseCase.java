package es.jarroyo.cleanproject.forecast.model.domain;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import es.jarroyo.cleanproject.base.RequestError;
import es.jarroyo.cleanproject.base.UseCase;
import es.jarroyo.cleanproject.forecast.source.voiceaction.VoiceActionRequest;
import es.jarroyo.cleanproject.forecast.source.voiceaction.VoiceActionRequestInterface;

import static com.google.common.base.Preconditions.checkNotNull;

public class RequestVoiceActionUseCase extends UseCase<RequestVoiceActionUseCase.RequestValues, RequestVoiceActionUseCase.ResponseValue> {

    private final VoiceActionRequest mRequestVoiceAction;

    public RequestVoiceActionUseCase(@NonNull VoiceActionRequest voiceActionRequest) {
        mRequestVoiceAction = checkNotNull(voiceActionRequest, "voiceActionRequest cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        mRequestVoiceAction.requestVoiceAction(new VoiceActionRequestInterface.LoadDataCallback() {
            @Override
            public void onSuccess(String responseWit) {
                ResponseValue responseValue = new ResponseValue(responseWit);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onError(@Nullable RequestError error) {
                ResponseValue responseValue = new ResponseValue(error);
                getUseCaseCallback().onError(responseValue);
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValues {
        public RequestValues() {
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final RequestError mError;
        private final String mResponseWit;

        public ResponseValue(@NonNull String responseWit) {
            mResponseWit = checkNotNull(responseWit, "DataList cannot be null!");
            mError = null;
        }

        public ResponseValue(RequestError error) {
            mError = error;
            mResponseWit = null;
        }

        public String getmResponseWit() {
            return mResponseWit;
        }

        public RequestError getError() {
            return mError;
        }
    }

}
