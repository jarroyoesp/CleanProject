package es.jarroyo.cleanproject.forecast.source.voiceaction;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import ai.wit.sdk.IWitListener;
import ai.wit.sdk.Wit;
import ai.wit.sdk.model.WitOutcome;

public class VoiceActionRequest implements VoiceActionRequestInterface,IWitListener {

    private Context mContext;
    private Wit _wit;
    public VoiceActionRequest(@NonNull Context context){
        mContext = context;
    }



    /***********************************************************************************************
     * WIT
     **********************************************************************************************/

    private void prepareWit() {
        String accessToken = "59d7d0f4-dd6b-43c6-992e-9a9fd700bde5";
        _wit = new Wit(accessToken, this);
        _wit.enableContextLocation(mContext);
    }

    private void launchWit() {
        try {
            _wit.toggleListening();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void witDidGraspIntent(ArrayList<WitOutcome> arrayList, String s, Error error) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if (error != null) {
            return;
        }
        String jsonOutput = gson.toJson(arrayList);
    }

    @Override
    public void witDidStartListening() {

    }

    @Override
    public void witDidStopListening() {

    }

    @Override
    public void witActivityDetectorStarted() {

    }

    @Override
    public String witGenerateMessageId() {
        return null;
    }

    @Override
    public void requestVoiceAction(@NonNull LoadDataCallback callback) {
        // Debido a un problema con la librería Wit en versiones Android > 6 lo dejamos preparado para
        // integrar pero sin funcionar.
        /*FATAL EXCEPTION: Thread-4
        Process: es.jarroyo.cleanproject, PID: 5212
        java.lang.ArrayIndexOutOfBoundsException: length=9600; index=9600
        at ai.wit.sdk.WitMic$SamplesReaderThread.short2byte(WitMic.java:227)
        at ai.wit.sdk.WitMic$SamplesReaderThread.run(WitMic.java:207)*/

        // Simulamos el comportamiento como si Wit nos devolviera correctamente la respuesta
        /*prepareWit();
        launchWit();*/

        callback.onSuccess("OnSuccess Wit");
    }
}
