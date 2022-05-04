package be.kuleuven.javabean.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import be.kuleuven.javabean.R;
import be.kuleuven.javabean.model.CoffeeOrder;

public class QueueActivity extends AppCompatActivity {
    private TextView txtInfo;
    private RequestQueue requestQueue;
    private static final String SUBMIT_URL = "https://studev.groept.be/api/ptdemo/order/";
    private static final String QUEUE_URL = "https://studev.groept.be/api/ptdemo/queue";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue);

        txtInfo = (TextView) findViewById(R.id.txtInfo);
        requestQueue = Volley.newRequestQueue(this);

//        Bundle extras = getIntent().getExtras();
//        String toppings = (extras.getBoolean("Sugar") ? "+sugar" : "") +
//                (extras.getBoolean("WhipCream") ? "+cream" : "");
//        String requestURL = SUBMIT_URL + extras.get("Name") + "/" +
//                extras.get("Coffee") + "/" +
//                ((toppings.length() == 0) ? "-" : toppings) + "/" +
//        //(STRING.length() == 0)?用作条件，如果要往库里输入的是null，那么需要在对应的位置输入一个“-”
//                extras.get("Quantity");
//        这些是原本的老方法

        CoffeeOrder order = (CoffeeOrder) getIntent().getExtras().getParcelable("Order");
//        String toppings = (order.hasSugar() ? "+sugar" : "") +
//                (order.hasWhipCream() ? "+cream" : "");
//        String requestURL = SUBMIT_URL + order.getName() + "/" +
//                order.getCoffee() + "/" +
//                ((toppings.length() == 0) ? "-" : toppings) + "/" +
//                //(STRING.length() == 0)?用作条件，如果要往库里输入的是null，那么需要在对应的位置输入一个“-”
//                order.getQuantity();
//        //因为对于一个url,我们需要的格式就是：NAME/COFFEE/TOPPINGS/QUANTITY，对于String requestURL的第三次升级如下：
          //在CoffeeOrder里面增加一个public String getRequestURL()
        String requestURL = SUBMIT_URL + order.getRequestURL();
        //以上是创建新class并进行分类合集后的新方法

//一个JsonArrayRequest需要（request方法，URL，（可空的）jsonRequest，Response.Listener, (可空的)Response.ErrorListener）
        JsonArrayRequest queueRequest = new JsonArrayRequest(Request.Method.GET, QUEUE_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                showOrderInfo(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QueueActivity.this, "Unable to communicate with the server", Toast.LENGTH_LONG).show();
            }
        });

//一个StringRequest需要（request方法，URL，Response.Listener, (可空的)Response.ErrorListener）
        StringRequest submitRequest = new StringRequest(Request.Method.GET, requestURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(QueueActivity.this, "Order placed", Toast.LENGTH_SHORT).show();
                requestQueue.add(queueRequest);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QueueActivity.this, "Unable to place the order", Toast.LENGTH_LONG).show();
            }
        });
//到目前为止，只是生成了Request，还没有发送给server

        requestQueue.add(submitRequest);
//send the submitRequest and starts the execution of this Activity
    }

    private void showOrderInfo(JSONArray response) {
        String info = "";
        for (int i = 0; i < response.length(); ++i) {
            JSONObject o = null;
            try {
                o = response.getJSONObject(i);
//                        info += o.get("customer") + ": " + o.get("coffee") + " x " + o.get("quantity") + " " +
//                                o.get("toppings") + " will be ready at " + o.get("date_due") + "\n";
//                        在CoffeeOrder引入第三个constructor以后，我们有了新的方法,如下两行
                CoffeeOrder order = new CoffeeOrder(o);
                info += order.toString() + "\n" + "will be ready at " + o.get("date_due") + "\n";
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        txtInfo.setText(info);
    }

}



//        Bundle extras = getIntent().getExtras();
//
//        String coffeeData = extras.get("Name") + ": " +
//                extras.get("Quantity") + "x " +
//                extras.get("Coffee") +
//                (extras.getBoolean("Sugar") ? " + sugar" : "") +
//                (extras.getBoolean("WhipCream") ? " + whip cream" : "") + "\n";
//        txtInfo.setText(coffeeData);
