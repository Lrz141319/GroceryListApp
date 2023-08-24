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
import android.widget.TextView;
import android.widget.Toast;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    ListView m_ListView;
    ArrayList<String> m_Items;
    ArrayAdapter<String> m_ItemsAdapter;
    EditText m_AddItemEditText;
    TextView m_TxtDate;
    String m_FileName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        m_ListView = (ListView) findViewById(R.id.lstView);
        m_TxtDate = (TextView) findViewById(R.id.txtDate);
        m_AddItemEditText = (EditText) findViewById(R.id.txtNewItem);

        Bundle bundle = getIntent().getExtras();
        String date = bundle != null ? bundle.getString("date") : null;
        m_TxtDate.setText(date);
        m_FileName = date + ".txt";

        ReadItemsFromFile(m_FileName);

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
            SaveItemsToFile(m_FileName);
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
                                SaveItemsToFile(m_FileName);
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
                    SaveItemsToFile(m_FileName);
                }
            }
    );

    private void ReadItemsFromFile(String fileName)
    {
        File filesDir = getFilesDir();
        File groceryFile = new File(filesDir, fileName);
        if(!groceryFile.exists())
        {
            m_Items = new ArrayList<String>();
        }
        else
        {
            try
            {
                m_Items = new ArrayList<String>(FileUtils.readLines(groceryFile));
            }catch(IOException e)
            {
                m_Items = new ArrayList<String>();
            }
        }
    }

    private void SaveItemsToFile(String fileName)
    {
        File filesDir = getFilesDir();
        File groceryFile = new File(filesDir, fileName);
        try
        {
            FileUtils.writeLines(groceryFile, m_Items);
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}