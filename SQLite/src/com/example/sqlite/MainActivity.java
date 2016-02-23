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
	// �]�wTable�W�٬�mytable,�]�w���_id���s���s��ƫ��A(���n��),�]�w���name���W�٦s��r���A
	private final static String CREATE_TABLE = "CREATE TABLE mytable(_id INTEGER PRIMARY KEY,name TEXT)";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtResult = (TextView) findViewById(R.id.txtResult);
		spnPrefer = (Spinner) findViewById(R.id.spnPrefer);
		// �إ�DB��mydb
		db = openOrCreateDatabase("mydb.db", MODE_PRIVATE, null);
		try {
			db.execSQL(CREATE_TABLE); // �إ߸�ƪ�
			db.execSQL("INSERT INTO mytable (name) values ('�ͥ��J�O�����e')");// �s�W���e
			db.execSQL("INSERT INTO mytable (name) values ('�G�����J�O�����e')");
			db.execSQL("INSERT INTO mytable(name) values ('�s�ߥ��J�O�����e')");

		} catch (Exception e) {
		}
		cursor = getAll(); // �d�ߩҦ����
		UpdataAdapter(cursor); // ���J��ƪ��spinner ��
		// �]�w spnPrefer ���� ItemSelected �ƥ� listener �� spnPreferListener
		spnPrefer.setOnItemSelectedListener(spnPreferListener);

	}

	protected void onDestroy() {// ���}��
		super.onDestroy();
		db.close(); // ������Ʈw
	}

	// spinner��ť�ƥ�
	private Spinner.OnItemSelectedListener spnPreferListener = new Spinner.OnItemSelectedListener() {
		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			String text = "";
			if (cursor != null && cursor.getCount() >= 0) {
				cursor.moveToPosition(position);
				// ���o��ƪ��(_id,name)�A�n��_id���ޭȬ�0,�n��name���ޭȬ�1
				text = cursor.getString(1);
			}
			txtResult.setText("\n���СG\r\n " + text);

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
					cursor, // ��Ʈw�� Cursors
							// ����
					new String[] { "name" }, new int[] { android.R.id.text1 },
					0);
			// �]�w Spinner ��ܪ��榡
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

			// �]�w Spinner ����ƨӷ�
			spnPrefer.setAdapter(adapter);
		}
	}

	public Cursor getAll() {
		// �d�ߩҦ���ơASimpleCursorAdapter������@�w�n�]�t_id
		Cursor cursor = db.rawQuery("SELECT * FROM mytable ", null);
		return cursor;
	}
}