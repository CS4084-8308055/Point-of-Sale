package ie.ul.davidbeck.pointofsale;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private TextView mNameTextView, mQuantityTextView, mDateTextView;
    private Item mCurrentItem;
    private Item mClearedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNameTextView = findViewById(R.id.name_text);
        mQuantityTextView = findViewById(R.id.quantity_text);
        mDateTextView = findViewById(R.id.date_text);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
             }
        });
    }

    private void addItem() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Customise dialog
        //builder.setMessage("Hello");
        //builder.setPositiveButton("OK", null);

        View view = getLayoutInflater().inflate(R.layout.dialog_add, null, false);
        builder.setView(view);
        final EditText nameEditText = view.findViewById(R.id.edit_name);
        final EditText quantityEditText = view.findViewById(R.id.edit_quantity);
        final CalendarView deliveryDateView = view.findViewById(R.id.calendar_view);
        final GregorianCalendar calendar = new GregorianCalendar();

        deliveryDateView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
            }
        });

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = nameEditText.getText().toString();
                int quantity = Integer.parseInt(quantityEditText.getText().toString());
                mCurrentItem = new Item(name, quantity, calendar);
                showCurrentItem();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    private void showCurrentItem() {
        mNameTextView.setText(mCurrentItem.getName());
        mQuantityTextView.setText(getString(R.string.quantity_format, mCurrentItem.getQuantity()));
        mDateTextView.setText(getString(R.string.date_format, mCurrentItem.getDeliveryDateString()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_reset:
                mClearedItem = mCurrentItem;
                mCurrentItem = new Item();
                showCurrentItem();

                Snackbar snackbar = Snackbar.make(findViewById(R.id.coordinator_layout), "Item cleared", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mCurrentItem = mClearedItem;
                        showCurrentItem();
                        Snackbar.make(findViewById(R.id.coordinator_layout), "Item restore", Snackbar.LENGTH_SHORT);
                    }
                });
                snackbar.show();
                return true;
            case R.id.action_settings:
                startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
