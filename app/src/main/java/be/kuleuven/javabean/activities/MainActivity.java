package be.kuleuven.javabean.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import be.kuleuven.javabean.R;
import be.kuleuven.javabean.model.CoffeeOrder;

public class MainActivity extends AppCompatActivity {
    private Button btnPlus;
    private Button btnMinus;
    private TextView lblQty;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnPlus = (Button) findViewById(R.id.btnPlus);
        btnMinus = (Button) findViewById(R.id.btnMinus);
        lblQty = (TextView) findViewById(R.id.lblQty);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

//        btnPlus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //SOME ACTIONS
//            }
//        });
//        方法一：在onCreate方法里面创建OnClickListener
//        方法二：通过另写一个public void <method name>(View view)，再在design里面的onClick中，勾选对应的method name
    }

    public void onBtnPlus_Clicked(View caller){
        int quantity = Integer.parseInt(lblQty.getText().toString()) + 1;
//Integer.parseInt(COMPONENT.toString())
        lblQty.setText(Integer.toString(quantity));
        btnSubmit.setEnabled(Integer.parseInt(lblQty.getText().toString())>0);
    }

    public void onBtnMinus_Clicked(View caller){
        int quantity = Integer.parseInt(lblQty.getText().toString()) - 1;
//Integer.parseInt(COMPONENT.toString())
        lblQty.setText(Integer.toString(quantity));
        btnSubmit.setEnabled(Integer.parseInt(lblQty.getText().toString())>0);
    }

    public void onBtnSubmit_Clicked(View caller) {
        EditText txtName = (EditText) findViewById(R.id.txtName);
        Spinner spCoffee = (Spinner) findViewById(R.id.spCoffee);
        CheckBox cbSugar = (CheckBox) findViewById(R.id.cbSugar);
        CheckBox cbWhipCream = (CheckBox) findViewById(R.id.cbWhipCream);

        CoffeeOrder order = new CoffeeOrder(txtName.getText().toString(),
                spCoffee.getSelectedItem().toString(),
                cbSugar.isChecked(),
                cbWhipCream.isChecked(),
                Integer.parseInt(lblQty.getText().toString()));

        Intent intent = new Intent(this,QueueActivity.class);
        intent.putExtra("Order",order);

        startActivity(intent);
//        EditText txtName = (EditText) findViewById(R.id.txtName);
//        Spinner spCoffee = (Spinner) findViewById(R.id.spCoffee);
//        CheckBox cbSugar = (CheckBox) findViewById(R.id.cbSugar);
//        CheckBox cbWhipCream = (CheckBox) findViewById(R.id.cbWhipCream);
//
//        Intent intent = new Intent(this,QueueActivity.class);
//        intent.putExtra("Name", txtName.getText());
//        intent.putExtra("Coffee", spCoffee.getSelectedItem().toString());
//        intent.putExtra("Sugar", cbSugar.isChecked());
//        intent.putExtra("WhipCream", cbWhipCream.isChecked());
//        intent.putExtra("Quantity", Integer.parseInt(lblQty.getText().toString()));
//
//        startActivity(intent);
    }
}