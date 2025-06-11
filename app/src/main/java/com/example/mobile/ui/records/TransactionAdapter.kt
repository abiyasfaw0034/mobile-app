package com.example.mobile.ui.records

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.data.database.entities.Transaction
import com.example.mobile.databinding.ItemTransactionBinding
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(
    private val items: List<Transaction>,
    private val onLongClick: (Transaction) -> Unit
) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            binding.textCategory.text = transaction.category
            binding.textNote.text = transaction.note ?: "-"

            val isExpense = transaction.type.lowercase() == "expense"
            binding.textAmount.text = if (isExpense) "-${transaction.amount}" else "+${transaction.amount}"
            binding.textAmount.setTextColor(
                if (isExpense)
                    binding.root.context.getColor(android.R.color.holo_red_light)
                else
                    binding.root.context.getColor(android.R.color.holo_green_light)
            )

            binding.textType.text = if (isExpense) "Expenses: ${transaction.amount}" else "Income: ${transaction.amount}"
            binding.textDate.text = formatDate(transaction.date)

            // 🟠 Long click to trigger context menu
            binding.root.setOnLongClickListener {
                onLongClick(transaction)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }
}

fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMMM EEEE", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
