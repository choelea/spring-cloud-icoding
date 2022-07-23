package tech.icoding.sci.sdk.common;

public class ErrorCodeAndMessage extends CodeAndMessage {

    public static ErrorCodeAndMessage create(
            int errorCode, String errorMessage) {

        ErrorCodeAndMessage errorCodeAndMessage = new ErrorCodeAndMessage();
        errorCodeAndMessage.setCode(errorCode);
        errorCodeAndMessage.setMessage(errorMessage);

        return errorCodeAndMessage;
    }
}