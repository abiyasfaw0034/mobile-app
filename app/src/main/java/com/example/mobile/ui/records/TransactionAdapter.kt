package com.example.mobile.ui.records

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile.R
import com.example.mobile.data.database.entities.Transaction
import com.example.mobile.databinding.ItemTransactionBinding
import java.text.SimpleDateFormat
import java.util.*

class TransactionAdapter(private val items: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemTransactionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transaction: Transaction) {
            // Set category and note
            binding.textCategory.text = transaction.category
            binding.textNote.text = transaction.note ?: "-"

            // Format amount
            val isExpense = transaction.type.lowercase() == "expense"
            binding.textAmount.text = if (isExpense) "-${transaction.amount}" else "+${transaction.amount}"
            binding.textAmount.setTextColor(
                if (isExpense)
                    binding.root.context.getColor(android.R.color.holo_red_light)
                else
                    binding.root.context.getColor(android.R.color.holo_green_light)
            )

            // Set type summary (optional)
            binding.textType.text = if (isExpense) "Expenses: ${transaction.amount}" else "Income: ${transaction.amount}"

            // Format date
            binding.textDate.text = formatDate(transaction.date)

            // Set icon based on category name
//            binding.iconCategory.setImageResource(getCategoryIcon(transaction.category))
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTransactionBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

}
fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMMM  EEEE", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
//fun getCategoryIcon(category: String): Int {
//    return when (category.lowercase()) {
//        "food", "burger" -> R.drawable.ic_food
//        "salary", "income" -> R.drawable.ic_salary
//        "shopping" -> R.drawable.ic_shopping
//        "transport" -> R.drawable.ic_transport
//        else -> R.drawable.ic_category_default
//    }
//}
