package com.adpumb.ads.error;

public class ErrorReport {
    private static ErrorReport errorReport = new ErrorReport();

    public static ErrorReport getInstance() {
        return errorReport;
    }

    public void report(FatalAdUnit adUnit) {

    }


}
