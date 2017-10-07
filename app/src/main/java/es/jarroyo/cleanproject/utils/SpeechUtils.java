package es.jarroyo.cleanproject.utils;

import java.util.List;

import es.jarroyo.cleanproject.domain.model.Data;

/**
 * Created by javierarroyo on 6/10/17.
 */

public class SpeechUtils {

    public static String getTextToSpeech(List<Data> dataList, int nextHours){
        StringBuffer textToSpeech= new StringBuffer();

        if (dataList != null && dataList.size() > 0) {
            textToSpeech.append("The weather today ");

            for (int posNextInfo = 0; posNextInfo < nextHours; posNextInfo++) {
                textToSpeech.append(" at " + addHours(dataList.get(posNextInfo)) +" is. ");
                textToSpeech.append(addTemperature(dataList.get(posNextInfo)));
                textToSpeech.append(addWind(dataList.get(posNextInfo)));
                textToSpeech.append(addClouds(dataList.get(posNextInfo)));
            }
        } else {
            textToSpeech.append("Something was wrong.Please try again");
        }
        return textToSpeech.toString();
    }

    private static String addHours(Data data) {
        String hourAndMinute = DateUtils.getHoursAndMinutesFrom(data.getDt()*1000);
        String[] hAndM = hourAndMinute.split(":");
        return hAndM[0]+" hours";
    }

    private static String addTemperature(Data data) {
        String temp = Math.round(data.getMain().getTemp()) + " grades.";
        return temp;
    }

    private static String addWind(Data data) {
        String temp = "The wind has a velocity of " +Math.round(data.getWind().getSpeed())+" meters per second. ";
        return temp;
    }

    private static String addClouds(Data data) {
        String temp = "";
        if (data.getWeather() != null && data.getWeather().get(0) != null) {
            temp = " and is " + data.getWeather().get(0).getDescription();
        }
        return temp;
    }
}
