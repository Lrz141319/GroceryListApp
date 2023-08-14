package com.lrz141319.grocerylistapp;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    ListView m_ListView;
    ArrayList<String> m_Items;
    ArrayAdapter<String> m_ItemsAdapter;
    EditText m_AddItemEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        m_ListView = (ListView) findViewById(R.id.lstView);
        m_AddItemEditText = (EditText) findViewById(R.id.txtNewItem);
        m_Items = new ArrayList<String>();
        m_Items.add("item one");
        m_Items.add("item two");
        m_ItemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, m_Items);
        m_ListView.setAdapter(m_ItemsAdapter);

        this.SetupListViewListener();
    }

    public void onAddItemClick(View view)
    {
        String toAddString = m_AddItemEditText.getText().toString();
        if(toAddString.length()>0)
        {
            m_ItemsAdapter.add(toAddString);
            m_AddItemEditText.setText("");
        }
    }
    private void SetupListViewListener()
    {
        m_ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long rowId)
            {
                Log.i("MainActivity", "Long Clicked item " + position);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle(R.string.dialog_delete_title)
                        .setMessage(R.string.dialog_delete_msg)
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                m_Items.remove(position);
                                m_ItemsAdapter.notifyDataSetChanged();
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                            }
                        });
                builder.create().show();
                return true;
            }
        });

        m_ListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                String updateItem = (String) m_ItemsAdapter.getItem(position);
                Log.i("MainActivity", "Clicked item" + position + ": " + updateItem);

                Intent intent = new Intent(MainActivity.this, EditToDoItemActivity.class);
                if(intent != null)
                {
                    intent.putExtra("item", updateItem);
                    intent.putExtra("position", position);

                    mLauncher.launch(intent);
                    m_ItemsAdapter.notifyDataSetChanged();
                }
            }
        });
    }
    ActivityResultLauncher<Intent> mLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK)
                {
                    String editedItem = result.getData().getExtras().getString("item");
                    int position = result.getData().getIntExtra("position", -1);
                    m_Items.set(position, editedItem);
                    Log.i("Updated item in list ", editedItem + ", position: " + position);
                    Toast.makeText(getApplicationContext(), "Updated: " + editedItem,
                            Toast.LENGTH_SHORT).show();
                    m_ItemsAdapter.notifyDataSetChanged();
                }
            }
    );
}