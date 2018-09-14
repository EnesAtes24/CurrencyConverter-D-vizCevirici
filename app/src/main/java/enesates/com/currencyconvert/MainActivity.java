package enesates.com.currencyconvert;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView tryText;
    TextView usdText;
    TextView jpyText;
    TextView chfText;
    TextView cadText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tryText = findViewById(R.id.tryText);
        usdText = findViewById(R.id.usdText);
        jpyText = findViewById(R.id.jpyText);
        chfText = findViewById(R.id.chfText);
        cadText = findViewById(R.id.cadText);

    }

    public void getRates(View view) {

        DownloadData downloadData = new DownloadData();

        try{

            String url = "http://data.fixer.io/api/latest?access_key=3b89bc048484866d0d54f23fef0a6e8a&format=1";
            downloadData.execute(url);

        } catch(Exception e) {

        }

    }

    private class DownloadData extends AsyncTask<String, Void, String> {
        // AsyncTask 3 tane değer istiyor bizden
        // 1. si Params yani biz String olarak bir url vericez.
        // 2. si Progres yani biz download yaparken bir progres bar(ilerleme barı) olacak ama biz az bir dosya indirdiğimiz için boş bıraktık
        // 3. sü Result yani ben sana sonuç olarak, cevap olarak ne vereyim diyor onu da String seçtik çünkü döviz cevapları string.
        // Async serkonize olmayan bir şekilde çalışıyor demek. Çünkü bu işlem yapılırken app'i kitlemesin diye arka planda yapıyoruz asekron olarak.

        @Override
        protected String doInBackground(String... strings) {
            // Burada AsyncTask class'ındaki ilk String'i yani url'yi vericez o da bize Result(sonuç) olarak dönen Stringi aşağıdaki onPostExecute metoduna gönderecek.
            // doInBackground(String... strings) aslında bir String dizisi demek ve Params olarak veya varads diyede geçer.
            String result = ""; // Download yaparak aldığımız tüm verileri bu Stringin içine koyucaz.
            URL url;
            HttpURLConnection httpURLConnection; // Bizim url adresimiz Http olduğu için bunu verdik Https olsaydı s'li olanı yazacaktık.

            try {

                url = new URL(strings[0]);
                httpURLConnection = (HttpURLConnection) url.openConnection(); // Bağlantıyı açtık.
                InputStream inputStream = httpURLConnection.getInputStream(); // InputStream kullanılarak gelen cevapları okumaya çalışıcaz.
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

                int data = inputStreamReader.read(); // inputStreamReader.read(); bize bir integer veriyor.

                while(data > 0) { // data > 0 dan olduğu sürece bizim hala alacak datamız var demektir.

                    char character = (char) data; // data'yı cast(çevirmek) ederek karakter halinde tek tek alıcaz.
                    result += character; // Aldığımız her değeri result stringine  tek tek aktarıp kaydettik.

                    data = inputStreamReader.read(); // Bir sonraki karaktere geç demek.

                }

                return result;

            } catch (Exception e) {
                return null; // try-catch yazdık çünkü url hatalı olursa app çkmesin diye ve return null; 'ı da catch'in içine yazdık.
            }


        }

        @Override
        protected void onPostExecute(String s) {
            // Bu metodu biz Androis Studio'nun Code kısmının Override bölümünden  manuel olarak ekledik.
            // Bu metod yukarıdaki doInBackground metodunun işlemi bittikten sonra ne olsun onu yazıcaz. Dönen String değerini alacak
            super.onPostExecute(s);

            //System.out.println("Alınan data : " +s);

            try{
                // Kullandığımız JSON Api'ı eğer { } parantezler arasındaysa JSONObject [ ] arasındaysa JSONArray kullanılır.
                // Api kodlarına göre adım adım ilerlerdik önce ana Json'i aldık sonra rates'i aldık sonra TRY, USD vs aldık.

                JSONObject jsonObject = new JSONObject(s);
                String base = jsonObject.getString("base"); // getString'in içinde yazan strind değeri Api'daki ile aynı olmak zorunda.
                //System.out.println("base:" + base);

                String rates = jsonObject.getString("rates"); // getString'in içinde yazan strind değeri Api'daki ile aynı olmak zorunda.

                JSONObject jsonObject1 = new JSONObject(rates);
                String turkishLira = jsonObject1.getString("TRY"); // getString'in içinde yazan strind değeri Api'daki ile aynı olmak zorunda.
                tryText.setText("TRY : " + turkishLira);

                String usd = jsonObject1.getString("USD"); // getString'in içinde yazan strind değeri Api'daki ile aynı olmak zorunda.
                usdText.setText("USD : " + usd);

                String cad = jsonObject1.getString("CAD"); // getString'in içinde yazan strind değeri Api'daki ile aynı olmak zorunda.
                cadText.setText("CAD : " + cad);

                String chf = jsonObject1.getString("CHF"); // getString'in içinde yazan strind değeri Api'daki ile aynı olmak zorunda.
                chfText.setText("CHF : " + chf);

                String jpy = jsonObject1.getString("JPY"); // getString'in içinde yazan strind değeri Api'daki ile aynı olmak zorunda.
                jpyText.setText("JPY : " + jpy);

            } catch (Exception e) {

            }

        }
    }

}
