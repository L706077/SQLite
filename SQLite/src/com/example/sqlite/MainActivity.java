package com.example.sqlite;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity {

	TextView txtResult;
	Spinner spnPrefer;
	Cursor cursor;
	private SQLiteDatabase db = null;
	// 設定Table名稱為mytable,設定欄位_id為編號存整數型態(必要有),設定欄位name為名稱存文字型態
	private final static String CREATE_TABLE = "CREATE TABLE mytable(_id INTEGER PRIMARY KEY,name TEXT)";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtResult = (TextView) findViewById(R.id.txtResult);
		spnPrefer = (Spinner) findViewById(R.id.spnPrefer);
		// 建立DB為mydb
		db = openOrCreateDatabase("mydb.db", MODE_PRIVATE, null);
		try {
			db.execSQL(CREATE_TABLE); // 建立資料表
			db.execSQL("INSERT INTO mytable (name) values ('生巧克力的內容')");// 新增內容
			db.execSQL("INSERT INTO mytable (name) values ('果仁巧克力的內容')");
			db.execSQL("INSERT INTO mytable(name) values ('酒心巧克力的內容')");

		} catch (Exception e) {
		}
		cursor = getAll(); // 查詢所有資料
		UpdataAdapter(cursor); // 載入資料表至spinner 中
		// 設定 spnPrefer 元件 ItemSelected 事件的 listener 為 spnPreferListener
		spnPrefer.setOnItemSelectedListener(spnPreferListener);

	}

	protected void onDestroy() {// 離開時
		super.onDestroy();
		db.close(); // 關閉資料庫
	}

	// spinner監聽事件
	private Spinner.OnItemSelectedListener spnPreferListener = new Spinner.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			String text = "";
			if (cursor != null && cursor.getCount() >= 0) {
				cursor.moveToPosition(position);
				// 取得資料表值(_id,name)，要拿_id索引值為0,要拿name索引值為1
				text = cursor.getString(1);
			}
			txtResult.setText("\n介紹：\r\n " + text);

		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
		}
	};

	public void UpdataAdapter(Cursor cursor) {
		if (cursor != null && cursor.getCount() >= 0) {

			SimpleCursorAdapter adapter = new SimpleCursorAdapter(
					this,
					android.R.layout.simple_list_item_1,
					cursor, // 資料庫的 Cursors
							// 物件
					new String[] { "name" }, new int[] { android.R.id.text1 },
					0);
			// 設定 Spinner 顯示的格式
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// 設定 Spinner 的資料來源
			spnPrefer.setAdapter(adapter);
		}
	}

	public Cursor getAll() {
		// 查詢所有資料，SimpleCursorAdapter資料欄位一定要包含_id
		Cursor cursor = db.rawQuery("SELECT * FROM mytable ", null);
		return cursor;
	}
}