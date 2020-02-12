package edu.ucsd.cse110.walkwalkrevolution;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class HeightPromptDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        SharedPreferences savedHeightPref = getContext().getSharedPreferences("saved_height", getContext().MODE_PRIVATE);
        SharedPreferences.Editor editor = savedHeightPref.edit();

        View currentView = inflater.inflate(R.layout.dialog_height_prompt, null);
        builder.setView(currentView)
                .setPositiveButton(R.string.submit, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        saveHeight(currentView, editor);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cancelHeightInput(editor);
                    }
                });
        return builder.create();
    }

    public void saveHeight(View view, SharedPreferences.Editor editor) {
        EditText heightInput = (EditText) view.findViewById(R.id.height);
        float heightInputFloat = Float.valueOf(heightInput.getText().toString());

        if (heightInputFloat > 0) {
            editor.putFloat("user_height", heightInputFloat);
            editor.apply();
        }
    }

    public void cancelHeightInput(SharedPreferences.Editor editor) {
        editor.putBoolean("use_height", false);
        editor.apply();
        HeightPromptDialog.this.getDialog().cancel();
    }

    //ResetHeight For Testing Purposes
    public void resetHeight(SharedPreferences.Editor editor) {
        editor.putFloat("user_height", -1);
        editor.putBoolean("use_height", true);
        editor.apply();
    }
}
