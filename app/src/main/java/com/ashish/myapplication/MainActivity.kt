package com.ashish.myapplication

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.ashish.myapplication.databinding.ActivityMainBinding
import java.util.Collections


class MainActivity : ComponentActivity() {

    private lateinit var binding: ActivityMainBinding

    private var mContext: Context? = null
    private var mActivity: Activity? = null

    private var inputList1 = ArrayList<Int>()
    private var inputList2 = ArrayList<Int>()
    private var inputList3 = ArrayList<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mContext = this@MainActivity
        mActivity = this@MainActivity

        initViews()
    }

    fun initViews() {

        binding.textInputEditText3.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_SEND) {
                    checkValidation()
                    return true
                }
                return false
            }
        })

        binding.buttonCalculate.setOnClickListener {
            checkValidation()
        }

        binding.textViewReset.setOnClickListener {

            binding.groupResult.visibility = View.GONE

            binding.textInputEditText1.setText("")
            binding.textInputEditText2.setText("")
            binding.textInputEditText3.setText("")

            binding.textInputEditText1.requestFocus()
        }
    }

    fun checkValidation() {

        if (binding.textInputEditText1.validateEditText() &&
            binding.textInputEditText2.validateEditText() &&
            binding.textInputEditText3.validateEditText()
        ) {
            Utils.hideKeyBoard(mActivity!!, binding.textInputEditText1)

            convertDataToListsAndContinue()
        }
    }

    fun convertDataToListsAndContinue() {

        try {
            val strList1 = binding.textInputEditText1.text.toString().trim()
            val strList2 = binding.textInputEditText2.text.toString().trim()
            val strList3 = binding.textInputEditText3.text.toString().trim()


            val items1 = strList1.split(",")
            val items2 = strList2.split(",")
            val items3 = strList3.split(",")

            inputList1.clear()
            inputList2.clear()
            inputList3.clear()

            for (items in items1)
                inputList1.add(items.toInt())

            for (items in items2)
                inputList2.add(items.toInt())

            for (items in items3)
                inputList3.add(items.toInt())

            calculations()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun calculations() {

        try {
            val intersection = ArrayList<Int>()
            for (elements in inputList1) {
                if (inputList2.contains(elements)) {
                    if (inputList3.contains(elements)) {
                        intersection.add(elements)
                    }
                }
            }

            binding.textViewIntersect.text = getString(R.string.intersect, intersection.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            val unionSet: MutableSet<Int> = HashSet()
            unionSet.addAll(inputList1)
            unionSet.addAll(inputList2)
            unionSet.addAll(inputList3)

            val unionList = ArrayList<Int>(unionSet)
            unionList.sort()

            binding.textViewUnion.text = getString(R.string.union, unionList.toString())

            val largestNumber = Collections.max(unionSet)
            binding.textViewHighestNumber.text = getString(R.string.highest_number, largestNumber.toString())

            binding.groupResult.visibility = View.VISIBLE
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun EditText.validateEditText(): Boolean {

        try {
            val regex = Regex("^\\d+(,\\d+)*\$")
            val text = this.text.toString().trim()

            if (text.isEmpty()) {
                this.error = getString(R.string.please_provide_input)
                this.requestFocus()

                return false
            } else if (!text.matches(regex)) {
                this.error = getString(R.string.please_provide_valid_input)
                this.requestFocus()

                return false
            } else {
                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }
}
