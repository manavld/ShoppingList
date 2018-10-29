package info.pauek.shoppinglist;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    // TODO: 1. Afegir un CheckBox a cada ítem, per marcar o desmarcar els ítems (al model també!)
    // TODO: 2. Que es puguin afegir elements (+ treure els inicials)
    // TODO: 3. Afegir un menú amb una opció per esborrar de la llista tots els marcats.
    // TODO: 4. Que es pugui esborrar un element amb LongClick (cal fer OnLongClickListener)

    // Model
    List<ShoppingItem> items;

    // Referències a elements de la pantalla
    private RecyclerView items_view;
    private ImageButton btn_add;
    private EditText edit_box;
    private ShoppingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        items = new ArrayList<>();
        items.add(new ShoppingItem("Potatoes", false));
        items.add(new ShoppingItem("Toilet Paper", false));

        items_view = findViewById(R.id.items_view);
        btn_add = findViewById(R.id.btn_add);
        edit_box = findViewById(R.id.edit_box);

        adapter = new ShoppingListAdapter(this, items);

        items_view.setLayoutManager(new LinearLayoutManager(this));
        items_view.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        items_view.setAdapter(adapter);

        adapter.setOnClickListener(new ShoppingListAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                String msg = "Has clicat: " + items.get(position).getName();
                Toast.makeText(ShoppingListActivity.this, msg, Toast.LENGTH_SHORT).show();
                items.get(position).setChecked(!items.get(position).isChecked());
                adapter.notifyItemChanged(position);
            }
        });

        adapter.setOnLongClickListener(new ShoppingListAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                OnDeleteItem(position);
            }
        });
    }

    public void AddItemClick (View view) {
        String text = edit_box.getText().toString();
        if(!text.equals("")) {
            items.add(new ShoppingItem(text, false));
        }
        if(text.equals((""))){
            Toast.makeText(this, "You Must Insert Text", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyItemRemoved(items.size());
        edit_box.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        if (item.getItemId() == R.id.delete_item) {
            for(int i=0; i<items.size(); i++){
                if(items.get(i).isChecked() == true){
                    items.remove(items.get(i));
                    adapter.notifyItemRemoved(i);
                    i--;
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void OnDeleteItem(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String delete_text = "Are you sure you want to delete '" + items.get(position).getName() + "' from your list?";
        builder.setTitle("Delete?")
                .setMessage(delete_text)
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                         Toast.makeText(ShoppingListActivity.this, items.get(position).getName() + " deleted", Toast.LENGTH_SHORT).show();
                          items.remove(position);
                          adapter.notifyItemRemoved(position);
                    }
                 })
                .setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }
}
