package com.xinle.lottery.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.xinle.lottery.R;
import com.xinle.lottery.component.ClipImageView;
import com.xinle.lottery.material.SaveImageUtils;
import com.xinle.lottery.util.RGBLuminanceSource;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Hashtable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ACE-PC on 2016/12/8.
 */

public class QRCodeFragment extends Activity {
    private static final String TAG = QRCodeFragment.class.getSimpleName();
    @BindView(R.id.web_html)
    WebView webHtml;
    @BindView(R.id.image)
    ClipImageView imageView;
    @BindView(R.id.qrcode)
    LinearLayout qrcodeLayout;
    @BindView(R.id.recharge_orders)
    TextView rechargeOrders;
    @BindView(R.id.recharge_payamt)
    TextView rechargePayamt;
    @BindView(R.id.qrcode_tip)
    TextView qrcodeTip;


    private String html;
    private String time;
    private String payamt;
    private String type;
    private GoogleApiClient client;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.qrcode_fragment);
        ButterKnife.bind(this);

        html = getIntent().getStringExtra("html");
        payamt = getIntent().getStringExtra("payamt");
        type = getIntent().getStringExtra("type");
        if (html.isEmpty() || payamt.isEmpty() || type.isEmpty()) {
            return;
        }

        ImageButton backhome = (ImageButton) findViewById(android.R.id.home);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView titleText = (TextView) findViewById(android.R.id.title);
        titleText.setText("二维码扫描");

        Document doc = Jsoup.parse(html, "UTF-8");
        String srcjpg = "";
        String[] codehtml = null;
        if (type.equals("wechat")) {
            codehtml = html.split(",");
            if (codehtml[0].equals("OK")) {
                srcjpg = codehtml[2];
            }
        } else if (type.equals("alipay")) {
            Elements jpgs = doc.getElementsByAttributeValue("alt", "二维码");
            for (Element jpg : jpgs) {
                srcjpg = jpg.attr("src");
            }
        }

        if (srcjpg.isEmpty()) {
            webHtml.setVisibility(View.VISIBLE);
            qrcodeLayout.setVisibility(View.GONE);
            WebSettings webSettings = webHtml.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webHtml.loadData(html, "text/html", "utf-8");
            webHtml.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        } else {
            webHtml.setVisibility(View.GONE);
            qrcodeLayout.setVisibility(View.VISIBLE);
            String msg = this.getResources().getString(R.string.is_recharge_orders);
            String payamtStr = this.getResources().getString(R.string.is_recharge_payamt);
            if (type.equals("wechat")) {
                if (codehtml != null || codehtml.length > 0) {
                    msg = StringUtils.replaceEach(msg, new String[]{"ORDERS"}, new String[]{codehtml[1]});
                    payamtStr = StringUtils.replaceEach(payamtStr, new String[]{"PAYAMT"}, new String[]{payamt});
                }
            } else if (type.equals("alipay")) {
                Element orders = doc.getElementById("orderId");
                msg = StringUtils.replaceEach(msg, new String[]{"ORDERS"}, new String[]{orders.attr("value")});
                payamtStr = StringUtils.replaceEach(payamtStr, new String[]{"PAYAMT"}, new String[]{payamt});
            }

            rechargePayamt.setText(Html.fromHtml(payamtStr));
            rechargeOrders.setText(Html.fromHtml(msg));
            qrcodeTip.setText(type.equals("alipay") ? "长按二维码跳转到支付宝" : "截屏后，打开微信的扫一扫，在相册中选取截屏的图片即可支付。");
            scanner(srcjpg);
        }
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void scanner(String imageStr) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        ImageRequest imageRequest = new ImageRequest(imageStr,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        if(response!=null){
                            imageView.setImageBitmap(response);
                        }
                    }
                }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.default_image));
            }
        }
        );
        mQueue.add(imageRequest);
        imageView.setDrawingCacheEnabled(true);
        imageView.setOnCutListener(new ClipImageView.OnCutListener() {
            @Override
            public void OnCutImage(Bitmap bitmap) {
                saveCurrentImage(bitmap);
            }
        });
    }

    //这种方法状态栏是空白，显示不了状态栏的信息
    private void saveCurrentImage(Bitmap bitmap) {
        String path=SaveImageUtils.saveImageToGallerys(getApplicationContext(),bitmap);
        View view = getWindow().getDecorView().getRootView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final Result result = parseQRcodeBitmap(path);
                    runOnUiThread(() -> {
                        if (null != result) {
                            Toast.makeText(QRCodeFragment.this, "二维码识别成功", Toast.LENGTH_LONG).show();
                            Uri uri = Uri.parse(result.toString());
                            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent);
                        } else {
                            Toast.makeText(QRCodeFragment.this, "无法识别", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).start();
            //禁用DrawingCahce否则会影响性能 ,而且不禁止会导致每次截图到保存的是缓存的位图
            view.setDrawingCacheEnabled(false);
        }
    }

    //解析二维码图片,返回结果封装在Result对象中
    private com.google.zxing.Result parseQRcodeBitmap(String bitmapPath) {
        Hashtable<DecodeHintType, String> hints = new Hashtable<>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath, options);
        options.inSampleSize = options.outHeight / 400;
        if (options.inSampleSize <= 0) {
            options.inSampleSize = 1;
        }
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(bitmapPath, options);
        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(bitmap);
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
        QRCodeReader reader = new QRCodeReader();
        Result result = null;
        try {
            result = reader.decode(binaryBitmap, hints);
        } catch (Exception e) {
        }
        return result;
    }

    @OnClick({android.R.id.home, R.id.qrcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.qrcode:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webHtml != null) {
            webHtml.onResume();
        }
    }

    @Override
    protected void onPause() {
        if (webHtml != null) {
            webHtml.onPause();
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (webHtml != null) {
            webHtml.destroy();
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Log.i(TAG, scanResult);
        }
    }

    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("QRCodeFragment Page")
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

}
