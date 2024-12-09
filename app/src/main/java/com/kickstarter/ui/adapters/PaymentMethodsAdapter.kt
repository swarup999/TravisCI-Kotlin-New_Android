package com.kickstarter.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.kickstarter.R
import com.kickstarter.databinding.ItemPaymentMethodBinding
import com.kickstarter.models.StoredCard
import com.kickstarter.ui.viewholders.KSViewHolder
import com.kickstarter.ui.viewholders.PaymentMethodsViewHolder

const val SECTION_CARDS = 0

class PaymentMethodsAdapter(private val delegate: PaymentMethodsViewHolder.Delegate, diffCallback: DiffUtil.ItemCallback<Any>) : KSListAdapter(diffCallback) {

    init {
        addSection(emptyList<Any>())
    }

    interface Delegate : PaymentMethodsViewHolder.Delegate

    override fun layout(sectionRow: SectionRow?): Int = R.layout.item_payment_method

    override fun viewHolder(layout: Int, viewGroup: ViewGroup): KSViewHolder = PaymentMethodsViewHolder(ItemPaymentMethodBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false), delegate)

    fun populateCards(cards: List<StoredCard>) {
        setSection(SECTION_CARDS, cards)
        submitList(items())
    }
}
