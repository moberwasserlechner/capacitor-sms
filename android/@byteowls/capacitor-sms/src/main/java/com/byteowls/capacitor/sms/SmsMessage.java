package com.byteowls.capacitor.sms;

import com.getcapacitor.plugin.SplashScreen;

import java.util.List;

/**
 * @author m.oberwasserlechner@byteowls.com
 */
public class SmsMessage {

    private List<String> numbers;
    private String text;

    public SmsMessage(List<String> numbers, String text) {
        this.numbers = numbers;
        this.text = text;
    }

    public String getFirstNumber() {
        if (this.numbers != null && this.numbers.size() >= 1) {
            return this.numbers.get(0);
        }
        return null;

    }

    public List<String> getNumbers() {
        return this.numbers;
    }

    public String getText() {
        return text;
    }

    public String getJoinedNumbers(String separator) {
        StringBuilder joined = new StringBuilder();
        for (int i = 0; i < this.numbers.size(); i++) {
            if (i > 0) {
                joined.append(separator);
            }
            joined.append(this.numbers.get(i));
        }
        return joined.toString();
    }
}
