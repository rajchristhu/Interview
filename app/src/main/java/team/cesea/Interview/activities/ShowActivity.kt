package team.cesea.Interview.activities

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import team.cesea.Interview.R
import team.cesea.Interview.adapters.ShowAdapter
import java.util.ArrayList

class ShowActivity : AppCompatActivity() {

    private lateinit var employeeList: MutableList<ListModel>
    private lateinit var mDatabase: SQLiteDatabase
    private lateinit var listViewEmployees: ListView
    private lateinit var adapter: ShowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show)

        listViewEmployees = findViewById(R.id.listViewEmployees) as ListView
        employeeList = ArrayList()

        mDatabase = openOrCreateDatabase(ListActivity.DATABASE_NAME, Context.MODE_PRIVATE, null)

        showEmployeesFromDatabase()
    }

    private fun showEmployeesFromDatabase() {
        val cursorEmployees = mDatabase.rawQuery("SELECT * FROM employees", null)

        if (cursorEmployees.moveToFirst()) {
            do {
                employeeList.add(ListModel(
                    cursorEmployees.getInt(0),
                    cursorEmployees.getString(1),
                    cursorEmployees.getString(2)
                ))
            } while (cursorEmployees.moveToNext())
        }
        cursorEmployees.close()

        adapter = ShowAdapter(this, R.layout.list_layout_employee, employeeList, mDatabase)

        listViewEmployees.adapter = adapter
    }

}
