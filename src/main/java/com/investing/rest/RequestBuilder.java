package com.investing.rest;

import java.time.LocalDate;

public class RequestBuilder {

    private RequestBuilder() {}

    public static String getPeriodAsString(String period) {
        switch (period) {
            case "MONTHLY": {
                return "from=" + LocalDate.now().minusMonths(1) + "&till=" + LocalDate.now() + "&interval=24";
            }
            case "WEEKLY": {
                return "from=" + LocalDate.now().minusWeeks(1) + "&till=" + LocalDate.now() + "&interval=24";
            }
            case "YEARLY": {
                return "from=" + LocalDate.now().minusYears(1) + "&till=" + LocalDate.now() + "&interval=24";
            }
            default: return null;
        }

    }
}
