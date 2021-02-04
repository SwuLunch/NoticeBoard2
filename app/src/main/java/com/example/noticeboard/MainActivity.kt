package com.example.noticeboard


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem

import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    lateinit var recordRv : RecyclerView
    lateinit var dbHelper: MyDbHelper
    lateinit var addRecordBtn:FloatingActionButton
    private val NEWEST_FIRST = Constants.C_ADDED_TIME_STAMP+" DESC"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = MyDbHelper(this)
        addRecordBtn = findViewById<FloatingActionButton>(R.id.addRecordBtn)
        recordRv = findViewById<RecyclerView>(R.id.recordsRv)

        loadRecords()

        //click FloatingActionButton to start AddUpdateRecordActivity

        addRecordBtn.setOnClickListener{
            startActivity(Intent(this,AddUpdateRecordActivity::class.java))
        }
    }

    private fun loadRecords() {
        val adapterRecord = AdapterRecord(this, dbHelper.getAllRecords(NEWEST_FIRST))

        recordRv.adapter = adapterRecord
    }

    private fun searchRecords(query:String){
        val adapterRecord = AdapterRecord(this, dbHelper.searchRecords(query))
        recordRv.adapter = adapterRecord
    }

    override fun onResume() {
        super.onResume()
        loadRecords()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val item = menu.findItem(R.id.action_search)

        val searchView = item.actionView as androidx.appcompat.widget.SearchView

        searchView.setOnQueryTextListener(object :
        androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null)
                    searchRecords(newText)
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query != null)
                    searchRecords(query)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return super.onOptionsItemSelected(item)
    }
}