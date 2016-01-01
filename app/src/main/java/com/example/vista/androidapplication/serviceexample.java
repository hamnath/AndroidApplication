package com.example.vista.androidapplication;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by vista on 3/20/15.
 */
public class serviceexample extends Activity {

    private static final String NAMESPACE = "com.service.ServiceImpl";
    private static final String URL = "http://192.168.202.124:9000/AndroidWS/wsdl/ServiceImpl.wsdl";
    private static final String SOAP_ACTION = "ServiceImpl";
    private static final String METHOD_NAME = "message";

    private static final String[] sampleACTV = new String[] {
            "android", "iphone", "blackberry"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.serviceexample);

        ArrayAdapter<String> arrAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_dropdown_item_1line, sampleACTV);

        AutoCompleteTextView ACTV = (AutoCompleteTextView)findViewById
                (R.id.AutoCompleteTextView01);
        ACTV.setAdapter(arrAdapter);

        SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
        SoapSerializationEnvelope envelope =
                new SoapSerializationEnvelope(SoapEnvelope.VER11);

        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);

        try {
            androidHttpTransport.call(SOAP_ACTION, envelope);
            SoapObject resultsRequestSOAP = (SoapObject) envelope.bodyIn;
            ACTV.setHint("Received :" + resultsRequestSOAP.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
