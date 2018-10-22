package com.nnl.password.activity

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.app.Fragment
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.nnl.password.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [MenuFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [MenuFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class MenuFragment : Fragment(), View.OnClickListener {
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_menu, container, false)
        view.findViewById<Button>(R.id.btnMenuAdd).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnMenuAbout).setOnClickListener(this)
        view.findViewById<Button>(R.id.btnMenuChangePassword).setOnClickListener(this)
        return view
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MenuFragment.
         */
        @JvmStatic
        fun newInstance() =
                MenuFragment()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnMenuAdd -> startActivity(Intent(activity, AddItemActivity::class.java))
            R.id.btnMenuChangePassword -> {
                startActivity(Intent(activity, SetPasswordActivity::class.java))
            }
            R.id.btnMenuAbout -> {
                AlertDialog.Builder(activity).setTitle(R.string.About).setMessage(R.string.about_message).show()
            }
        }
    }
}
