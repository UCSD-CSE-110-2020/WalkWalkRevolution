package edu.ucsd.cse110.walkwalkrevolution;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
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
        builder.setView(currentView);
        Button bt_confirm = (Button) currentView.findViewById(R.id.bt_confirm);
        // support mocking
        bt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((EditText) currentView.findViewById(R.id.box_height)).getText().toString().matches("")) {
                    Toast.makeText(getActivity(), "Please Enter a Valid Height", Toast.LENGTH_SHORT).show();
                }
                else {
                    saveHeight(currentView, editor);
                    dismiss();
                }
            }
        });
        return builder.create();
    }

    public void saveHeight(View view, SharedPreferences.Editor editor) {
        EditText heightInput = (EditText) view.findViewById(R.id.box_height);
        float heightInputFloat = Float.valueOf(heightInput.getText().toString());

        if (heightInputFloat > 0) {
            editor.putFloat("user_height", heightInputFloat);
            editor.apply();
        }
    }

    //ResetHeight For Testing Purposes
    public void resetHeight(SharedPreferences.Editor editor) {
        editor.putFloat("user_height", -1);
        editor.putBoolean("use_height", true);
        editor.apply();
    }
}
