package com.taxi.passenger.utils;

import com.paypal.android.sdk.payments.PayPalConfiguration;

/**
 * Created by techintegrity on 08/07/16.
 */
public class Constants {
    public static final String API_NOT_CONNECTED = "Google API not connected";
    public static final String SOMETHING_WENT_WRONG = "OOPs!!! Something went wrong...";
    public static String PlacesTag = "Google Places Auto Complete";
    public static int REQUEST_CAMERA = 1;
    public static int REQUEST_GALLERY = 2;
    public static final String PUBLISHABLE_KEY = "pk_test_6pRNASCoBOKtIshFeQd4XMUh";
    public static final int VIEW_TYPE_DEFAULT = 1;
    public static final int VIEW_TYPE_LOADER = 2;
    /*Paypall integration variable*/
    public static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_NO_NETWORK;
    // note that these credentials will differ between live & sandbox environments.
    public static final String CONFIG_CLIENT_ID = "AYqm_vX5LIbsdhuZBgkVBHAJ9YR6yA2_3N81R9wZGkjBZPMHDu91uo47fwL7779Bxly6li5vQWfrO0fy";
    public static final int REQUEST_CODE_PAYMENT = 1;
    public static final int REQUEST_CODE_STRIPE = 2;


}
