package com.seventh.intelligentguide;

import org.json.JSONException;
import org.json.JSONObject;

import com.seventh.intelligentguide.httphelper.DatabaseHandler;
import com.seventh.intelligentguide.httphelper.UserFunctions;
import com.seventh.intelligentguide.util.ApplictionManage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
/**
 * 用户登录
 * @author rui
 *
 */
public class UserLogin extends Activity {
	private Button btnLogin;// 登录
	private Button btnLinkToRegister;// 注册
	private Button bt_undo;// 取消
	private Button bt_shiyong;// 试用
	private EditText inputEmail;
	private EditText inputPassword;
	private CheckBox cb_remember;// 记住密码
	private TextView loginErrorMsg;
	private SharedPreferences sp;// 存储用户名和密码

	// JSON Response node names
	private static String KEY_SUCCESS = "success";
	private static String KEY_ERROR = "error";
	private static String KEY_ERROR_MSG = "error_msg";
	private static String KEY_UID = "uid";
	private static String KEY_NAME = "name";
	private static String KEY_EMAIL = "email";
	private static String KEY_CREATED_AT = "created_at";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userlogin);
		ApplictionManage.getApplictionManage().addActivity(this);
		// Importing all assets like buttons, text fields
		inputEmail = (EditText) findViewById(R.id.loginEmail);
		inputPassword = (EditText) findViewById(R.id.loginPassword);
		cb_remember = (CheckBox) findViewById(R.id.cb_rememberpwd);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
		bt_undo = (Button) findViewById(R.id.btuUndo);
		bt_shiyong = (Button) findViewById(R.id.btnLinkToTryScreen);
		loginErrorMsg = (TextView) findViewById(R.id.login_error);

		sp = getSharedPreferences("config", MODE_PRIVATE);
		inputEmail.setText(sp.getString("UserNumbser", ""));
		inputPassword.setText(sp.getString("UserPWD", ""));
		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String email = inputEmail.getText().toString();
				String password = inputPassword.getText().toString();
				if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
					Toast.makeText(getApplicationContext(), "帐码或密码不能为空", 0)
							.show();
				} else {
					UserFunctions userFunction = new UserFunctions();
					JSONObject json = userFunction.loginUser(email, password);
					// 检查登录请求
					try {
						if (json.getString(KEY_SUCCESS) != null) {
							loginErrorMsg.setText("");
							String res = json.getString(KEY_SUCCESS);
							if (Integer.parseInt(res) == 1) {
								// 用户登录成功
								if(cb_remember.isChecked()){
									Editor editor=sp.edit();
									editor.putString("UserNumbser", email);
									editor.putString("UserPWD", password);
									editor.commit();
									Toast.makeText(getApplicationContext(), "密码已保存", 0).show();
								}
								// 在SQLite中保存
								DatabaseHandler db = new DatabaseHandler(
										getApplicationContext());
								JSONObject json_user = json
										.getJSONObject("user");
								// 在SQLite中清除
								userFunction
										.logoutUser(getApplicationContext());
								db.addUser(json_user.getString(KEY_NAME),
										json_user.getString(KEY_EMAIL),
										json.getString(KEY_UID),
										json_user.getString(KEY_CREATED_AT));

								Intent dashboard = new Intent(
										getApplicationContext(), Index.class);
								Bundle bundle = new Bundle();
								bundle.putCharSequence("logined", "login");
								dashboard.putExtras(bundle);
								// 关闭所有视图
								dashboard
										.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(dashboard);
								finish();
							} else {
								// 登录错误
								loginErrorMsg.setText("用户名或密码错误");
							}
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		});

		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(),
						UserRegister.class);
				startActivity(i);
				finish();
			}
		});
		
		//取消按钮
		bt_undo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
				System.exit(0);
			}
		});
		
		//试用按钮
		bt_shiyong.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent();
				in.setClass(UserLogin.this, Index.class);
				Bundle bundle=new Bundle();
				bundle.putCharSequence("logined", "shiyong");
				in.putExtras(bundle);
				startActivity(in);
			}
		});
	}
}
