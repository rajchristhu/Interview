package team.cesea.Interview.adapters

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import team.cesea.Interview.R
import team.cesea.Interview.activities.ListModel

class ShowAdapter (internal var mCtx: Context, internal var listLayoutRes: Int, internal var employeeList: MutableList<ListModel>, internal var mDatabase: SQLiteDatabase) : ArrayAdapter<ListModel>(mCtx, listLayoutRes, employeeList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(listLayoutRes, null)

        val employee = employeeList[position]


        val textViewName = view.findViewById<TextView>(R.id.textViewName)
        val textViewDept = view.findViewById<TextView>(R.id.textViewDepartment)
        val textViewSalary = view.findViewById<TextView>(R.id.textViewSalary)
        val textViewJoiningDate = view.findViewById<TextView>(R.id.textViewJoiningDate)


        textViewName.text = employee.name
        textViewDept.text = employee.mail



        val buttonDelete = view.findViewById<Button>(R.id.buttonDeleteEmployee)
        val buttonEdit = view.findViewById<Button>(R.id.buttonEditEmployee)

        buttonEdit.setOnClickListener { updateEmployee(employee) }

        buttonDelete.setOnClickListener {
            val builder = AlertDialog.Builder(mCtx)
            builder.setTitle("Are you sure?")
            builder.setPositiveButton("Yes") { dialogInterface, i ->
                val sql = "DELETE FROM employees WHERE id = ?"
                mDatabase.execSQL(sql, arrayOf(employee.id))
                reloadEmployeesFromDatabase()
            }
            builder.setNegativeButton("Cancel") { dialogInterface, i -> }
            val dialog = builder.create()
            dialog.show()
        }

        return view
    }

    private fun updateEmployee(employee: ListModel) {
        val builder = AlertDialog.Builder(mCtx)

        val inflater = LayoutInflater.from(mCtx)
        val view = inflater.inflate(R.layout.listadapter, null)
        builder.setView(view)


        val editTextName = view.findViewById<EditText>(R.id.editTextName)
        val editTextSalary = view.findViewById<EditText>(R.id.editTextSalary)

        editTextName.setText(employee.name)

        val dialog = builder.create()
        dialog.show()

        view.findViewById<View>(R.id.buttonUpdateEmployee).setOnClickListener(View.OnClickListener {
            val name = editTextName.text.toString().trim { it <= ' ' }
            val salary = editTextSalary.text.toString().trim { it <= ' ' }

            if (name.isEmpty()) {
                editTextName.error = "Name can't be blank"
                editTextName.requestFocus()
                return@OnClickListener
            }

            if (salary.isEmpty()) {
                editTextSalary.error = "Salary can't be blank"
                editTextSalary.requestFocus()
                return@OnClickListener
            }

            val sql = "UPDATE employees \n" +
                    "SET name = ?, \n" +
                    "salary = ? \n" +
                    "WHERE id = ?;\n"

            mDatabase.execSQL(sql, arrayOf(name,  salary, employee.id.toString()))
            Toast.makeText(mCtx, "Employee Updated", Toast.LENGTH_SHORT).show()
            reloadEmployeesFromDatabase()

            dialog.dismiss()
        })
    }

    private fun reloadEmployeesFromDatabase() {
        val cursorEmployees = mDatabase.rawQuery("SELECT * FROM employees", null)
        if (cursorEmployees.moveToFirst()) {
            employeeList.clear()
            do {
                employeeList.add(ListModel(
                    cursorEmployees.getInt(0),
                    cursorEmployees.getString(1),
                    cursorEmployees.getString(2)
                ))
            } while (cursorEmployees.moveToNext())
        }
        cursorEmployees.close()
        notifyDataSetChanged()
    }

}
