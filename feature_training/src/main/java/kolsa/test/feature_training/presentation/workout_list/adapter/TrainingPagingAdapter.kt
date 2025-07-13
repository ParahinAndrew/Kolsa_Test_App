package kolsa.test.feature_training.presentation.workout_list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getString
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kolsa.test.core.R
import kolsa.test.core.common.TypesTraining
import kolsa.test.data.local.database.TrainingEntity
import kolsa.test.feature_training.databinding.ItemTrainingBinding

class TrainingPagingAdapter(
    private val onItemClicked: (TrainingEntity) -> Unit
) : PagingDataAdapter<TrainingEntity, TrainingPagingAdapter.TrainingViewHolder>(
    TRAINING_DIFF_CALLBACK
) {

    inner class TrainingViewHolder(private val binding: ItemTrainingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { training ->
                    onItemClicked(training)
                }
            }
        }

        fun bind(training: TrainingEntity) {
            binding.apply {
                titleText.text = training.title ?: ""
                typeText.text =
                    getString(root.context, TypesTraining.fromInt(training.type).stringTypeId)
                durationText.text = when {
                    training.duration == null -> ""
                    training.duration!!.all { it.isDigit() } -> {
                        if (training.duration!!.isNotEmpty()) "${training.duration} ${
                            root.context.getString(
                                R.string.minutes
                            )
                        }" else ""
                    }

                    else -> {
                        training.duration
                    }
                }
                descriptionText.text = training.description ?: ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val binding =
            ItemTrainingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrainingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val item = getItem(position)
        item?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val TRAINING_DIFF_CALLBACK = object : DiffUtil.ItemCallback<TrainingEntity>() {
            override fun areItemsTheSame(
                oldItem: TrainingEntity,
                newItem: TrainingEntity
            ): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(
                oldItem: TrainingEntity,
                newItem: TrainingEntity
            ): Boolean =
                oldItem == newItem
        }
    }
}