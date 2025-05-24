package com.example.financetracker.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.financetracker.R

class OnboardingPagerAdapter(private val fragment: androidx.fragment.app.Fragment) :
    RecyclerView.Adapter<OnboardingPagerAdapter.OnboardingViewHolder>() {

    private val onboardingItems = listOf(
        OnboardingItem(
            R.drawable.accounting,
            R.string.welcome_to_finance_tracker,
            R.string.track_your_finances
        ),
        OnboardingItem(
            R.drawable.budgeton,
            R.string.budget_management,
            R.string.set_monthly_budgets
        ),
        OnboardingItem(
            R.drawable.report_o,
            R.string.insightful_reports,
            R.string.visualize_spending
        )
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val item = onboardingItems[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.titleTextView.setText(item.titleResId)
        holder.descriptionTextView.setText(item.descriptionResId)
    }

    override fun getItemCount(): Int = onboardingItems.size

    class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.onboarding_image)
        val titleTextView: TextView = itemView.findViewById(R.id.onboarding_title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.onboarding_description)
    }

    data class OnboardingItem(
        val imageResId: Int,
        val titleResId: Int,
        val descriptionResId: Int
    )
} 