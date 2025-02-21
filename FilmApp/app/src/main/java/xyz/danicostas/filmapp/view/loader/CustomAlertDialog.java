package xyz.danicostas.filmapp.view.loader;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import xyz.danicostas.filmapp.R;

public class CustomAlertDialog extends AlertDialog {

    private Context context;
    private LayoutInflater inflater;
    private View dialogView;
    private AlertDialog progressDialog;
    TextView tvTextLoader;

    public CustomAlertDialog(Context context, String text) {
        super(context);
        this.context = context;
        inflater = LayoutInflater.from(context);
        dialogView = inflater.inflate(R.layout.progress_dialog, null);
        tvTextLoader = dialogView.findViewById(R.id.tvTextLoader);
        tvTextLoader.setText(text);
        progressDialog = new AlertDialog.Builder(context)
                .setView(dialogView)
                .create();
    }

    public void setText(String text) {
        dialogView.postDelayed(() -> this.tvTextLoader.setText(text), 1000);
    }

    @Override
   public void show() {
       progressDialog.show();
       progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
   }

   public void dismiss(Long time) {
       dialogView.postDelayed(() -> progressDialog.dismiss(), time);
   }
}
