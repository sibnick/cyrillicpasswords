package org.passwdhelper.app;

import android.app.Activity;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getName();

    private EditText password;
    private Button btnSubmit;
    private CheckBox checkBox;

    private static final String ENG = "`1234567890-=qwertyuiop[]\\asdfghjkl;'zxcvbnm,./~!@#$%^&*()_+QWERTYUIOP{}|ASDFGHJKL:\"ZXCVBNM<>?";
    private static final String RUS = "ё1234567890-=йцукенгшщзхъ\\фывапролджэячсмитьбю.Ё!\"№;%:?*()_+ЙЦУКЕНГШЩЗХЪ/ФЫВАПРОЛДЖЭЯЧСМИТЬБЮ,";
    private static final Map<Character, Character> map = new HashMap<Character, Character>();
    static{
        for(int i = 0; i<ENG.length(); i++){
            char eng = ENG.charAt(i);
            char rus = RUS.charAt(i);
            map.put(rus, eng);
        }
    }

    private Timer timer = new Timer();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addListenerOnButton();

    }

    public void addListenerOnButton() {

        password = (EditText) findViewById(R.id.txtPassword);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        checkBox = (CheckBox) findViewById(R.id.btnShow);

        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d(TAG, "copy passwd");
                @SuppressWarnings("deprecation")
                final ClipboardManager cm=(ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                Editable text = password.getText();

                if(text!=null) {
                    final CharSequence passwd = transform(text.toString());
                    cm.setText(passwd);
                    Toast toast = Toast.makeText(MainActivity.this, R.string.passwd_copied, Toast.LENGTH_LONG);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            CharSequence clipboard = cm.getText();
                            if(clipboard!=null && clipboard.equals(passwd)){
                                Log.d(TAG, "Remove password from clipboard");
                                cm.setText("");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast toast = Toast.makeText(MainActivity.this, R.string.passwd_removed, Toast.LENGTH_LONG);
                                        toast.setDuration(Toast.LENGTH_LONG);
                                        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL, 0, 0);
                                        toast.show();
                                    }
                                });
                            }else{
                                Log.d(TAG, "Nothing to remove from clipboard");
                            }
                        }
                    }, 1000 * 60L); //one minute

                }
            }

        });
        checkBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean hiddenFlag = getResources().getString(R.string.btn_hide).equals(checkBox.getText());
                applyHiddenFlag(hiddenFlag);
            }
        });


    }

    private void applyHiddenFlag(boolean hiddenFlag) {
        if (hiddenFlag) {
            checkBox.setText(R.string.btn_show);
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            setFocus();
        } else {
            checkBox.setText(R.string.btn_hide);
            password.setTransformationMethod(null);
            password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            setFocus();
        }
    }

    private void setFocus() {
        password.requestFocus();
        CharSequence txt = password.getText();
        if(txt!=null) {
            password.setSelection(txt.length());
        }
    }

    private CharSequence transform(String s) {
        StringBuilder sb = new StringBuilder();
        for(int i =0; i<s.length(); i++){
            Character cur = s.charAt(i);
            Character transformed = map.get(cur);
            if(transformed==null){
                sb.append(cur.charValue());
            }else{
                sb.append(transformed.charValue());
            }
        }
        return sb.toString();
    }

    public MainActivity() {
        super();
    }



    @Override
    protected void onStop() {
        password.setText("");
        Log.d(TAG, "onStop Remove password from field");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        password.setText("");
        Log.d(TAG, "onDestroy Remove password from field");
        super.onDestroy();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d(TAG, "restore state");
        super.onRestoreInstanceState(savedInstanceState);
        password.setText(savedInstanceState.getCharSequence("tmp.passwd"));
        boolean hiddenFlag = savedInstanceState.getBoolean("tmp.hide");
        applyHiddenFlag(hiddenFlag);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Log.d(TAG, "store state");
        outState.putCharSequence("tmp.passwd", password.getText().toString());
        outState.putBoolean("tmp.hide", !getResources().getString(R.string.btn_hide).equals(checkBox.getText()));
        super.onSaveInstanceState(outState);
    }
}
