package com.lrz141319.grocerylistapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class EditToDoItemActivity extends Activity
{
	public int Position = 0;
	EditText m_EditTxt;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_edit_item);

		String editItem = getIntent().getStringExtra("item");
		Position = getIntent().getIntExtra("position",-1);

		m_EditTxt = (EditText)findViewById(R.id.etEditItem);
		m_EditTxt.setText(editItem);
	}

	public void onSubmit(View v)
	{
		m_EditTxt = (EditText) findViewById(R.id.etEditItem);

		Intent data = new Intent();

		data.putExtra("item", m_EditTxt.getText().toString());
		data.putExtra("position", Position);

		setResult(RESULT_OK, data);
		finish();
	} 
}
