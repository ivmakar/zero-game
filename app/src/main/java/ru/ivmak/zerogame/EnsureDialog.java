package ru.ivmak.zerogame;

import android.app.Dialog;
import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class EnsureDialog {
    private final Display display;
    private Context context;
    private TextView mTvTitle;
    private TextView mTvOK;
    private Dialog dialog;
    private Window dialogWindow;

    public EnsureDialog(Context context) {
        this.context = context;
        final WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        display = windowManager.getDefaultDisplay();
        dialog = new Dialog(context, R.style.Custom_Dialog_Style);
        dialogWindow = dialog.getWindow();

    }

    public EnsureDialog builder() {
        final View view = LayoutInflater.from(context).inflate(R.layout.dialog_ensure_layout, null, false);
        LinearLayout mLinearDialog = ((LinearLayout) view.findViewById(R.id.linear_dialog));

        mTvTitle = ((TextView) view.findViewById(R.id.tv_title));
        mTvOK = ((TextView) view.findViewById(R.id.tv_ok));
        dialog.setContentView(view);
        mLinearDialog.setLayoutParams(new FrameLayout.LayoutParams(((int) (display.getWidth() * 0.80)), LinearLayout.LayoutParams.WRAP_CONTENT));
        dialogWindow.setGravity(Gravity.CENTER);
        return this;
    }

    public EnsureDialog setGravity(int gravity) {
        dialogWindow.setGravity(gravity);
        return this;
    }

    public EnsureDialog setTitle(String title) {
        if ("".equals(title)) {
            mTvTitle.setText("1000");
        } else {
            mTvTitle.setText(title);
        }
        return this;
    }

    public EnsureDialog setPositiveButton(String text, final View.OnClickListener listener) {
        if ("".equals(text)) {
            mTvOK.setText("Ok");
        } else {
            mTvOK.setText(text);
        }
        mTvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(v);
            }
        });
        return this;
    }

    public EnsureDialog setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
        return this;
    }

    public EnsureDialog dismiss() {
        dialog.dismiss();
        return this;
    }

    public void show() {
        dialog.show();
    }
}
