package com.mobiledrivetech.external.sample.presentation.ui.fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.mobiledrivetech.external.sample.R
import com.mobiledrivetech.external.sample.databinding.FragmentCommandsListBinding
import com.mobiledrivetech.external.sample.presentation.ui.adapters.CommandsListAdapter
import com.mobiledrivetech.external.sample.presentation.ui.viewmodels.CommandsListViewModel

class CommandsListFragment : Fragment() {

    companion object {
        fun newInstance() = CommandsListFragment()
    }

    private val viewModel: CommandsListViewModel by viewModels()
    private lateinit var binding: FragmentCommandsListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.commandResult.observe(this) {
            val alertDialog = AlertDialog.Builder(requireContext())
            alertDialog.setTitle(R.string.dialog_title)
            alertDialog.setMessage(it)
            alertDialog.setPositiveButton(R.string.dialog_positive_btn_text) { dialog, _ ->
                dialog.dismiss()
            }
            alertDialog.show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCommandsListBinding.inflate(inflater, container, false)
        val commandsListAdapter =
            CommandsListAdapter(viewModel.allCommands) {
                viewModel.onItemClick(it.command)
            }
        binding.commandsList.apply {
            adapter = commandsListAdapter
            addItemDecoration(
                androidx.recyclerview.widget.DividerItemDecoration(
                    context,
                    androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
                )
            )
        }
        return binding.root
    }
}
