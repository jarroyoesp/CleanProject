package es.jarroyo.cleanproject.base;

/**
 * Created by javierarroyo on 27/6/17.
 */

public class RequestError {
    public static final int ERROR_NO_INTERNET = 1000;

    String errorMessage;
    int codeError;

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getCodeError() {
        return codeError;
    }

    public void setCodeError(int codeError) {
        this.codeError = codeError;
    }
}
