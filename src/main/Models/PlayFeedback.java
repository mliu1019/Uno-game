package Models;

public class PlayFeedback {
    boolean success = false;
    String message = "";

    public PlayFeedback() {}
    public PlayFeedback(boolean suc, String msg) {
        success = suc;
        message = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
