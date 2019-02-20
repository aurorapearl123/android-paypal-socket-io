package com.example.ian.paypalpayment;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ian.paypalpayment.Config.Config;
import com.example.ian.paypalpayment.Model.Client;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import org.json.JSONException;

import java.math.BigDecimal;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClientDetailActivity extends AppCompatActivity {

    //TextView txt_email, txt_phone, txt_full_name;
    //ImageView profile_image;

    @BindView(R.id.id_detail_email) TextView _txt_email;
    @BindView(R.id.id_detail_full_name) TextView _txt_full_name;
    @BindView(R.id.id_detail_phone) TextView _txt_phone;
    @BindView(R.id.id_detail_profile_image) ImageView _profile_image;
    @BindView(R.id._id_detail_send_money) TextView _send_money;

    public static final int PAYPAL_REQUEST_CODE = 7171;

    private static PayPalConfiguration config = new PayPalConfiguration()
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX) // use sandbox because we on test
            .clientId(Config.PAYPAL_CLIENT_ID);

    String amount = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_detail);

        //start paypal service
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        //set toolbar back
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setDisplayShowHomeEnabled(false);


        ButterKnife.bind(this);
        Client client = getIntent().getParcelableExtra("client_detail");
        Log.wtf("DETAILS", client.toString()+"");
        //set details
        _txt_email.setText(client.getEmail());
        _txt_phone.setText(client.getPhone());
        _txt_full_name.setText(client.getFirst_name()+" "+client.getMiddle_name()+" "+client.getLast_name());
        String image = "https://media.pitchfork.com/photos/59359f9e3fc4797de0f8d637/2:1/w_790/50668425.jpg";
        Glide.with(getApplicationContext())
                //.load(client.getProfile())
                .load(image)
                .apply(RequestOptions.circleCropTransform())
                .into(_profile_image);

        _send_money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(), "Hello World", Toast.LENGTH_LONG).show();
                sendMoney();
            }
        });

    }

    public void sendMoney()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText amountText = new EditText(getApplicationContext());
        amountText.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setMessage("Enter amount to be send");
        alert.setTitle("Enter Amount");
        alert.setView(amountText);
        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value
                //Editable YouEditTextValue = amountText.getText();
                //OR
                String _amount_text = amountText.getText().toString();
                //Toast.makeText(getApplicationContext(), "the amount"+_amount_text, Toast.LENGTH_LONG).show();
                processPayment(_amount_text);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();
    }
    private void processPayment(String _amount)
    {
        amount = _amount;

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Donate for EDMTDev", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PAYPAL_REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                PaymentConfirmation paymentConfirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if(paymentConfirmation != null) {
                    try{
                        Log.wtf("SEND-AMOUNT", amount+"");
                        String paymentDetails = paymentConfirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, PaymentDetails.class)
                                .putExtra("PaymentDetails", paymentDetails)
                                .putExtra("PaymentAmount", amount)
                        );
                    }catch (JSONException e) {

                    }
                }
            }
            else if(resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show();
            }
        }
        else if(resultCode == PaymentActivity.RESULT_EXTRAS_INVALID){
            Toast.makeText(this, "Invalid", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
