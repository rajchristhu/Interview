package team.cesea.Interview.activities

import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_list.*
import team.cesea.Interview.R
import java.text.SimpleDateFormat
import java.util.*

class ListActivity : AppCompatActivity(), View.OnClickListener  {
    private lateinit var textViewViewEmployees: TextView
    private lateinit var editTextName: EditText
    private lateinit var editTextSalary: EditText
    private lateinit var spinnerDepartment: Spinner

    private lateinit var mDatabase: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        textViewViewEmployees = findViewById<TextView>(R.id.textViewViewEmployees)
        editTextName = findViewById<EditText>(R.id.editTextName)
        editTextSalary = findViewById<EditText>(R.id.editTextSalary)

        findViewById<Button>(R.id.buttonAddEmployee).setOnClickListener(this)
        textViewViewEmployees.setOnClickListener(this)

        //creating a database
        mDatabase = openOrCreateDatabase(DATABASE_NAME, Context.MODE_PRIVATE, null)

        createEmployeeTable()
    }



    private fun createEmployeeTable() {
        mDatabase.execSQL(
            "CREATE TABLE IF NOT EXISTS employees (\n" +
                    "    id INTEGER NOT NULL CONSTRAINT employees_pk PRIMARY KEY AUTOINCREMENT,\n" +
                    "    name varchar(200) NOT NULL,\n" +
                    "    department varchar(200) NOT NULL\n" +
                    ");"
        )
    }


    private fun inputsAreCorrect(name: String, salary: String): Boolean {
        if (name.isEmpty()) {
            editTextName.error = "Please enter a name"
            editTextName.requestFocus()
            return false
        }

        if (salary.isEmpty() || Integer.parseInt(salary) <= 0) {
            editTextSalary.error = "Please enter salary"
            editTextSalary.requestFocus()
            return false
        }
        return true
    }

    private fun addEmployee() {

        val name = editTextName.text.toString().trim { it <= ' ' }
        val salary = editTextSalary.text.toString().trim { it <= ' ' }
//        val dept = spinnerDepartment.selectedItem.toString()

        val cal = Calendar.getInstance()
        val sdf = SimpleDateFormat("yyyy-mm-dd hh:mm:ss")
        val joiningDate = sdf.format(cal.time)

        if (inputsAreCorrect(name, salary)) {

            val insertSQL = "INSERT INTO employees \n" +
                    "(name, department)\n" +
                    "VALUES \n" +
                    "(?, ?);"

            mDatabase.execSQL(insertSQL, arrayOf(name, "drfs"))

            Toast.makeText(this, "Employee Added Successfully", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonAddEmployee ->
                addEmployee()
            R.id.textViewViewEmployees ->
                startActivity(Intent(this, ShowActivity::class.java))
        }
    }

    companion object {

        val DATABASE_NAME = "myemployeedatabase"
    }
}
