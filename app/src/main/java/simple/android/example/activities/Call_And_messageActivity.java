package simple.android.example.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.mahu.example.R;

public class Call_And_messageActivity extends AppCompatActivity {
   // ActivityCallAndMessageBinding binding;
    String sendNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  binding = DataBindingUtil.setContentView(this,R.layout.activity_call__and_message);

        Log.e("TAG","SendNumber"+sendNumber);

        binding.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              sendNumber =  binding.phoneNumber.getText().toString().trim();
                Intent callIntent = new Intent(Intent.ACTION_CALL);

                callIntent.setData(Uri.parse("tel:"+sendNumber));
                Log.e("TAG","SendNumber"+sendNumber);
                if (ActivityCompat.checkSelfPermission(Call_And_messageActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)

                {
                    return;
                }
                startActivity(callIntent);
            }
        });
        binding.message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNumber =  binding.phoneNumber.getText().toString().trim();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", sendNumber, null)));
            }
        });
        binding.whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNumber =  binding.phoneNumber.getText().toString().trim();
                openWhatsApp(sendNumber);
            }
        });*/

    }
    private void openWhatsApp(String smsNumber) {
        Context context = this;

       // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + smsNumber;
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            Toast.makeText(Call_And_messageActivity.this, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

}
