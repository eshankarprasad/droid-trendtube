package org.trendtube.app.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import org.trendtube.app.R;


public class TTProgressDialog extends Dialog {

	public TTProgressDialog(Context context) {

		super(context, R.style.TTProgressDialog);
		setContentView(R.layout.tt_progessbar_dailog);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void setMessage(CharSequence message) {
		((TextView) findViewById(R.id.message)).setText(message);
	}
	public void setMessage(int messageResId) {
		((TextView) findViewById(R.id.message)).setText(messageResId);
	}
	@Override
	public void show() {
		super.show();
	}

}
